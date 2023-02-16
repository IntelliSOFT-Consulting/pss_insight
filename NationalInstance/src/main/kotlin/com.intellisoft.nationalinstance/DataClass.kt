package com.intellisoft.nationalinstance

import com.fasterxml.jackson.annotation.JsonProperty

data class Results(val code: Int, val details: Any?)

data class DbResults(
    val count: Int,
    val details: Any?
)

data class DbError(val details: Any?)

data class DbDataElementsValue(
    val description: String?,
    val program: String?
)

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
    val metadata: DbMetaData?
)
data class DbOrganisationUnit(
    @JsonProperty("organisationUnits")
    val organisationUnits : List<DbOrgUnits>
)
data class DbOrgUnits(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("displayName")
    val displayName: String
)
data class DbMetaData(

    @JsonProperty("body")
    val body : DbBody

)

data class DbBody(
    @JsonProperty("dataElements")
    val dataElements: ArrayList<DbDataElementData>
)

data class DbDataElementData(
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("code")
    val code: String?,
    @JsonProperty("created")
    val created: String?,
    @JsonProperty("formName")
    val formName: String?,
    @JsonProperty("valueType")
    val valueType: String?,
    @JsonProperty("description")
    val description: String?
)
data class DbDataEntrySave(
    val httpStatus: String?,
    val httpStatusCode: Int?,
    val message: String?
)
data class DbAllTemplate(
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("program")
    val program: String?,
    @JsonProperty("metadata")
    val metadata: Any?
)
data class DbSaveTemplate(
    @JsonProperty("httpStatusCode")
    val httpStatusCode: Int?,
)