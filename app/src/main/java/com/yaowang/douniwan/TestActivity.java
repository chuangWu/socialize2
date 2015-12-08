package com.yaowang.douniwan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-12-07 13:05
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test);
    }

    public void qqLogin(View view) {
        s.instance().socialize(s.TYPE_QQ, this).login(new BaseSocialize.OnLoginSocializeListener() {
            @Override
            public void onLoginCompleted(ResultEntity entity) {
                showToast(entity.toString());
            }
        });
    }


    public void qqShare(View view) {
        s.instance().socialize(s.TYPE_QQ, this).share(getShareAPPEntity(this), new BaseSocialize.OnShareSocializeListener() {

            @Override
            public void onShareCompleted() {
                showToast("分享完成");
            }
        });
    }


    public void weixinLogin(View view) {
        s.instance().socialize(s.TYPE_WEIXIN, this).login(new BaseSocialize.OnLoginSocializeListener() {
            @Override
            public void onLoginCompleted(ResultEntity entity) {
                showToast(entity.toString());
            }
        });
    }

    public void weixinShare(View view) {
        s.instance().socialize(s.TYPE_WEIXIN, this).share(getShareAPPEntity(this), new BaseSocialize.OnShareSocializeListener() {
            @Override
            public void onShareCompleted() {
                showToast("分享完成");
            }
        });
    }

    public void weiboLogin(View view) {
        s.instance().socialize(s.TYPE_WEIBO, this).login(new BaseSocialize.OnLoginSocializeListener() {
            @Override
            public void onLoginCompleted(ResultEntity entity) {
                showToast(entity.toString());
            }
        });
    }

    public void weiboShare(View view) {
        startActivity(new Intent(this, WeiboActivity.class));
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


    public void showToast(CharSequence content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        s.instance().onActivityResult(requestCode, resultCode, data);
    }
}
