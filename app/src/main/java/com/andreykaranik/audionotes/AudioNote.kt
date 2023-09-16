package com.andreykaranik.audionotes

/**
 * This class represents an audio note.
 *
 * @author Andrey Karanik
 */
data class AudioNote(
    val name: String = "NoName",
    val path: String,
    val duration: Long,
    val date: String = "01:01:2000",
    val time: Long = 0
)
