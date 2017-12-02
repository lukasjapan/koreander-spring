package de.cvguy.kotlin.koreander.spring

import de.cvguy.kotlin.koreander.Koreander
import org.springframework.web.servlet.View
import java.net.URL
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class KoreanderView(
        val engine: Koreander,
        val url: URL
): View {
    override fun getContentType(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun render(model: MutableMap<String, *>?, request: HttpServletRequest?, response: HttpServletResponse?) {
        response?.writer?.print(engine.render(url.openStream(), Unit))
    }
}