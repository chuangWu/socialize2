package com.yaowang.douniwan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-12-07 10:45
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public class QQSocialize extends BaseSocialize implements ISocialize {

    public static final String TYPE = "QQ";
    public static final String SCOPE = "all";
    public static final String HEADPIC = "figureurl_qq_2";
    public static final String NICKNAME = "nickname";
    public static final String GENDER = "nickname";
    protected Bundle params = new Bundle();

    public abstract class BaseQQListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            doComplete((JSONObject) o);
        }

        @Override
        public void onError(UiError uiError) {
            onToastError(new Exception(context.getString(R.string.socialize_error)));
        }

        @Override
        public void onCancel() {
            onToastError(new Exception(context.getString(R.string.socialize_cancel)));
        }

        public abstract void doComplete(JSONObject jsonObject);
    }

    private Tencent tencent;
    private LoginListener loginListener;
    private ShareListener shareListener;

    public QQSocialize(Activity context) {
        super(context);
    }

    @Override
    public void initSocialize() {
        tencent = Tencent.createInstance(Constant.TENCENT_APP_ID, context);
    }

    @Override
    public void makeUserInfo() {
        if (context != null && tencent != null) {
            UserInfo userInfo = new UserInfo(context, tencent.getQQToken());
            userInfo.getUserInfo(new BaseQQListener() {
                @Override
                public void doComplete(JSONObject jsonObject) {
                    resultToEntity(jsonObject);
                }
            });
        }

    }

    @Override
    public void resultToEntity(Object object) {
        JSONObject result = null;
        if (object instanceof JSONObject) {
            result = (JSONObject) object;
        }
        if (result == null)
            return;
        try {
            if (result != null && tencent != null) {
                ResultEntity entity = new ResultEntity();
                entity.setId(tencent.getOpenId());
                entity.setHeadpic(result.getString(HEADPIC));
                entity.setNickname(result.getString(NICKNAME));
                entity.setSex(result.getString(GENDER).equals("女") ? "1" : "2");
                entity.setType(TYPE);

                if (onLoginSocializeListener != null) {
                    onLoginSocializeListener.onLoginCompleted(entity);
                }
            }
        } catch (Exception e) {
            onToastError(new Exception(context.getString(R.string.socialize_data)));
        }


    }


    @Override
    public void login(BaseSocialize.OnLoginSocializeListener listener) {
        onLoginSocializeListener = listener;
        if (tencent != null && context != null) {
            if (loginListener == null)
                loginListener = new LoginListener();
            tencent.login(context, SCOPE, loginListener);
        }

    }

    private class LoginListener extends BaseQQListener {
        @Override
        public void doComplete(JSONObject jsonObject) {
            try {
                String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                        && !TextUtils.isEmpty(openId)) {
                    tencent.setAccessToken(token, expires);
                    tencent.setOpenId(openId);
                }
                QQSocialize.this.makeUserInfo();
            } catch (Exception e) {
                e.printStackTrace();
                onToastError(e);
            }
        }
    }

    @Override
    public void share(ShareEntity entity, BaseSocialize.OnShareSocializeListener listener) {
        onShareSocializeListener = listener;
        shareToQQ(entity);
        //   shareToQzone(entity);
    }

    private void shareToQQ(ShareEntity entity) {
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, entity.getTitle());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, entity.getTargetUrl());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, entity.getSummary());
        //params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, R.d);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL, entity.getImageUrl());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_APP_NAME, entity.getAppName());
        if (shareListener == null)
            shareListener = new ShareListener();
        tencent.shareToQQ(context, params, shareListener);
    }

    private void shareToQzone(ShareEntity entity) {
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, entity.getTitle());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL, entity.getTargetUrl());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY, entity.getSummary());
        //params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, R.d);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL, entity.getImageUrl());
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_APP_NAME, entity.getAppName());
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, entity.getTitle());//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, entity.getSummary());//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, entity.getTargetUrl());//必填
        ArrayList<String> list = new ArrayList<String>();
        list.add(entity.getImageUrl());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        if (shareListener == null)
            shareListener = new ShareListener();
        tencent.shareToQzone(context, params, shareListener);
    }

    private class ShareListener extends BaseQQListener {
        @Override
        public void doComplete(JSONObject jsonObject) {
            if (onShareSocializeListener != null) {
                onShareSocializeListener.onShareCompleted();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (tencent != null && loginListener != null)
            tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (tencent != null && shareListener != null)
            tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
    }
}
