package org.dxm.recyclerviews.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by phoebe on 9/18/16.
 */

public class LinearMarginBuilder {
    private int interItem = 0;
    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;

    public LinearMarginBuilder left(int left) {
        this.left = left;
        return this;
    }

    public LinearMarginBuilder top(int top) {
        this.top = top;
        return this;
    }

    public LinearMarginBuilder right(int right) {
        this.right = right;
        return this;
    }

    public LinearMarginBuilder bottom(int bottom) {
        this.bottom = bottom;
        return this;
    }

    public LinearMarginBuilder itemSpacing(int interItem) {
        this.interItem = interItem;
        return this;
    }

    public RecyclerView.ItemDecoration build() {
        return new LinearMarginDecoration(this);
    }

    private static class LinearMarginDecoration extends RecyclerView.ItemDecoration {
        private final int interItem;
        private final int left, top, right, bottom;

        private LinearMarginDecoration(LinearMarginBuilder builder) {
            this.interItem = builder.interItem;
            this.left = builder.left;
            this.top = builder.top;
            this.right = builder.right;
            this.bottom = builder.bottom;
        }

        @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            LinearLayoutManager lm = (LinearLayoutManager) parent.getLayoutManager();
            int adapterPos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            int count = parent.getAdapter().getItemCount();
            if (lm.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                outRect.left = adapterPos == 0 ? left : interItem;
                outRect.top = top;
                outRect.right = adapterPos == count - 1 ? right : 0;
                outRect.bottom = bottom;
            } else {
                outRect.left = left;
                outRect.top = adapterPos == 0 ? top : interItem;
                outRect.right = right;
                outRect.bottom = adapterPos == count - 1 ? bottom : 0;
            }
        }
    }

}
