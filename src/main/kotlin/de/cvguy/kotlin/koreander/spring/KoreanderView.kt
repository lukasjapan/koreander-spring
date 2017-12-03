package de.cvguy.kotlin.koreander.spring

import de.cvguy.kotlin.koreander.spring.exception.KoreanderModelException
import org.springframework.web.servlet.View
import java.net.URL
import java.nio.charset.Charset
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.starProjectedType

class KoreanderView(
        val resolver: KoreanderViewResolver,
        val url: URL,
        val charset: Charset,
        val preCompiled: Boolean
): View {
    override fun getContentType(): String {
        return "text/html"
    }

    override fun render(model: MutableMap<String, *>, request: HttpServletRequest, response: HttpServletResponse) {
        val context: Any = model["context"] ?: Unit

        var type: Any? = model["type"]

        if(type == null) {
            if(context::class.typeParameters.isNotEmpty()) {
                throw KoreanderModelException("type", "Key is required for a context with type parameters. Got a '${context::class.starProjectedType}'.")
            }

            type = context::class.createType()
        }

        val ktype = type as? KType

        ktype ?: throw KoreanderModelException("type", "Key must be a KType instance. Got a '${type::class.starProjectedType}'.")

        val output = if(preCompiled) {
            TODO("unserialize view from resource and do a (safe) render")
        }
        else {
            val compiledTemplate = resolver.compileViewResource(url, ktype, charset)

            resolver.engine.unsafeRender(compiledTemplate, context)
        }

        response.writer.print(output)
    }
}