package org.dxm.recyclerviews.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ants on 9/18/16.
 */

public class GridSpacingBuilder {
    private final static int NOT_SPECIFIED = -1;
    private int interItem;
    private int interLine;
    private int left, top, right = NOT_SPECIFIED, bottom = NOT_SPECIFIED;

    public GridSpacingBuilder itemSpacing(int interItem) {
        this.interItem = interItem;
        return this;
    }

    public GridSpacingBuilder lineSpacing(int interLine) {
        this.interLine = interLine;
        return this;
    }

    public GridSpacingBuilder left(int left) {
        this.left = left;
        return this;
    }

    public GridSpacingBuilder top(int top) {
        this.top = top;
        return this;
    }

    public GridSpacingBuilder right(int right) {
        this.right = right;
        return this;
    }

    public GridSpacingBuilder bottom(int bottom) {
        this.bottom = bottom;
        return this;
    }

    public RecyclerView.ItemDecoration build() {
        return new GridSpacingDecoration(this);
    }

    private static class GridSpacingDecoration extends RecyclerView.ItemDecoration {
        private final int interItem;
        private final int interLine;
        private final int left, top, right, bottom;

        private GridSpacingDecoration(GridSpacingBuilder builder) {
            this.interItem = builder.interItem;
            this.interLine = builder.interLine;
            this.left = builder.left;
            this.top = builder.top;
            this.right = builder.right;
            this.bottom = builder.bottom;
        }

        @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            GridLayoutManager lm = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = lm.getSpanSizeLookup();
            int spanCount = lm.getSpanCount();
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int adapterPos = lp.getViewAdapterPosition();
            int spanIndex = spanSizeLookup.getSpanIndex(adapterPos, spanCount);
            int spanEndIndex = spanIndex + spanSizeLookup.getSpanSize(adapterPos);
            int spanGroupIndex = spanSizeLookup.getSpanGroupIndex(adapterPos, spanCount);
            if (lm.getOrientation() == GridLayoutManager.VERTICAL) {
                outRect.left = spanIndex == 0 ? left : interItem;
                outRect.top = spanGroupIndex == 0 ? top : interLine;
                int right = this.right == NOT_SPECIFIED ? 0 : this.right;
                outRect.right = spanEndIndex == spanCount - 1 ? right : 0;
                if (bottom == NOT_SPECIFIED) {
                    outRect.bottom = 0;
                } else {
                    int itemCount = parent.getAdapter().getItemCount();
                    if (adapterPos < itemCount - spanCount) {
                        outRect.bottom = 0;
                    } else {
                        int lastSpanGroupIndex = spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount);
                        outRect.bottom = spanGroupIndex == lastSpanGroupIndex ? bottom : 0;
                    }
                }
            } else {
                outRect.left = spanGroupIndex == 0 ? left : interLine;
                outRect.top = spanIndex == 0 ? top : interItem;
                if (right == NOT_SPECIFIED) {
                    outRect.right = 0;
                } else {
                    int itemCount = parent.getAdapter().getItemCount();
                    if (adapterPos < itemCount - spanCount) {
                        outRect.right = 0;
                    } else {
                        int lastSpanGroupIndex = spanSizeLookup.getSpanGroupIndex(itemCount - 1, spanCount);
                        outRect.right = spanGroupIndex == lastSpanGroupIndex ? right : 0;
                    }
                }
                int bottom = this.bottom == NOT_SPECIFIED ? 0 : this.bottom;
                outRect.bottom = spanEndIndex == spanCount - 1 ? bottom : 0;
            }
        }
    }

}

