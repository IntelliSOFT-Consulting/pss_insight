package com.intellisoft.internationalinstance

import com.fasterxml.jackson.annotation.JsonProperty
import org.json.JSONArray
import org.json.JSONObject

data class Results(val code: Int, val details: Any?)

data class DbResults(
    val count: Int,
    val details: Any?
)

data class DbDetails(val details: Any?)

data class DbMetadataJson(
    @JsonProperty("programs")
    val programs: List<DbPrograms>
)

data class DbProgramsList(
    @JsonProperty("programs")
    val programs: List<DbPrograms>
)

data class DbPrograms(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("created")
    val created: String,
    @JsonProperty("name")
    val name: String,
)
data class DbTemplate(
    @JsonProperty("description")
    val description: String?,
    @JsonProperty("program")
    val program: String?,
    @JsonProperty("metadata")
    val metadata: Any?
)

data class DbTemplateData(
    val versionNumber: String?,
    val description: String?,
    val program: String?
)
data class DbSaveTemplate(
    @JsonProperty("httpStatusCode")
    val httpStatusCode: Int?,
)
data class DbVersionData(
    val versionDescription: String?,
    val isPublished: Boolean,
    val indicators: List<String>,

    val createdBy: String?,
    val publishedBy: String?,

    var versionId: Long?)
enum class PublishStatus {
    DRAFT,
    PUBLISHED
}
data class DbIndicatorValues(
    val versionName:String,
    val versionDescription:String,
    val versionId: Long,
    val status: String,
    val indicators: List<String>,


    )

data class DbFrontendIndicators(
    val categoryName: String,
    val indicatorName: String,
    val indicators: List<DbIndicators>
)
data class DbIndicators(
    val code: String,
    val name: String,
    val id: String
)
data class DbFrontendCategoryIndicators(
    val categoryName: String,
    val indicators: List<DbFrontendIndicators>
)