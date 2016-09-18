package org.dxm.recyclerviews;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ants on 9/18/16.
 */

public class ItemDecorations {
    private static class MarginDecoration extends RecyclerView.ItemDecoration {
        @NonNull final Rect rect;

        private MarginDecoration(@NonNull Rect rect) {
            this.rect = rect;
        }

        @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(rect);
        }
    }

    public static MarginBuilder marginBuilder() {
        return new MarginBuilder();
    }

    public static RecyclerView.ItemDecoration margin(int margin) {
        return marginBuilder().left(margin).top(margin).right(margin).bottom(margin).build();
    }

    public static class MarginBuilder {
        private final Rect rect = new Rect();
        public MarginBuilder left(int left) {
            rect.left = left;
            return this;
        }
        public MarginBuilder top(int top) {
            rect.top = top;
            return this;
        }

        public MarginBuilder right(int right) {
            rect.right = right;
            return this;
        }

        public MarginBuilder bottom(int bottom) {
            rect.bottom = bottom;
            return this;
        }

        public RecyclerView.ItemDecoration build() {
            return new MarginDecoration(rect);
        }
    }
}
