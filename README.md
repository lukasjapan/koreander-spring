# Koreander Spring Integration

TBA

## Example

```kotlin
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
```

```
%html
    %head
        %link href=https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css rel=stylesheet itegrity=sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u crossorigin=anonymous
    %body
        %h1 Koreander - Current Context Example
        %p $this
```

## Todo

- tests