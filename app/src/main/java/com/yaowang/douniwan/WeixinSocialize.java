package com.yaowang.douniwan;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaowang.douniwan.wxapi.WXEntryActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-12-07 10:53
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public class WeixinSocialize extends BaseSocialize implements ISocialize {
    public static final String SCOPE = "snsapi_userinfo";
    public static final String STATE = "wechat_sdk_dnw";
    public static final String OPENID = "openid";
    public static final String SEX = "sex";
    public static final String NICKNAME = "nickname";
    public static final String HEADIMGURL = "headimgurl";
    public static final String TYPE = "WEIXIN";

    private IWXAPI api;
    private ResponseReceiver responseReceiver;
    private JSONObject tokenObject;

    public WeixinSocialize(Activity context) {
        super(context);
    }

    @Override
    public void initSocialize() {
        api = WXAPIFactory.createWXAPI(context, Constant.WEIXIN_APP_KEY, true);
        isWXAppInstalledAndSupported(api);
        api.registerApp(Constant.WEIXIN_APP_KEY);
        if (responseReceiver == null)
            responseReceiver = new ResponseReceiver();
        context.registerReceiver(responseReceiver, new IntentFilter(WXEntryActivity.class.getSimpleName()));
    }

    @Override
    public void makeUserInfo() {
        //TODO
        try {
            if (tokenObject == null)
                return;
            String info_url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + tokenObject.getString("access_token") + "&openid=" + tokenObject.getString("openid");
            new AsyncHttpClient().get(info_url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        resultToEntity(new JSONObject(new String(responseBody)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("onFailure", responseBody.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resultToEntity(Object object) {
        JSONObject result = null;
        if (object != null && object instanceof JSONObject) {
            result = (JSONObject) object;
        }
        if (result != null)
            try {
                ResultEntity entity = new ResultEntity();
                entity.setId(result.getString(OPENID));
                entity.setSex(result.getString(SEX));
                entity.setNickname(result.getString(NICKNAME));
                entity.setHeadpic(result.getString(HEADIMGURL));
                entity.setType(TYPE);
                if (onLoginSocializeListener != null) {
                    onLoginSocializeListener.onLoginCompleted(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    @Override
    public void login(OnLoginSocializeListener listener) {
        onLoginSocializeListener = listener;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = SCOPE;
        req.state = STATE;
        api.sendReq(req);
    }

    @Override
    public void share(ShareEntity entity, OnShareSocializeListener listener) {
        onShareSocializeListener = listener;
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = entity.getTargetUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = entity.getTitle();
        msg.description = entity.getSummary();
        msg.setThumbImage(entity.getImgBitmap());

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = entity.getFlag() == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private void isWXAppInstalledAndSupported(IWXAPI api) {
        boolean isInstalledWX = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if (!isInstalledWX) {
            onToastError(new Exception(context.getString(R.string.socialize_weixin_install)));
            return;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(responseReceiver);
            String responseCode = intent.getStringExtra(WXEntryActivity.TAG_RESPONSE_CODE);
            if (!TextUtils.isEmpty(responseCode)) {
                if (responseCode.equalsIgnoreCase(WXEntryActivity.TAG_SHARE)) {
                    if (onShareSocializeListener != null) {
                        onShareSocializeListener.onShareCompleted();
                    }
                } else if (responseCode.equalsIgnoreCase(WXEntryActivity.TAG_CANCEL)) {

                } else {
                    WeixinSocialize.this.postToken(responseCode);
                }
            }
        }
    }

    public void postToken(String code) {
        //TODO
        String token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + com.yaowang.douniwan.Constant.WEIXIN_APP_KEY + "&secret=" + com.yaowang.douniwan.Constant.WEIXIN_APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
        new AsyncHttpClient().get(token_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    tokenObject = new JSONObject(new String(responseBody));
                    makeUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("onFailure", responseBody.toString());
            }
        });
    }
}
