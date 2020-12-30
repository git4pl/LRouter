package com.pltech.lrouter_compiler

import com.google.auto.service.AutoService
import com.pltech.lrouter_annotation.Route
import com.pltech.lrouter_compiler.Constants.GENERATED_PACKAGE
import com.pltech.lrouter_compiler.Constants.METHOD_REGISTER
import com.pltech.lrouter_compiler.Constants.NO_MODULE_NAME_TIPS
import com.pltech.lrouter_compiler.Constants.PACKAGE_NAME
import com.pltech.lrouter_compiler.Constants.WARNING_TIPS
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@AutoService(Processor::class) //将注解注册给系统jvm
@SupportedAnnotationTypes("com.pltech.lrouter_annotation.Route")
class RouteProcessor1 : AbstractProcessor() {

    var filer: Filer? = null //生成Java文件的对象
    var logger: Logger? = null
    var types: Types? = null
    var elementUtils: Elements? = null
    var typeUtils: TypeUtils? = null

    // Module name, maybe its 'app' or others
//    var moduleName: String? = null

    val LRouterClass = ClassName.get(PACKAGE_NAME, "LRouter")

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        filer = processingEnv!!.filer
        types = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        typeUtils = TypeUtils(types!!, elementUtils!!)
        logger = Logger(processingEnv.messager)

        println("RouteProcessor init() executed.")
        // Attempt to get user configuration [moduleName]
//        val options = processingEnv.options
//        if (!Utils.isMapEmpty(options)) {
//            moduleName = options["KEY_MODULE_NAME"]
//        }
//
//        if (!Utils.isStrEmpty(moduleName)) {
//            moduleName = moduleName!!.replace("[^0-9a-zA-Z_]+".toRegex(), "")
//            logger!!.info("The user has configuration the module name, it was [$moduleName]")
//        } else {
//            logger!!.error(NO_MODULE_NAME_TIPS)
//            throw RuntimeException("ARouter::Compiler >>> No module name, for more information, look at gradle log.")
//        }
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val types = HashSet<String>()
        types.add(Route::class.java.canonicalName)
        return types
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (Utils.isCollectionEmpty(annotations)) {
            return false
        }

        val routeElements = roundEnv!!.getElementsAnnotatedWith(Route::class.java)
        try {
            //logger!!.info(">>> Found routes, start... <<<\n")
            this.parseRoutes(routeElements)
        } catch (e: Exception) {
            logger!!.error(e)
        }
        return true
    }

    private fun parseRoutes(routeElements: MutableSet<out Element>) {
        //val hashMap = HashMap<String, ClassName>()
        for (element in routeElements) {
            val typeElement = element as TypeElement
            val routePath = typeElement.getAnnotation(Route::class.java).path
            //hashMap[routePath] = ClassName.get(typeElement)
            val typeIroute = elementUtils?.getTypeElement(Constants.IROUTE_PACKAGE)
            val filename = routePath.replace("/", "_") + "_${typeElement.simpleName}"
            println("the filename is: $filename")
            val typeHelper = TypeSpec.classBuilder(filename.substring(1))
                    .addJavadoc(WARNING_TIPS)
                    .addSuperinterface(ClassName.get(typeIroute))
                    .addModifiers(Modifier.PUBLIC)
            val methodSpec = MethodSpec.methodBuilder(METHOD_REGISTER)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
            methodSpec.addStatement("$LRouterClass.INSTANCE.addRoutePath(\"$routePath\", ${ClassName.get(typeElement)}.class)")
            typeHelper.addMethod(methodSpec.build())
            JavaFile.builder(GENERATED_PACKAGE, typeHelper.build()).build().writeTo(filer)
        }
        println("RouteProcessor parseRoutes() executed.")
    }
}