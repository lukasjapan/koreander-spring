package de.cvguy.kotlin.koreander.spring

import de.cvguy.kotlin.koreander.Koreander
import org.springframework.web.servlet.ModelAndView

inline fun <reified T : Any>ModelAndView(template: String, context:  T): ModelAndView {
    val model = mutableMapOf<String, Any?>(
            "context" to context
    )

    // add type only if necessary for performance
    if(context::class.typeParameters.isNotEmpty()) {
        model["type"] = Koreander.typeOf(context)
    }

    return ModelAndView(template, model)
}