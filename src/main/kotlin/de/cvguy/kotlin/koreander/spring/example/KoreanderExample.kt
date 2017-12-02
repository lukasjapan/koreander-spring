package de.cvguy.kotlin.koreander.spring.example

import de.cvguy.kotlin.koreander.spring.KoreanderViewResolver
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ViewResolver

@Controller
class HelloController {
    @GetMapping("/")
    fun hello() = "example"
}

@SpringBootApplication
class KoreanderExample {
    @Bean fun viewResolver(): ViewResolver = KoreanderViewResolver()
}

fun main(args: Array<String>) {
    SpringApplication.run(KoreanderExample::class.java, *args)
}