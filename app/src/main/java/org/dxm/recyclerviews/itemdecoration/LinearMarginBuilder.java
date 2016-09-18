package org.dxm.recyclerviews.itemdecoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ants on 9/18/16.
 */

public class LinearMarginBuilder {
    @NonNull private final RecyclerView.Adapter adapter;
    private int interItem = 0;

    public LinearMarginBuilder(@NonNull RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public LinearMarginBuilder itemSpacing(int interItem) {
        this.interItem = interItem;
        return this;
    }

    public RecyclerView.ItemDecoration build() {
        return new LinearMarginDecoration(this);
    }

    private static class LinearMarginDecoration extends RecyclerView.ItemDecoration {
        @NonNull private final RecyclerView.Adapter adapter;
        private final int interItem;

        private LinearMarginDecoration(LinearMarginBuilder builder) {
            this.adapter = builder.adapter;
            this.interItem = builder.interItem;
        }

        @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int adapterPos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            int itemCount = adapter.getItemCount();
            outRect.left = adapterPos == 0 ? 0 : interItem;
            outRect.top = 0;
            outRect.right = adapterPos == itemCount - 1 ? 0 : interItem;
            outRect.bottom = 0;
        }
    }

}
