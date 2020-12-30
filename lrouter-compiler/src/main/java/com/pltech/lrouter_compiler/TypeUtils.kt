package com.pltech.lrouter_compiler

import com.pltech.lrouter_annotation.TypeKind
import com.pltech.lrouter_compiler.Constants.BOOLEAN
import com.pltech.lrouter_compiler.Constants.BYTE
import com.pltech.lrouter_compiler.Constants.CHAR
import com.pltech.lrouter_compiler.Constants.DOUBLE
import com.pltech.lrouter_compiler.Constants.FLOAT
import com.pltech.lrouter_compiler.Constants.INTEGER
import com.pltech.lrouter_compiler.Constants.LONG
import com.pltech.lrouter_compiler.Constants.PARCELABLE
import com.pltech.lrouter_compiler.Constants.SERIALIZABLE
import com.pltech.lrouter_compiler.Constants.SHORT
import com.pltech.lrouter_compiler.Constants.STRING
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * Utils for type exchange
 * Created by Pang Li on 2020/12/29
 */
class TypeUtils(private val types: Types, elements: Elements) {
    private val parcelableType: TypeMirror = elements.getTypeElement(PARCELABLE).asType()
    private val serializableType: TypeMirror = elements.getTypeElement(SERIALIZABLE).asType()

    /**
     * Diagnostics out the true java type
     *
     * @param element Raw type
     * @return Type class of java
     */
    fun typeExchange(element: Element): Int {
        val typeMirror = element.asType()

        // Primitive
        return if (typeMirror.kind.isPrimitive) {
            element.asType().kind.ordinal
        } else when (typeMirror.toString()) {
            BYTE -> TypeKind.BYTE.ordinal
            SHORT -> TypeKind.SHORT.ordinal
            INTEGER -> TypeKind.INT.ordinal
            LONG -> TypeKind.LONG.ordinal
            FLOAT -> TypeKind.FLOAT.ordinal
            DOUBLE -> TypeKind.DOUBLE.ordinal
            BOOLEAN -> TypeKind.BOOLEAN.ordinal
            CHAR -> TypeKind.CHAR.ordinal
            STRING -> TypeKind.STRING.ordinal
            else ->  // Other side, maybe the PARCELABLE or SERIALIZABLE or OBJECT.
                when {
                    types.isSubtype(typeMirror, parcelableType) -> {
                        // PARCELABLE
                        TypeKind.PARCELABLE.ordinal
                    }
                    types.isSubtype(typeMirror, serializableType) -> {
                        // SERIALIZABLE
                        TypeKind.SERIALIZABLE.ordinal
                    }
                    else -> {
                        TypeKind.OBJECT.ordinal
                    }
                }
        }
    }

}