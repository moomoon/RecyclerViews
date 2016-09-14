package org.dxm.recyclerviews;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phoebe on 9/14/16.
 */

public class SpanSizeLookups {
  private static class SpanSizeLookupImpl extends GridLayoutManager.SpanSizeLookup {
    private final SparseIntArray spansize;
    private final int defaultSpanSize;
    private SpanSizeLookupImpl(@NonNull Builder builder) {
      this.spansize = new SparseIntArray(builder.mappings.size());
      for (Pair<Integer, Integer> mapping: builder.mappings) {
        spansize.put(mapping.first, mapping.second);
      }
      this.defaultSpanSize = builder.defaultSpanSize == null ? 1 : builder.defaultSpanSize;
      setSpanIndexCacheEnabled(builder.cacheSpanIndices);
    }

    @Override
    public int getSpanSize(int position) {
      return spansize.get(position, defaultSpanSize);
    }
  }

  @NonNull public static Builder builder() {
    return new Builder();
  }
  public static class Builder {
    @NonNull private final List<Pair<Integer, Integer>> mappings = new ArrayList<>();
    @Nullable private Integer defaultSpanSize = null;
    private boolean cacheSpanIndices = true;
    @NonNull public Entry map(int from) {
      return new Entry(this, from);
    }

    @NonNull public Builder enableSpanIndexCache(boolean cacheSpanIndices) {
      this.cacheSpanIndices = cacheSpanIndices;
      return this;
    }

    @NonNull public Builder defaultSpanSize(int defaultSpanSize) {
      this.defaultSpanSize = defaultSpanSize;
      return this;
    }

    @NonNull public GridLayoutManager.SpanSizeLookup build() {
      return new SpanSizeLookupImpl(this);
    }

    public static class Entry {
      @NonNull private final Builder builder;
      private final int from;

      public Entry(@NonNull Builder builder, int from) {
        this.builder = builder;
        this.from = from;
      }

      public Builder to(int to) {
        builder.mappings.add(new Pair<>(from, to));
        return builder;
      }
    }
  }
}
