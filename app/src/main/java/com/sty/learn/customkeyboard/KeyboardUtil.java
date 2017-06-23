package com.sty.learn.customkeyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import java.util.List;

/**
 * Created by shity on 2017-6-23 09:33:12.
 */
public class KeyboardUtil {
    private Context mContext;
    private Activity mActivity;
    //KeyboardView:A view that renders a virtual Keyboard. It handles rendering of keys and detecting key presses and touch movements
    private KeyboardView mKeyboardView;
    //Keyboard:Loads an XML description of a keyboard and stores the attributes of the keys. A keyboard consists of rows of keys.
    private Keyboard mNumberKeyboard; // 数字键盘
    private Keyboard mLetterKeyboard; // 字母键盘

    private boolean isNumber = true;  // 是否数字键盘
    private boolean isUpper = false;   // 是否大写
    private EditText mEditText;

    public KeyboardUtil(Context context, Activity activity, EditText editText) {
        mContext = context;
        mActivity = activity;
        mEditText = editText;

        mNumberKeyboard = new Keyboard(mContext, R.xml.keyboard_numbers);
        mLetterKeyboard = new Keyboard(mContext, R.xml.keyboard_qwerty);
        mKeyboardView = (KeyboardView) mActivity.findViewById(R.id.keyboard_view);
        mKeyboardView.setKeyboard(mNumberKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(true);
        mKeyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override //Send a key press to the listener.
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mEditText.getText();
            int start = mEditText.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) { // cancel
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) { // 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) { // 大小写切换
                changeKeyboart();
                mKeyboardView.setKeyboard(mLetterKeyboard);

            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) { // 数字与字母键盘互换
                if (isNumber) {
                    isNumber = false;
                    mKeyboardView.setKeyboard(mLetterKeyboard);
                } else {
                    isNumber = true;
                    mKeyboardView.setKeyboard(mNumberKeyboard);
                }

            } else if (primaryCode == 57419) { // 左移
                if (start > 0) {
                    mEditText.setSelection(start - 1);
                }

            } else if (primaryCode == 57419) { // 右移
                if (start > mEditText.length()) {
                    mEditText.setSelection(start + 1);
                }
            } else { // 输入键盘值
                editable.insert(start, Character.toString((char) primaryCode));
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

    private void changeKeyboart() {
        List<Keyboard.Key> keyList = mLetterKeyboard.getKeys();
        if (isUpper) { // 大写切换小写
            isUpper = false;
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isLetter(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else { // 小写切换成大写
            isUpper = true;
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isLetter(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    /**
     * 判断是否是字母
     */
    private boolean isLetter(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }

    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }


}
