package com.yaowang.douniwan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaowang.douniwan.Constant;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-08-20 18:39
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    public static final String TAG_OK = "TAG_OK";
    public static final String TAG_CANCEL = "TAG_CANCEL";
    public static final String TAG_SHARE = "TAG_SHARE";
    public static final String TAG_RESPONSE_CODE = "TAG_RESPONSE_CODE";
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_KEY, false);
        api.registerApp(Constant.WEIXIN_APP_KEY);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("TAG", baseReq.getType() + "");
    }

    @Override
    public void onResp(BaseResp resp) {
        Intent intent = new Intent();
        intent.setAction(WXEntryActivity.class.getSimpleName());
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp instanceof SendAuth.Resp) {
                    SendAuth.Resp response = (SendAuth.Resp) resp;
                    intent.putExtra(TAG_RESPONSE_CODE, response.code);
                } else {
                    intent.putExtra(TAG_RESPONSE_CODE, TAG_SHARE);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                intent.putExtra(TAG_RESPONSE_CODE, TAG_CANCEL);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
        sendBroadcast(intent);
        this.finish();
    }


}
