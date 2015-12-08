package com.yaowang.douniwan;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-12-07 10:43
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public abstract class BaseSocialize {
    protected Activity context;

    public BaseSocialize(Activity context) {
        this.context = context;
        initSocialize();
    }


    /**
     * 初始化
     */
    public abstract void initSocialize();

    /**
     * 用户信息处理
     */
    public abstract void makeUserInfo();

    /**
     * result转实体
     */
    public abstract void resultToEntity(Object result);

    public void showToast(@Nullable CharSequence content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@Nullable int resId) {
        showToast(context.getString(resId));
    }

    public void onToastError(Throwable ex) {
        showToast(ex.getMessage());
    }

    public interface OnLoginSocializeListener {
        void onLoginCompleted(ResultEntity entity);
    }

    public interface OnShareSocializeListener {
        void onShareCompleted();
    }

    public OnShareSocializeListener onShareSocializeListener;
    public OnLoginSocializeListener onLoginSocializeListener;

}
