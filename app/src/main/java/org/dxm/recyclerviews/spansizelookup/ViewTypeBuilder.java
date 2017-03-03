package org.dxm.recyclerviews.spansizelookup;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phoebe on 9/19/16.
 */

public class ViewTypeBuilder {
    @NonNull private final RecyclerView.Adapter adapter;
    @NonNull private final List<Pair<Integer, Integer>> mappings = new ArrayList<>();
    @Nullable private Integer defaultSpanSize = null;
    private boolean cacheSpanIndices = true;

    public ViewTypeBuilder(@NonNull RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @NonNull public Entry map(int from) {
        return new Entry(this, from);
    }

    @NonNull public ViewTypeBuilder enableSpanIndexCache(boolean cacheSpanIndices) {
        this.cacheSpanIndices = cacheSpanIndices;
        return this;
    }

    @NonNull public ViewTypeBuilder defaultSpanSize(int defaultSpanSize) {
        this.defaultSpanSize = defaultSpanSize;
        return this;
    }

    @NonNull public GridLayoutManager.SpanSizeLookup build() {
        return new ViewTypeSpanSizeLookupImpl(this);
    }


    public static class Entry {
        @NonNull private final ViewTypeBuilder builder;
        private final int from;

        public Entry(@NonNull ViewTypeBuilder builder, int from) {
            this.builder = builder;
            this.from = from;
        }

        public ViewTypeBuilder to(int to) {
            builder.mappings.add(new Pair<>(from, to));
            return builder;
        }
    }

    private static class ViewTypeSpanSizeLookupImpl extends GridLayoutManager.SpanSizeLookup {
        private final SparseIntArray spansize;
        private final RecyclerView.Adapter adapter;
        private final int defaultSpanSize;
        private ViewTypeSpanSizeLookupImpl(@NonNull ViewTypeBuilder builder) {
            this.adapter = builder.adapter;
            this.spansize = new SparseIntArray(builder.mappings.size());
            for (Pair<Integer, Integer> mapping: builder.mappings) {
                spansize.put(mapping.first, mapping.second);
            }
            this.defaultSpanSize = builder.defaultSpanSize == null ? 1 : builder.defaultSpanSize;
            setSpanIndexCacheEnabled(builder.cacheSpanIndices);
        }

        @Override
        public int getSpanSize(int position) {
            return spansize.get(adapter.getItemViewType(position), defaultSpanSize);
        }
    }

}
