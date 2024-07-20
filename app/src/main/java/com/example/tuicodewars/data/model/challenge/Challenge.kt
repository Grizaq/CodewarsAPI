package com.example.tuicodewars.data.model.challenge

data class Challenge(
    val approvedAt: String,
    val approvedBy: ApprovedBy,
    val category: String,
    val contributorsWanted: Boolean,
    val createdAt: String,
    val createdBy: CreatedBy,
    val description: String,
    val id: String,
    val languages: List<String>,
    val name: String,
    val publishedAt: String,
    val rank: Rank,
    val slug: String,
    val tags: List<String>,
    val totalAttempts: Int,
    val totalCompleted: Int,
    val totalStars: Int,
    val unresolved: Unresolved,
    val url: String,
    val voteScore: Int
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