package de.cvguy.kotlin.koreander.spring

import de.cvguy.kotlin.koreander.Koreander
import org.springframework.ui.Model
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

inline fun <reified T : Any>Model.setKoreanderContext(context:  T) {
    addAttribute("context", context)

    // add type only if necessary for performance
    if(context::class.typeParameters.isNotEmpty()) {
        addAttribute("type", Koreander.typeOf(context))
    }
}