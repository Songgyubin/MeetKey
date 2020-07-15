package com.naver.naverspeech.client;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class Make_Group_Check extends RelativeLayout implements Checkable {


    public Make_Group_Check(Context context, AttributeSet attrs){
        super(context,attrs);

    }
    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox) findViewById(R.id.make_group_check);

        if(cb.isChecked() != checked){
            cb.setChecked(checked);
        }

    }
    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox) findViewById(R.id.make_group_check);

        return cb.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox) findViewById(R.id.make_group_check);
        setChecked(cb.isChecked() ? false : true);
    }
}
