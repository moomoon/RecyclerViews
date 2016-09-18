package org.dxm.recyclerviews;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.dxm.recyclerviews.itemdecoration.GridSpacingBuilder;
import org.dxm.recyclerviews.itemdecoration.LinearMarginBuilder;
import org.dxm.recyclerviews.itemdecoration.MarginBuilder;

/**
 * Created by ants on 9/18/16.
 */

public class ItemDecorations {

    public static MarginBuilder marginBuilder() { return new MarginBuilder(); }
    public static RecyclerView.ItemDecoration margin(int margin) {
        return marginBuilder().left(margin).top(margin).right(margin).bottom(margin).build();
    }
    public static GridSpacingBuilder gridSpacingBuilder(@NonNull GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        return new GridSpacingBuilder(spanSizeLookup);
    }

    public static LinearMarginBuilder linearSpacingBuilder(@NonNull RecyclerView.Adapter adapter) {
        return new LinearMarginBuilder(adapter);
    }

}
