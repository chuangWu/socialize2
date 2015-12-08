package com.yaowang.douniwan;

import android.content.Intent;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-12-07 10:39
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public interface ISocialize {


    /**
     * 登录
     */
    void login(BaseSocialize.OnLoginSocializeListener listener);

    /**
     * 分享
     */
    void share(ShareEntity entity, BaseSocialize.OnShareSocializeListener listener);

    /**
     * 处理返回数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
