package com.intellisoft.internationalinstance

import org.springframework.http.ResponseEntity
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class FormatterClass {

    //Convert date to string
    fun convertDateToString(date: Date?): String {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return if (date != null){
            val dateStr = inputFormat.parse(date.toString())
            outputFormat.format(dateStr)
        }else{
            ""
        }


    }

    fun isEmailValid(emailAddress: String):Boolean{

        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$"
        val pat = Pattern.compile(emailRegex)
        return pat.matcher(emailAddress).matches()
    }

    fun getOpenMrsDate(time: String): String? {

        return try {

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val output = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val d = sdf.parse(time)
            output.format(d)

        }catch (e: Exception){
            null
        }


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
                ResponseEntity.badRequest().body(DbError(results.details.toString()))
            }
        }
    }






}