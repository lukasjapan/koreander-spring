package de.cvguy.kotlin.koreander.spring

import de.cvguy.kotlin.koreander.CompiledTemplate
import de.cvguy.kotlin.koreander.Koreander
import de.cvguy.kotlin.koreander.spring.exception.KoreanderViewNotFoundException
import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KType
import org.slf4j.LoggerFactory

typealias CompiledTemplateCache = ConcurrentHashMap<Triple<URL, KType, Charset>, CompiledTemplate>
typealias ResolvedViewCache = ConcurrentHashMap<Pair<String, Locale>, KoreanderView>

data class KoreanderViewResolverConfiguration(
    val prefix: String = "/templates/",
    val suffix: String = ".kor",
    val defaultCharset: Charset = StandardCharsets.UTF_8,
    val cache: Boolean = true // Use javax.cache instead?
)

class KoreanderViewResolver(
        val config: KoreanderViewResolverConfiguration = KoreanderViewResolverConfiguration()
) : ViewResolver {
    val logger = LoggerFactory.getLogger(javaClass)

    val compiledTemplateCache = CompiledTemplateCache()
    val resolvedViewCache = ResolvedViewCache()
    val engine = Koreander()

    override fun resolveViewName(viewName: String, locale: Locale): View {
        val compute = {
            logger.debug("Resolving Koreander view. viewName={}, locale={}", viewName, locale)
            val resourceName = "${config.prefix}$viewName${config.suffix}"

            val resource = javaClass.getResource(resourceName)

            resource ?: throw KoreanderViewNotFoundException(viewName, config.prefix, config.suffix)

            KoreanderView(this, resource, config.defaultCharset, false)
        }

        return if(config.cache) {
            logger.debug("Looking for cached Koreander view. viewName={}, locale={}", viewName, locale)
            resolvedViewCache.computeIfAbsent(Pair(viewName, locale)) { compute() }
        }
        else {
            compute()
        }
    }

    fun compileViewResource(viewUrl: URL, type: KType, charset: Charset): CompiledTemplate {
        val compute = {
            logger.debug("Compiling Koreander template. viewUrl={}, type={}, charset={}", viewUrl, type, charset)

            engine.compile(viewUrl, type, charset)
        }

        return if(config.cache) {
            logger.debug("Looking for cached Koreander template. viewUrl={}, type={}, charset={}", viewUrl, type, charset)
            compiledTemplateCache.computeIfAbsent(Triple(viewUrl, type, charset)) { compute() }
        }
        else {
            compute()
        }
    }
}