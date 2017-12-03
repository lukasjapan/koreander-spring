package de.cvguy.kotlin.koreander.spring.example

import de.cvguy.kotlin.koreander.spring.KoreanderViewResolver
import de.cvguy.kotlin.koreander.spring.KoreanderViewResolverConfiguration
import de.cvguy.kotlin.koreander.spring.ModelAndView
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.ViewResolver
import java.time.ZonedDateTime

@Controller
class HelloController {
    @GetMapping("/")
    fun hello(): ModelAndView {
        println("executed controller")
        return ModelAndView("example", ZonedDateTime.now())
    }
}

@SpringBootApplication
@EnableCaching
class KoreanderExample {

    @Bean
    fun koreanderViewResolver(a: ApplicationContext): ViewResolver = KoreanderViewResolver()
}

fun main(args: Array<String>) {
    SpringApplication.run(KoreanderExample::class.java, *args)
}