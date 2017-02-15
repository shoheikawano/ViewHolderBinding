package com.shaunkawano.viewholderbinding;

import com.google.auto.service.AutoService;
import com.shaunkawano.viewholderbinding.internal.ClassResolver;
import com.shaunkawano.viewholderbinding.internal.MessageHelper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

import static com.squareup.javapoet.TypeName.INT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author Shohei Kawano
 */
@AutoService(Processor.class) public class ViewHolderBindingProcessor extends AbstractProcessor {

  private static final String SUFFIX = "ViewHolderBinding";

  private Elements elements;
  private MessageHelper messenger;
  private Filer filer;

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(AdapterBinding.class.getCanonicalName());
  }

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    elements = processingEnv.getElementUtils();
    messenger = new MessageHelper(processingEnv.getMessager());
    filer = processingEnv.getFiler();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (Element adapter : roundEnv.getElementsAnnotatedWith(AdapterBinding.class)) {
      if (adapter.getKind() != ElementKind.CLASS) {
        messenger.e("%s is not a class; @AdapterBinding can only be annotated to classes.",
            adapter.getSimpleName());
        return true;
      }

      MethodSpec.Builder createMethodBuilder = MethodSpec.methodBuilder("createViewHolder")
          .addModifiers(PUBLIC, STATIC)
          .addParameter(ClassResolver.getLayoutInflaterClass(), "inflater")
          .addParameter(ClassResolver.getViewGroupClass(), "parent")
          .addParameter(INT, "viewType")
          .returns(ClassResolver.getViewHolderClass());
      createMethodBuilder.addCode("switch (viewType) {\n");

      MethodSpec.Builder bindMethodBuilder = MethodSpec.methodBuilder("bindViewHolder")
          .addModifiers(PUBLIC, STATIC)
          .addParameter(ClassResolver.getAdapterClass(), "adapter")
          .addParameter(ClassResolver.getViewHolderClass(), "holder")
          .addParameter(INT, "position")
          .addStatement("$T viewType = adapter.getItemViewType(position)", INT)
          .addCode("switch (viewType) {\n");

      for (Element adapterElements : adapter.getEnclosedElements()) {
        ViewHolder viewHolder = adapterElements.getAnnotation(ViewHolder.class);
        if (viewHolder == null) {
          continue;
        }

        if (adapterElements.getKind() != ElementKind.CLASS) {
          messenger.e("%s is not a class; @ViewHolder can only be annotated to classes.",
              adapterElements.getSimpleName());
          return true;
        }

        if (adapterElements.getModifiers().contains(PRIVATE)) {
          messenger.e("%s class must NOT be private scoped.", adapterElements.getSimpleName());
          return true;
        }

        String holdingViewName = adapterElements.getSimpleName() + "View";
        createMethodBuilder.addCode("$>case $L:$<\n", viewHolder.layout())
            .addStatement("$>$>$T $L = inflater.inflate($L, parent, $L$<$<)",
                ClassResolver.getViewClass(), holdingViewName, viewHolder.layout(),
                viewHolder.attachToRoot())
            .addStatement("$>$>return new $T($L)$<$<", adapterElements, holdingViewName);

        // If @ViewHolder annotation exists that means @OnBind may also exist.
        for (Element bindMethod : adapterElements.getEnclosedElements()) {
          OnBind onBind = bindMethod.getAnnotation(OnBind.class);
          if (onBind == null) {
            continue;
          }

          if (bindMethod.getModifiers().contains(PRIVATE)) {
            messenger.e("%s method must NOT be private scoped.", bindMethod.getSimpleName());
            return true;
          }

          bindMethodBuilder.addCode("$>case $L:$<\n", viewHolder.layout());

          List<? extends VariableElement> parameters =
              ((ExecutableElement) bindMethod).getParameters();
          if (parameters == null || parameters.isEmpty()) {
            bindMethodBuilder.addStatement("$>$>(($T) holder).$N()", adapterElements,
                bindMethod.getSimpleName());
          } else {
            bindMethodBuilder.addStatement("$>$>(($T) holder).$N(position)", adapterElements,
                bindMethod.getSimpleName());
          }

          bindMethodBuilder.addStatement("break$<$<");
        }
      }

      createMethodBuilder.addCode("$>default:$<\n")
          .addStatement("$>$>" + "throw new IllegalArgumentException("
                  + "String.format($T.US, \"Illegal viewType(%d) passed to $L.\", viewType))$<$<",
              ClassResolver.getLocaleClass(), adapter.getSimpleName())
          .addCode("}\n");

      bindMethodBuilder.addCode("$>default:$<\n")
          .addStatement("$>$>" + "throw new IllegalArgumentException("
                  + "String.format($T.US, \"Illegal viewType(%d) passed to $L.\", viewType))$<$<",
              ClassResolver.getLocaleClass(), adapter.getSimpleName())
          .addCode("}\n");

      TypeSpec.Builder bindingClassBuilder =
          TypeSpec.classBuilder(adapter.getSimpleName().toString() + SUFFIX)
              .addMethod(createMethodBuilder.build())
              .addMethod(bindMethodBuilder.build());
      try {
        String packageName = elements.getPackageOf(adapter).getQualifiedName().toString();
        TypeSpec bindingClass = bindingClassBuilder.build();
        JavaFile.builder(packageName, bindingClass).build().writeTo(filer);
      } catch (IOException e) {
        messenger.e(e);
      }
    }

    return false;
  }
}
