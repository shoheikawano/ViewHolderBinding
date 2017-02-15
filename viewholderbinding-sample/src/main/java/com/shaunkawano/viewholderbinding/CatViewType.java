package com.shaunkawano.viewholderbinding;

import android.support.annotation.LayoutRes;

enum CatViewType {

  TITLE(R.layout.row_title),
  TARO(R.layout.row_taro),
  SHIRO(R.layout.row_shiro),
  LEONA(R.layout.row_leona);

  private @LayoutRes int layoutResId;

  CatViewType(@LayoutRes int layoutResId) {
    this.layoutResId = layoutResId;
  }

  public @LayoutRes int getLayoutResId() {
    return layoutResId;
  }

  public static CatViewType of(int position) {
    CatViewType[] values = CatViewType.values();
    for (int i = 0, length = values.length; i < length; i++) {
      CatViewType candidate = values[i];
      if (position == candidate.ordinal()) {
        return candidate;
      }
    }

    throw new IllegalArgumentException("Invalid position.");
  }

}
