package com.androidcomp.app.domain.model

enum class CodeLanguage {
    COMPOSE,
    XML
}

data class CodeSample(
    val language: CodeLanguage,
    val code: String
)
