package com.pltech.lrouter_compiler;

import com.google.auto.service.AutoService;
import com.pltech.lrouter_annotation.Route;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.pltech.lrouter_compiler.Constants.GENERATED_PACKAGE;
import static com.pltech.lrouter_compiler.Constants.METHOD_REGISTER;
import static com.pltech.lrouter_compiler.Constants.PACKAGE_NAME;
import static com.pltech.lrouter_compiler.Constants.WARNING_TIPS;

/**
 * Created by Pang Li on 2020/12/30
 */
@AutoService(Processor.class) //将注解注册给系统jvm
@SupportedAnnotationTypes("com.pltech.lrouter_annotation.Route")
public class RouteProcessor extends AbstractProcessor {
    Filer filer = null; //生成Java文件的对象
    Logger logger = null;
    Types types = null;
    Elements elementUtils = null;
    TypeUtils typeUtils = null;
    ClassName LRouterClass = ClassName.get(PACKAGE_NAME, "LRouter");

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        typeUtils = new TypeUtils(types, elementUtils);
        logger = new Logger(processingEnv.getMessager());
        System.out.println("RouteProcessor init() executed.");
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(Route.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (Utils.isCollectionEmpty(annotations)) {
            return false;
        }
        Set<? extends Element> routeElements = roundEnv.getElementsAnnotatedWith(Route.class);
        for (Element element :
                routeElements) {
            TypeElement typeElement = (TypeElement) element;
            String routePath = typeElement.getAnnotation(Route.class).path();
            TypeElement typeIroute = elementUtils.getTypeElement(Constants.IROUTE_PACKAGE);
            String filename = routePath.replace("/", "_") + "_" + typeElement.getSimpleName();
            System.out.println("the filename is: " + filename);
            TypeSpec.Builder typeHelper = TypeSpec.classBuilder(filename.substring(1))
                    .addJavadoc(WARNING_TIPS)
                    .addSuperinterface(ClassName.get(typeIroute))
                    .addModifiers(Modifier.PUBLIC);

            MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(METHOD_REGISTER)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class);
            methodSpec.addStatement("$T.INSTANCE.addRoutePath($S, $T.class)", LRouterClass, routePath, ClassName.get(typeElement));
            typeHelper.addMethod(methodSpec.build());

            try {
                JavaFile.builder(GENERATED_PACKAGE, typeHelper.build()).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
