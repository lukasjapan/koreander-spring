package de.cvguy.kotlin.koreander.spring

class KoreanderViewNotFoundException(
        viewName: String,
        prefix: String,
        suffix: String
) : Exception("View not found: '$viewName'. Expected resource at path '$prefix$viewName$suffix'")