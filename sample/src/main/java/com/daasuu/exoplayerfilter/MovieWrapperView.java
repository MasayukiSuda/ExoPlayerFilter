package com.daasuu.exoplayerfilter;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Aspect 16 : 9 of View
 * Created by sudamasayuki on 2017/05/17.
 */
public class MovieWrapperView extends FrameLayout {

    public MovieWrapperView(@NonNull Context context) {
        super(context);
    }

    public MovieWrapperView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieWrapperView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
