package com.yaowang.douniwan;

import android.app.Activity;
import android.content.Intent;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-12-07 10:34
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public final class s {
    public static final int TYPE_QQ = 0;
    public static final int TYPE_WEIXIN = TYPE_QQ + 1;
    public static final int TYPE_WEIBO = TYPE_WEIXIN + 1;

    protected ISocialize iSocialize;

    private s() {
    }

    static class HolderClass {
        static s INSTANCE = new s();
    }

    public static s instance() {
        return HolderClass.INSTANCE;
    }

    public ISocialize socialize(int type, Activity context) {
        switch (type) {
            case TYPE_QQ:
                iSocialize = new QQSocialize(context);
                return iSocialize;
            case TYPE_WEIBO:
                iSocialize = new WeiboSocialize(context);
                return iSocialize;
            case TYPE_WEIXIN:
                iSocialize = new WeixinSocialize(context);
                return iSocialize;
            default:
                iSocialize = new WeiboSocialize(context);
                return iSocialize;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        iSocialize.onActivityResult(requestCode, resultCode, data);
    }
}
