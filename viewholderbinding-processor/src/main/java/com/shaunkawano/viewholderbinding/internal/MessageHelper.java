package com.shaunkawano.viewholderbinding.internal;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

import static java.util.Locale.US;
import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

public final class MessageHelper {

  private Messager messager;

  public MessageHelper(Messager messager) {
    this.messager = messager;
  }

  public void note(String message, Object... args) {
    note(String.format(US, message, args));
  }

  public void note(String message) {
    print(NOTE, message);
  }

  public void e(Throwable e) {
    e(e.toString());
  }

  public void e(String message, Object... args) {
    e(String.format(US, message, args));
  }

  public void e(String message) {
    print(ERROR, message);
  }

  private void print(Diagnostic.Kind kind, String message) {
    messager.printMessage(kind, message);
  }
}
