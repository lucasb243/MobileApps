package com.example.mobileapps;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

class MarginItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceHeight;

    public MarginItemDecoration(int aSpaceHeight) {
        spaceHeight = aSpaceHeight;
    }

    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = spaceHeight;
            }
            outRect.left = spaceHeight;
            outRect.right = spaceHeight;
            outRect.bottom = spaceHeight;
        }

}
