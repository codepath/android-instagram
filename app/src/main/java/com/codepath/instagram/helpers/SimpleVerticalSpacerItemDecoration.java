package com.codepath.instagram.helpers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.instagram.core.MainApplication;

public class SimpleVerticalSpacerItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceInPx;

    public SimpleVerticalSpacerItemDecoration(int spaceInDp) {
        this.spaceInPx = Math.round(DeviceDimensionsHelper.convertDpToPixel(
                spaceInDp, MainApplication.sharedApplication().getApplicationContext()));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = spaceInPx;
    }
}
