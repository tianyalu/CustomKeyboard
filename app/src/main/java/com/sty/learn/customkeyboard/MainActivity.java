package com.sty.learn.customkeyboard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private LinearLayout llPointContainer;
    private ArrayList<View> pointViewList;
    private String pwd = "";

    private int index = -1;
    private int maxLength = 6;

    SpecialKeyboardUtil specialKeyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mActivity = this;

        mUsernameEt = (EditText) findViewById(R.id.editText1);
        mPasswordEt = (EditText) findViewById(R.id.editText2);

        mUsernameEt.setInputType(InputType.TYPE_NULL); //输入类型为没有指定明确的类型的特殊内容类型
        mUsernameEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new KeyboardUtil(mContext, mActivity, mUsernameEt).showKeyboard();
                return true;
            }
        });

        mPasswordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inputType = mPasswordEt.getInputType();
                mPasswordEt.setInputType(InputType.TYPE_NULL);
                new KeyboardUtil(mContext, mActivity, mPasswordEt).showKeyboard();
                mPasswordEt.setInputType(inputType);
                return true;
            }
        });


        initSpecialKeyboard();
        setListeners();
    }

    private void initSpecialKeyboard(){
        specialKeyboardUtil = new SpecialKeyboardUtil(mContext, mActivity);
        specialKeyboardUtil.showKeyboard();

        llPointContainer = (LinearLayout) findViewById(R.id.ll_point_container);
        LinearLayout.LayoutParams layoutParams;
        View pointView;
        pointViewList = new ArrayList<>();

        for(int i = 0; i < maxLength; i++) {
            pointView = new View(this);
            layoutParams = new LinearLayout.LayoutParams(28, 28);
            if(i != 0){
                layoutParams.leftMargin = 32;
            }
            pointView.setBackgroundResource(R.drawable.selector_bg_point);
            pointView.setEnabled(true);
            pointViewList.add(pointView);
            llPointContainer.addView(pointView, layoutParams);
        }

    }

    private void setListeners(){
        specialKeyboardUtil.setMyKeyboardListener(new SpecialKeyboardUtil.MyKeyboardListener() {
            @Override
            public void onDelete() {
                descPassword();
            }

            @Override
            public void onNumberPressed(String number) {
                incPassword(number);
            }
        });

        llPointContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                specialKeyboardUtil.showKeyboard();
                return true;
            }
        });
    }

    private void descPassword(){
        if(!TextUtils.isEmpty(pwd)){
            pwd = pwd.substring(0, pwd.length() - 1);
        }
        if(index > -1){
            pointViewList.get(index).setEnabled(true);
            index --;
        }
        Log.i("Tag", "当前的密码为：" + pwd);
    }

    private void incPassword(String number){
        if(TextUtils.isEmpty(pwd) || pwd.length() < maxLength){
            pwd += number;
        }
        Log.i("Tag", "当前的密码为：" + pwd);
        if(index < maxLength - 1){
            index ++;
            pointViewList.get(index).setEnabled(false);
            if(index == maxLength -1){
                Log.i("Tag", "密码输入完成了：" + pwd);
            }
        }
    }

    private void clearPassword(){
        for(int i = 0; i < maxLength; i++){
            pointViewList.get(i).setEnabled(true);
        }
        index = -1;
        pwd = "";
    }
}
