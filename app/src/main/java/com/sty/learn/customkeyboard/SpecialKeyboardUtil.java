package com.sty.learn.customkeyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;

/**
 * Created by Steven.T on 2017/12/6/0006.
 */

public class SpecialKeyboardUtil {
    private Context mContext;
    private Activity mActivity;
    private KeyboardView mKeyboardView;
    private Keyboard mNumberKeyboard;
    private MyKeyboardListener myKeyboardListener;

    public interface MyKeyboardListener{
        public void onDelete();

        public void onNumberPressed(String number);
    }

    public void setMyKeyboardListener(MyKeyboardListener listener){
        this.myKeyboardListener = listener;
    }

    public SpecialKeyboardUtil(Context mContext, Activity mActivity){
        this.mContext = mContext;
        this.mActivity = mActivity;

        mNumberKeyboard = new Keyboard(mContext, R.xml.simple_keyboard_numbers);
        mKeyboardView = (KeyboardView) mActivity.findViewById(R.id.keyboard_view2);
        mKeyboardView.setKeyboard(mNumberKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if(primaryCode == Keyboard.KEYCODE_CANCEL){
                hideKeyboard();
            }else if(primaryCode == Keyboard.KEYCODE_DELETE){
                if(myKeyboardListener != null){
                    myKeyboardListener.onDelete();
                }
            }else{
                if(myKeyboardListener != null){
                    myKeyboardListener.onNumberPressed(Character.toString((char) primaryCode));
                }
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    public void hideKeyboard(){
        int visibility = mKeyboardView.getVisibility();
        if(visibility == View.VISIBLE){
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    public void showKeyboard(){
        int visibility = mKeyboardView.getVisibility();
        if(visibility == View.INVISIBLE || visibility == View.GONE){
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }
}
