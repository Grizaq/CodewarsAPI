package com.example.tuicodewars.data.model.challenge

data class Challenge(
    val description: String,
    val languages: List<String>,
    val name: String,
    val totalAttempts: Int,
    val totalCompleted: Int,
    val url: String
) {
    companion object {
        /*
        required for some description containing too long strings due to (mostly) long URL inside
         */
        fun String.shortenLongWords(): String {
            return this.split(" ").joinToString(" ") { word ->
                if (word.length > 20) {
                    "${word.take(5)}[...]${word.takeLast(5)}"
                } else {
                    word
                }
            }
        }
    }
}
