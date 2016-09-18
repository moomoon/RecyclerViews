package org.dxm.recyclerviews.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ants on 9/18/16.
 */

public class LinearMarginBuilder {
    private int interItem = 0;

    public LinearMarginBuilder itemSpacing(int interItem) {
        this.interItem = interItem;
        return this;
    }

    public RecyclerView.ItemDecoration build() {
        return new LinearMarginDecoration(this);
    }

    private static class LinearMarginDecoration extends RecyclerView.ItemDecoration {
        private final int interItem;

        private LinearMarginDecoration(LinearMarginBuilder builder) {
            this.interItem = builder.interItem;
        }

        @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int adapterPos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            outRect.left = adapterPos == 0 ? 0 : interItem;
            outRect.top = 0;
            outRect.right = 0;
            outRect.bottom = 0;
        }
    }

}
