package com.savory.ui;

import android.content.Context;
import android.util.AttributeSet;

public class SquareSkeletonView extends SkeletonView {

    public SquareSkeletonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
