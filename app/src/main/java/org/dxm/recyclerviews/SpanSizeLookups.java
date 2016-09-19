package org.dxm.recyclerviews;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import org.dxm.recyclerviews.spansizelookup.ViewTypeBuilder;

/**
 * Created by Phoebe on 9/14/16.
 */

public class SpanSizeLookups {

  @NonNull public static ViewTypeBuilder viewTypeBuilder(@NonNull RecyclerView.Adapter adapter) {
    return new ViewTypeBuilder(adapter);
  }
}
