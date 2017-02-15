package com.shaunkawano.viewholderbinding.internal;

import com.squareup.javapoet.ClassName;

/**
 * @author Shohei Kawano
 */
public final class ClassResolver {

  private ClassResolver() {
  }

  public static ClassName getViewClass() {
    return getClass("android.view", "View");
  }

  public static ClassName getLayoutInflaterClass() {
    return getClass("android.view", "LayoutInflater");
  }

  public static ClassName getViewGroupClass() {
    return getClass("android.view", "ViewGroup");
  }

  public static ClassName getViewHolderClass() {
    return getClass("android.support.v7.widget.RecyclerView", "ViewHolder");
  }

  public static ClassName getAdapterClass() {
    return getClass("android.support.v7.widget.RecyclerView", "Adapter");
  }

  public static ClassName getLocaleClass() {
    return getClass("java.util", "Locale");
  }

  private static ClassName getClass(String packageName, String simpleName) {
    return ClassName.get(packageName, simpleName);
  }
}
