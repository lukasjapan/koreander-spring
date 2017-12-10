package de.cvguy.kotlin.koreander.spring

import de.cvguy.kotlin.koreander.CompiledTemplate
import de.cvguy.kotlin.koreander.Koreander
import de.cvguy.kotlin.koreander.spring.exception.KoreanderViewNotFoundException
import org.springframework.web.servlet.View
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.reflect.KType
import org.springframework.web.servlet.view.AbstractCachingViewResolver
import java.util.Collections.synchronizedMap
import kotlin.collections.LinkedHashMap

typealias CompiledTemplateCache = LinkedHashMap<Triple<URL, KType, Charset>, Lazy<CompiledTemplate>>
typealias CompiledTemplateCacheEntry = Map.Entry<Triple<URL, KType, Charset>, Lazy<CompiledTemplate>>

data class KoreanderViewResolverConfiguration(
    val prefix: String = "/templates/",
    val suffix: String = ".kor",
    val defaultCharset: Charset = StandardCharsets.UTF_8,
    val cache: Boolean = true,
    val cacheLimit: Int = 1024
)

class KoreanderViewResolver(
        val config: KoreanderViewResolverConfiguration = KoreanderViewResolverConfiguration()
) : AbstractCachingViewResolver() {
    val engine = Koreander()

    private val compiledTemplateCache = run {
        val map = if (config.cache) {
            assert(config.cacheLimit > 0, { "invalid cacheLimit" })

            // limit number of entries to cacheLimit
            // https://stackoverflow.com/questions/41184893/delete-oldest-objects-from-hashmap-to-reach-certain-size
            object : CompiledTemplateCache(config.cacheLimit + 1, .75f, false) {
                override fun removeEldestEntry(eldest: CompiledTemplateCacheEntry) = size > config.cacheLimit
            }
        } else {
            CompiledTemplateCache()
        }

        // for thread safety
        synchronizedMap(map)
    }

    init {
        // setup cache settings of AbstractCachingViewResolver
        cacheLimit = if(config.cache) config.cacheLimit else 0
    }

    override fun loadView(viewName: String, locale: Locale): View {
        if(logger.isDebugEnabled) {
            logger.debug("Resolving Koreander view. viewName=$viewName, locale=$locale")
        }

        val resourceName = "${config.prefix}$viewName${config.suffix}"

        val resource = javaClass.getResource(resourceName)

        resource ?: throw KoreanderViewNotFoundException(viewName, config.prefix, config.suffix)

        return KoreanderView(this, resource, config.defaultCharset, false)
    }

    fun compileViewResource(viewUrl: URL, type: KType, charset: Charset): CompiledTemplate {
        val compute = {
            if(logger.isDebugEnabled) {
                logger.debug("Compiling Koreander template. viewUrl=$viewUrl, type=$type, charset=$charset")
            }

            engine.compile(viewUrl, type, charset)
        }

        return if(config.cache) {
            logger.debug("Looking for cached Koreander template. viewUrl=$viewUrl, type=$type, charset=$charset")

            val key = Triple(viewUrl, type, charset)

            // The actual compilation is executed by kotlin lazy, so it won't block map access
            val lazyCompiledTemplate = compiledTemplateCache.computeIfAbsent(key) { lazy(compute) }

            return lazyCompiledTemplate.value
        }
        else {
            compute()
        }
    }
}