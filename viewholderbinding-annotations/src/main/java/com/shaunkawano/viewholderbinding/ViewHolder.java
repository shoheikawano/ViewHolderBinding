package com.shaunkawano.viewholderbinding;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author Shohei Kawano
 */
@Target(TYPE) @Retention(SOURCE) public @interface ViewHolder {

  int layout();

  boolean attachToRoot() default false;
}
