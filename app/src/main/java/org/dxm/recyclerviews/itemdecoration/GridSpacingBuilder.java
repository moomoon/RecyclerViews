package org.dxm.recyclerviews.itemdecoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ants on 9/18/16.
 */

public class GridSpacingBuilder {
    @NonNull private final GridLayoutManager.SpanSizeLookup spanSizeLookup;
    private int spanCount = 1;
    private int interItem = 0;
    private int interLine = 0;

    public GridSpacingBuilder(@NonNull GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup;
    }

    public GridSpacingBuilder spanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public GridSpacingBuilder itemSpacing(int interItem) {
        this.interItem = interItem;
        return this;
    }

    public GridSpacingBuilder lineSpacing(int interLine) {
        this.interLine = interLine;
        return this;
    }

    public RecyclerView.ItemDecoration build() {
        return new GridSpacingDecoration(this);
    }

    private static class GridSpacingDecoration extends RecyclerView.ItemDecoration {
        @NonNull private final GridLayoutManager.SpanSizeLookup spanSizeLookup;
        private final int spanCount;
        private final int interItem;
        private final int interLine;

        private GridSpacingDecoration(GridSpacingBuilder builder) {
            this.spanSizeLookup = builder.spanSizeLookup;
            this.spanCount = builder.spanCount;
            this.interItem = builder.interItem;
            this.interLine = builder.interLine;
        }

        @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
            int adapterPos = lp.getViewAdapterPosition();
            int spanIndex = spanSizeLookup.getSpanIndex(adapterPos, spanCount);
            int spanEndIndex = spanIndex + spanSizeLookup.getSpanSize(adapterPos);
            int spanGroupIndex = spanSizeLookup.getSpanGroupIndex(adapterPos, spanCount);
            boolean left = spanIndex != 0;
            boolean top = spanGroupIndex != 0;
            boolean right = spanEndIndex < spanCount - 1;
            outRect.set(left ? interItem : 0, top ? interLine : 0, right ? interItem : 0, 0);
        }
    }

}

