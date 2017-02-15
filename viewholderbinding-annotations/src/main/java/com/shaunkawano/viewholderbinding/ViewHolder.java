package com.shaunkawano.viewholderbinding;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Target(TYPE) @Retention(SOURCE) public @interface ViewHolder {

  int layout();

  boolean attachToRoot() default false;
}
