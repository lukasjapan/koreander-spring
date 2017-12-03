package de.cvguy.kotlin.koreander.spring.exception

import de.cvguy.kotlin.koreander.exception.KoreanderException

class KoreanderModelException(
        key: String,
        message: String
) : KoreanderException("Error at key '$key' in model: $message")