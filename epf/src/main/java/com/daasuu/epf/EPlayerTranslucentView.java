package com.daasuu.epf;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by LukeNeedham on 2020/01/14.
 */
public class EPlayerTranslucentView extends EPlayerView {

    public EPlayerTranslucentView(Context context) {
        this(context, null);
    }

    public EPlayerTranslucentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setZOrderOnTop(true);
    }
}
