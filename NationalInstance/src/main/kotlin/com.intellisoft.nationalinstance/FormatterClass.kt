package com.intellisoft.nationalinstance

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.http.*
import org.springframework.util.Base64Utils
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class FormatterClass {


    fun getHeaders(): HttpEntity<String?>? {
        val headers = HttpHeaders()
        headers["Authorization"] = "Basic " +
                Base64Utils.encodeToString("admin:district".toByteArray())
        return HttpEntity(headers)
    }
     fun saveSelectedMetadataToNationalInstance(
         internationalUrl: String,
         dataStore: String,
         masterTemplate: String,
         nationalUrl: String,
         version: String,
         restTemplate: RestTemplate
     ) {

         CoroutineScope(Dispatchers.IO).launch {

             // Get the metadata json
             val templateUrl: String = "$internationalUrl$dataStore$masterTemplate/$version"
             val dbAllTemplateResponseEntity: ResponseEntity<DbAllTemplate> = restTemplate.exchange<DbAllTemplate>(
                 templateUrl,
                 HttpMethod.GET, getHeaders(), DbAllTemplate::class.java
             )
             if (dbAllTemplateResponseEntity.statusCode == HttpStatus.OK) {
                 val dbAllTemplate = dbAllTemplateResponseEntity.body

                 // Create the request headers
                 val headers = HttpHeaders()
                 val auth = "admin:district"
                 val encodedAuth = Base64.getEncoder().encode(auth.toByteArray(StandardCharsets.UTF_8))
                 val authHeader = "Basic $encodedAuth"
                 headers.add("Authorization", authHeader)
                 val nationalTemplateUrl: String = "$nationalUrl$dataStore$masterTemplate/$version"

                 // Create the request body as a Map
                 val request = HttpEntity(dbAllTemplate, headers)
                 val response: ResponseEntity<DbSaveTemplate> = restTemplate.postForEntity<DbSaveTemplate>(
                     nationalTemplateUrl, request, DbSaveTemplate::class.java
                 )
                 if (response.statusCodeValue == 201) {
                     println(" $response")
                 } else {
                     println(" fail")
                 }
             }

         }


    }

    //Check date format
    fun isValidDate(date: String): Boolean {
        val dateRegex = """\d{4}-\d{2}-\d{2}"""
        return date.matches(Regex(dateRegex))
    }

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
                ResponseEntity.badRequest().body(DbDetails(results.details.toString()))
            }
        }
    }



}