package de.cvguy.kotlin.koreander.spring.example

import de.cvguy.kotlin.koreander.spring.KoreanderViewResolver
import de.cvguy.kotlin.koreander.spring.ModelAndView
import de.cvguy.kotlin.koreander.spring.setKoreanderContext
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.ViewResolver
import java.time.ZonedDateTime

@Controller
class HelloController {
    @GetMapping("/")
    fun nothing(): String {
        return "example"
    }

    @GetMapping("/modelandview")
    fun modelAndView(): ModelAndView {
        return ModelAndView("example", ZonedDateTime.now())
    }

    @GetMapping("/model")
    fun model(model: Model): String {
        model.setKoreanderContext(ZonedDateTime.now())
        return "example"
    }
}

@SpringBootApplication
class KoreanderExample {
    @Bean fun koreanderViewResolver(): ViewResolver = KoreanderViewResolver()
}

fun main(args: Array<String>) {
    SpringApplication.run(KoreanderExample::class.java, *args)
}