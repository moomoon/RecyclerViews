package org.dxm.recyclerviews;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.support.v4.animation.AnimatorCompatHelper.clearInterpolator;

/**
 * Created by Phoebe on 9/15/16.
 */


public class InsertAnimator extends DefaultItemAnimator {
  private final static boolean DEBUG = false;
  private final ArrayList<AdditionInfo> pendingAdditions = new ArrayList<>();
  private final ArrayList<ArrayList<AdditionInfo>> additionsList = new ArrayList<>();
  private final ArrayList<RecyclerView.ViewHolder> addAnimations = new ArrayList<>();

  private PointF nextInsertionAnchor = null;
  private Integer nextInsertionPosition = null;


  private static class AdditionInfo {
    @NonNull private final RecyclerView.ViewHolder holder;
    @NonNull private final PointF target;

    private AdditionInfo(@NonNull RecyclerView.ViewHolder holder, @NonNull PointF target) {
      this.holder = holder;
      this.target = target;
    }
  }

  public void insert(int position, PointF anchor, View anchorView, RecyclerView recyclerView) {
    nextInsertionAnchor = translate(anchor, anchorView, recyclerView);
    nextInsertionPosition = position;
  }


  @Override public boolean animateAdd(RecyclerView.ViewHolder holder) {
    final PointF anchor = nextInsertionAnchor;
    if (null == anchor || !isInsertion(holder)) return super.animateAdd(holder);
    final PointF target = new PointF(ViewCompat.getX(holder.itemView), ViewCompat.getY(holder.itemView));
    clearInterpolator(holder.itemView);
    ViewCompat.setX(holder.itemView, anchor.x);
    ViewCompat.setY(holder.itemView, anchor.y);
    endAnimation(holder);
    pendingAdditions.add(new AdditionInfo(holder, target));
    nextInsertionAnchor = null;
    nextInsertionPosition = null;
    return true;
  }

  public boolean isInsertion(RecyclerView.ViewHolder holder) {
    return null != nextInsertionPosition && holder.getAdapterPosition() == nextInsertionPosition;
  }

  @Override public void runPendingAnimations() {
    super.runPendingAnimations();
    if (pendingAdditions.isEmpty()) return;
    final ArrayList<AdditionInfo> additions = new ArrayList<>();
    additions.addAll(pendingAdditions);
    additionsList.add(additions);
    pendingAdditions.clear();
    Runnable adder = new Runnable() {
      @Override
      public void run() {
        for (AdditionInfo info : additions) {
          runAdditionAnim(info);
        }
        additions.clear();
        additionsList.remove(additions);
      }
    };
    ViewCompat.postOnAnimation(additions.get(0).holder.itemView, adder);
  }

  private void runAdditionAnim(final AdditionInfo info) {
    final RecyclerView.ViewHolder holder = info.holder;
    final View view = holder.itemView;
    final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
    addAnimations.add(info.holder);
    animation
            .x(info.target.x)
            .y(info.target.y)
            .alpha(1)
            .setDuration((long) (getAddDuration() * Math.min(3F, distance(view.getX(), view.getY(), info.target.x, info.target.y) / view.getWidth()))).
            setListener(new ViewPropertyAnimatorListener() {
              @Override
              public void onAnimationStart(View view) {
                dispatchAddStarting(holder);
              }

              @Override
              public void onAnimationCancel(View view) {
                ViewCompat.setAlpha(view, 1);
              }

              @Override
              public void onAnimationEnd(View view) {
                animation.setListener(null);
                dispatchAddFinished(holder);
                addAnimations.remove(holder);
                dispatchFinishedWhenDone();
              }
            }).start();

  }

  private static double distance(double x0, double y0, double x1, double y1) {
    final double dx = x0 - x1;
    final double dy = y0 - y1;
    return Math.sqrt(dx * dx + dy * dy);
  }

  @Override
  public boolean isRunning() {
    return super.isRunning() ||
            (!pendingAdditions.isEmpty() ||
                    !addAnimations.isEmpty() ||
                    !additionsList.isEmpty());
  }

  @Override
  public void endAnimation(RecyclerView.ViewHolder item) {
    super.endAnimation(item);
    final View view = item.itemView;
    if (removeAdditionInfo(pendingAdditions, item)) {
      ViewCompat.setAlpha(view, 1);
      dispatchAddFinished(item);
    }
    for (int i = additionsList.size() - 1; i >= 0; i--) {
      ArrayList<AdditionInfo> additions = additionsList.get(i);
      if (removeAdditionInfo(additions, item)) {
        ViewCompat.setAlpha(view, 1);
        ViewCompat.setTranslationX(view, 0);
        ViewCompat.setTranslationY(view, 0);
        dispatchAddFinished(item);
        if (additions.isEmpty()) {
          additionsList.remove(i);
        }
      }
    }
    //noinspection PointlessBooleanExpression,ConstantConditions
    if (addAnimations.remove(item) && DEBUG) {
      throw new IllegalStateException("after animation is cancelled, item should not be in "
              + "mAddAnimations list");
    }
    dispatchFinishedWhenDone();
  }

  private boolean removeAdditionInfo(Iterable<AdditionInfo> additions, RecyclerView.ViewHolder holder) {
    final Iterator<AdditionInfo> iterator = additions.iterator();
    while(iterator.hasNext()) {
      AdditionInfo next = iterator.next();
      if (next.holder.equals(holder)) {
        iterator.remove();
        return true;
      }
    }
    return false;
  }

  @Override
  public void endAnimations() {
    super.endAnimations();
    int count = pendingAdditions.size();
    for (int i = count - 1; i >= 0; i--) {
      AdditionInfo info = pendingAdditions.get(i);
      View view = info.holder.itemView;
      ViewCompat.setAlpha(view, 1);
      ViewCompat.setTranslationX(view, 0);
      ViewCompat.setTranslationY(view, 0);
      dispatchAddFinished(info.holder);
      pendingAdditions.remove(i);
    }
    if (!isRunning()) {
      return;
    }

    int listCount = additionsList.size();
    for (int i = listCount - 1; i >= 0; i--) {
      ArrayList<AdditionInfo> additions = additionsList.get(i);
      count = additions.size();
      for (int j = count - 1; j >= 0; j--) {
        RecyclerView.ViewHolder item = additions.get(j).holder;
        View view = item.itemView;
        ViewCompat.setAlpha(view, 1);
        dispatchAddFinished(item);
        additions.remove(j);
        if (additions.isEmpty()) {
          additionsList.remove(additions);
        }
      }
    }

    cancelAll(addAnimations);
    dispatchAnimationsFinished();
  }


  private void cancelAll(List<RecyclerView.ViewHolder> viewHolders) {
    for (int i = viewHolders.size() - 1; i >= 0; i--) {
      ViewCompat.animate(viewHolders.get(i).itemView).cancel();
    }
  }


  private void dispatchFinishedWhenDone() {
    if (!isRunning()) {
      dispatchAnimationsFinished();
    }
  }



  private static PointF translate(PointF point, View from, View to) {
    PointF translated = new PointF(point.x, point.y);
    int[] loc = new int[2];
    from.getLocationOnScreen(loc);
    translated.offset(loc[0], loc[1]);
    to.getLocationOnScreen(loc);
    translated.offset(-loc[0], -loc[1]);
    return translated;
  }
}