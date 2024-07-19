package com.example.tuicodewars.data.model.user

import kotlin.reflect.full.memberProperties

data class Languages(
    val coffeescript: Coffeescript,
    val crystal: Crystal,
    val csharp: Csharp,
    val elixir: Elixir,
    val haskell: Haskell,
    val java: Java,
    val javascript: Javascript,
    val objc: Objc,
    val python: Python,
    val ruby: Ruby,
    val shell: Shell,
    val sql: Sql,
    val typescript: Typescript
){
    companion object {
        fun getLanguageNames(languages: Languages): List<String> {
            return languages::class.memberProperties.map { it.name }
        }
    }
}