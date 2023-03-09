package com.intellisoft.mailserver

import com.fasterxml.jackson.annotation.JsonProperty

data class DbRespondents(
    @JsonProperty("respondents")
    val respondents: List<DbSurveyRespondent>
)
data class DbSurveyRespondent(
    @JsonProperty("emailAddress")
    val emailAddress:String,

    @JsonProperty("expiryDate")
    val expiryDate:String,

    @JsonProperty("customUrl")
    val customUrl: String,

    @JsonProperty("password")
    val password:String
)
data class Results(val code: Int, val details: Any?)
data class DbDetails(val details: Any?)