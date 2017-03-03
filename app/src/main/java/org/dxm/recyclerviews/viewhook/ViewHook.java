package org.dxm.recyclerviews.viewhook;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by phoebe on 03/03/2017.
 */

public abstract class ViewHook {
    private final Map<View, Hook> hooks = new WeakHashMap<>();
    abstract float computeProgress(@NonNull View view);

    @Nullable public View viewWithHighestProgressIn(@Nullable ViewGroup parent) {
        View hit = null;
        float progress = Float.MIN_VALUE;
        if (null == parent) {
            for (Hook hook : hooks.values()) {
                if (hook.progress > progress) {
                    progress = hook.progress;
                    hit = hook.view.get();
                }
            }
        } else {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                Hook hook = hooks.get(parent.getChildAt(i));
                if (null != hook && hook.progress > progress) {
                    progress = hook.progress;
                    hit = hook.view.get();
                }
            }

        }
        return hit;
    }

    public void update() {
        for (Hook hook: hooks.values()) hook.update();
    }

    public abstract class Hook {
        @NonNull private final WeakReference<View> view;
        private float progress = 0F;
        public Hook(@NonNull View view) {
            this.view = new WeakReference<>(view);
            view.post(new Runnable() {
                @Override public void run() {
                    onProgressChange(progress);
                }
            });
            hooks.put(view, this);
        }

        private void setProgress(float progress) {
            if (Math.abs(progress - this.progress) < 0.01F) return;
            this.progress = progress;
            onProgressChange(progress);
        }

        public void update() {
            View view = this.view.get();
            if (null == view) return;
            setProgress(computeProgress(view));
        }

        abstract void onProgressChange(float progress);
    }
}