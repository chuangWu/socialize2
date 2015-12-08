package com.yaowang.douniwan;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.common.AssistActivity;

/**
 * @author : created by chuangWu
 * @version : 0.01
 * @email : chuangwu127@gmail.com
 * @created time : 2015-12-08 10:28
 * @description : none
 * @for your attention : none
 * @revise : none
 */
public class MyAssistActivity extends AssistActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onActivityResult(int i, int i1, Intent intent) {
        super.onActivityResult(i, i1, intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
