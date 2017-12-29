package com.timi.qrcodeapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.timi.qrcodeapplication.uils.InputMethodUtils;
import com.timi.zxingscanlibrary.CommonScanActivity;
import com.timi.zxingscanlibrary.utils.Constant;

/**
 * $dsc　基类的Activity
 * author: timi
 * create at: 2017-12-29 14:57
 */

public abstract class BaseActivity extends AppCompatActivity {
    //当前Activity的实例
    static private BaseActivity currentActivity;
    //TAG
    public String TAG = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(setLayoutId());
        //current activity
        currentActivity = this;
        //tag
        TAG = currentActivity.getClass().getSimpleName() + "_";
        //初始化各种数据
        initBundle(savedInstanceState);
        initView();
        initData();
    }
    //设置布局id
    public abstract int setLayoutId();

    //初始化bundle
    public abstract void initBundle(Bundle savedInstanceState);

    //初始化View
    public abstract void initView();

    //初始化数据
    public abstract void initData();

    /**
     * 隐藏软键盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                hideKeyboard(ev, view);//调用方法判断是否需要隐藏键盘
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 隐藏软键盘
     *
     * @param event
     * @param view
     */
    public void hideKeyboard(MotionEvent event, View view) {
        try {
            if (view != null && view instanceof EditText
                    ) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    InputMethodUtils.hidSoftInput(event, BaseActivity.this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 扫码的返回 监听器
     */
    private ScanQRCodeResultListener mListener = null;

    /**
     * 调用相机扫描二维码的方法
     *
     * @param requestCode
     */
    public void scan(int requestCode, ScanQRCodeResultListener listener) {
        if (null != listener) {
            mListener = listener;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 60);
        } else {
            //权限已经被授予，在这里直接写要执行的相应方法即可
            Intent intent = new Intent(this, CommonScanActivity.class);
            intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (null != mListener) {
                    mListener.scanSuccess(requestCode, bundle.getString("result"));
                }
            }
        }
    }

    /**
     * 获取当前Activity的实例
     *
     * @return
     */
    public static Activity getCurrentActivty() {
        return currentActivity;
    }
    /**
     * 设置 文本
     *
     * @param tv
     * @param fomat
     * @param content
     */
    public void setTextViewText(TextView tv, @StringRes int fomat, int content) {
        tv.setText(String.format(getString(fomat), String.valueOf(content)));
    }

    /**
     * 设置 文本
     *
     * @param tv
     */
    public void setTextViewContent(TextView tv, Object object) {
        String content = "";
        if (object instanceof String) {
            content = TextUtils.isEmpty(String.valueOf(object)) ?"无" : String.valueOf(object);
        } else {
        content = String.valueOf(object);
        }
        tv.setText(content);
    }
    /**
     * zxing 扫码的回调接口
     */
    public interface ScanQRCodeResultListener {
        void scanSuccess(int requestCode, String result);
    }

}
