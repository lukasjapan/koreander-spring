package de.cvguy.kotlin.koreander.spring

import de.cvguy.kotlin.koreander.Koreander
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import java.util.*

class KoreanderViewResolver(
        val prefix: String = "/templates/",
        val suffix: String = ".kor"
) : ViewResolver {
    val engine = Koreander()

    override fun resolveViewName(viewName: String?, locale: Locale?): View {
        viewName ?: throw AssertionError()

        val resourceName = "$prefix$viewName$suffix"

        val resource = javaClass.getResource(resourceName)

        resource ?: throw KoreanderViewNotFoundException(viewName, prefix, suffix)

        return KoreanderView(engine, resource)
    }
}

inline fun <reified T : Any>ModelAndView(template: String, o:  T) = ModelAndView("meh", mapOf("a" to 23))