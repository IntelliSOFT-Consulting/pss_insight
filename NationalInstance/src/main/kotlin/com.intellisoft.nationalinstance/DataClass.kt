package com.intellisoft.nationalinstance

import com.fasterxml.jackson.annotation.JsonProperty

data class Results(val code: Int, val details: Any?)

data class DbResults(
    val count: Int,
    val details: Any?
)

data class DbError(val details: Any?)

data class DbDataEntry(
    val program: String,
    val orgUnit: String,
    val eventDate: String,
    val status: String,
    val storedBy: String,
    val dataValues: ArrayList<DbDataValues>
)
data class DbDataValues(
    val dataElement: String,
    val value: String
)
data class DbTemplateData(
    val versionNumber: String?,
    val description: String?,
    val program: String?
)
data class DbTemplate(
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("program")
    val program: String?,
    @JsonProperty("metadata")
    val metadata: Any?
)