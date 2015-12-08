package com.yaowang.douniwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-11-19 17:54
 * @description : none
 * @for your attention : 处理唤起微博客户端时，避免多个com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY问题
 * @revise : none
 */
public class WeiboActivity extends Activity implements IWeiboHandler.Response {

    ISocialize weiboSocialize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        weiboSocialize = s.instance().socialize(s.TYPE_WEIBO, this);
        weiboSocialize.share(getShareAPPEntity(this), new BaseSocialize.OnShareSocializeListener() {
            @Override
            public void onShareCompleted() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);
            }
        });

    }

    public ShareEntity getShareAPPEntity(Context cont) {
        ShareEntity shareEntity = new ShareEntity();
        shareEntity.setTitle(cont.getString(R.string.app_name));
        shareEntity.setAppName(cont.getString(R.string.app_name));
        shareEntity.setImageUrl("http://tstatic.kkt.com/headpic/syshead.png");
        shareEntity.setImgBitmap(BitmapFactory.decodeResource(cont.getResources(), R.mipmap.ic_launcher));
        shareEntity.setSummary("summary");
        shareEntity.setType("app");
        shareEntity.setTargetUrl("http://www.baidu.com");
        return shareEntity;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ((WeiboSocialize) weiboSocialize).getWeiboShareAPI().handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        finish();
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(this, "上传信息", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }
}
