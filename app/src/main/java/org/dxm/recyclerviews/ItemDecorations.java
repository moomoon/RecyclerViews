package org.dxm.recyclerviews;

import android.support.v7.widget.RecyclerView;

import org.dxm.recyclerviews.itemdecoration.GridSpacingBuilder;
import org.dxm.recyclerviews.itemdecoration.LinearMarginBuilder;
import org.dxm.recyclerviews.itemdecoration.MarginBuilder;

/**
 * Created by phoebe on 9/18/16.
 */

public class ItemDecorations {

    public static MarginBuilder marginBuilder() { return new MarginBuilder(); }
    public static RecyclerView.ItemDecoration margin(int margin) {
        return marginBuilder().left(margin).top(margin).right(margin).bottom(margin).build();
    }
    public static GridSpacingBuilder gridSpacingBuilder() {
        return new GridSpacingBuilder();
    }
    public static LinearMarginBuilder linearSpacingBuilder() {
        return new LinearMarginBuilder();
    }

}
