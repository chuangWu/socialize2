package com.yaowang.douniwan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.Utility;

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
public class WeiboSocialize extends BaseSocialize implements ISocialize {

    private abstract class BaseWeiboListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {
            onCompleted(bundle);
        }

        public abstract void onCompleted(Bundle bundle);

        @Override
        public void onWeiboException(WeiboException e) {
            onToastError(new Exception(context.getString(R.string.error_auth)));
        }

        @Override
        public void onCancel() {
            onToastError(new Exception(context.getString(R.string.cancel_auth)));
        }
    }

    public static final String SCOPE = "all";
    public static final String UID = "uid";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String METHOD = "GET";
    public static final String ID = "id";
    public static final String PROFILE_IMAGE_URL = "profile_image_url";
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String TYPE = "weibo";
    private SsoHandler ssoHandler;
    private Oauth2AccessToken accessToken;
    private IWeiboShareAPI weiboShareAPI;


    public WeiboSocialize(Activity context) {
        super(context);
    }

    @Override
    public void initSocialize() {
        AuthInfo authInfo = new AuthInfo(context, Constant.SINA_APP_KEY, Constant.REDIRECT_URL, SCOPE);
        ssoHandler = new SsoHandler(context, authInfo);
        weiboShareAPI = WeiboShareSDK.createWeiboAPI(context, Constant.SINA_APP_KEY);
        weiboShareAPI.registerApp();
    }


    @Override
    public void makeUserInfo() {
        WeiboParameters params = new WeiboParameters(Constant.SINA_APP_KEY);
        params.put(UID, accessToken.getUid());
        params.put(ACCESS_TOKEN, accessToken.getToken());
        new AsyncWeiboRunner(context).requestAsync(Constant.GET_USERINFO_URL, params, METHOD, new RequestListener() {

            @Override
            public void onComplete(String s) {
                try {
                    if (TextUtils.isEmpty(s))
                        return;
                    resultToEntity(new JSONObject(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

            }

            @Override
            public void onWeiboException(WeiboException e) {
                onToastError(new Exception(context.getString(R.string.error_auth)));
            }
        });
    }

    @Override
    public void resultToEntity(Object object) {
        JSONObject result = null;
        if (accessToken != null && object != null && object instanceof JSONObject) {
            result = (JSONObject) object;
        }
        if (result == null)
            return;
        try {
            ResultEntity entity = new ResultEntity();
            entity.setId(result.getString(ID));
            entity.setHeadpic(result.getString(PROFILE_IMAGE_URL));
            entity.setNickname(result.getString(NAME));
            entity.setSex(result.getString(GENDER).equals("女") ? "0" : "1");
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
        ssoHandler.authorize(new BaseWeiboListener() {
            @Override
            public void onCompleted(Bundle bundle) {
                accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context, accessToken);
                makeUserInfo();
            }
        });
    }


    @Override
    public void share(ShareEntity entity, OnShareSocializeListener listener) {
        onShareSocializeListener = listener;
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        setShareContent(entity, weiboMessage);
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        AuthInfo authInfo = new AuthInfo(context, Constant.SINA_APP_KEY, Constant.REDIRECT_URL, SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(context.getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        weiboShareAPI.sendRequest(context, request, authInfo, token, new BaseWeiboListener() {
            @Override
            public void onCompleted(Bundle bundle) {
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(context.getApplicationContext(), newToken);
                if (onShareSocializeListener != null) {
                    onShareSocializeListener.onShareCompleted();
                }
            }
        });

    }

    private void setShareContent(ShareEntity entity, WeiboMultiMessage weiboMessage) {
        TextObject textObject = new TextObject();
        textObject.text = entity.getSummary();

        ImageObject imageObject = new ImageObject();

        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = entity.getTitle();
        mediaObject.description = entity.getSummary();

        if (entity.getImgBitmap() != null) {

            Bitmap bitmap = entity.getImgBitmap();
            mediaObject.setThumbImage(bitmap);

            imageObject.setImageObject(bitmap);
            weiboMessage.imageObject = imageObject;
        } else {
            Bitmap tempBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            mediaObject.setThumbImage(tempBitmap);
            imageObject.setImageObject(tempBitmap);
            weiboMessage.imageObject = imageObject;
        }
        mediaObject.actionUrl = entity.getTargetUrl();
        mediaObject.defaultText = context.getString(R.string.app_name);

        weiboMessage.textObject = textObject;
        weiboMessage.mediaObject = mediaObject;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ssoHandler != null)
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
    }

    public IWeiboShareAPI getWeiboShareAPI() {
        return weiboShareAPI;
    }
}
