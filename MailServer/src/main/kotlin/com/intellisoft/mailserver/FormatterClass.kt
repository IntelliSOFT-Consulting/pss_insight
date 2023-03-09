package com.intellisoft.mailserver

import org.springframework.http.ResponseEntity
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FormatterClass {

    fun extractName(emailAddress: String): String{
        return emailAddress.substringBefore("@")
    }
    fun getRemainingTime(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateString, formatter)

        // Convert to Instant and calculate duration between current time and given date
        val instant = dateTime.toInstant(ZoneOffset.UTC)
        val duration = Duration.between(Instant.now(), instant)

        // Calculate remaining time in days, hours, minutes, and seconds
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        // Build remaining time string
        val sb = StringBuilder()
        if (days > 0) sb.append("$days days, ")
        sb.append("$hours hours, $minutes minutes, and $seconds seconds remaining")
        return sb.toString()
    }
    fun getResponse(results: Results): ResponseEntity<*>? {
        return when (results.code) {
            200, 201 -> {
                ResponseEntity.ok(results.details)
            }
            500 -> {
                ResponseEntity.internalServerError().body(results)
            }
            else -> {
                ResponseEntity.badRequest().body(DbDetails(results.details.toString()))
            }
        }
    }
}