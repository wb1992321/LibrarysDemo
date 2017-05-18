package cn.wang.img.selector.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

import cn.wang.img.selector.R;

/**
 * author : wangshuai Created on 2017/5/16
 * email : wangs1992321@gmail.com
 */
public class MyCheckTextView extends CheckBox {

    private CharSequence selectText, selectedText;

    public MyCheckTextView(Context context) {
        super(context);
        initAttrs(null);
    }

    public MyCheckTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public MyCheckTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }


    private void initAttrs(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyCheckTextView);
        selectText = typedArray.getText(R.styleable.MyCheckTextView_selectText);
        selectedText = typedArray.getText(R.styleable.MyCheckTextView_selectedText);
        setText(selectText);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (isChecked()) {
            setText(selectedText);
        } else setText(selectText);
    }

}
