package com.intellisoft.nationalinstance

import com.fasterxml.jackson.annotation.JsonProperty

data class Results(val code: Int, val details: Any?)

data class DbResults(
    val count: Int,
    val details: Any?
)

data class DbDetails(val details: Any?)

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
    val dataValues: List<DbDataValues>
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
    @JsonProperty("httpStatus")
    val httpStatus: String?,
    @JsonProperty("httpStatusCode")
    val httpStatusCode: Int?,
    @JsonProperty("message")
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
data class DbVersionData(
    val versionDescription: String?,
    val isPublished: Boolean,
    val indicators: List<String>,

    val createdBy: String?,
    val publishedBy: String?,

    var versionId: Long?)
enum class PublishStatus {
    DRAFT,
    PUBLISHED,

    SUBMITTED,
    SENT,

    REQUESTED,
    REJECTED,
    ACCEPTED,
    PAST_DUE

}
data class DbIndicatorValues(
    val versionName:String,
    val versionDescription:String,
    val versionId: String,
    val status: String,
    val indicators: Any, )
data class DbDataEntryData(
    val orgUnit: String,
    val selectedPeriod: String?,
    val status: String?,
    val dataEntryPersonId: String,
    val dataEntryDate: String?,
    val responses: List<DbDataEntryResponses>,
)
data class DbDataEntryResponses(
    val indicator: String,
    val response: String?,
    val comment: String?,
    val attachment: String?

)
data class DbSurvey(
    val surveyName: String,
    val surveyDescription: String,
    val status: String,
    val creatorId: String,
    val indicators : List<String>
)
data class DbSurveyRespondent(
    val emailAddressList: List<String>,
    val expiryDateTime: String,
    val surveyId: String,
    val customAppUrl: String
)
data class DbSurveyDetails(
    val respondentId: String,
    val password: String
)
data class DbResponse(
    val respondentId: String,
    val responses: List<DbRespondentSurvey>
)
data class DbRespondentSurvey(
    val indicatorId: String,
    val answer: String,
    val comments: String?,
    val attachment: String?
)


data class DbRequestLink(
    val respondentId: String,
    val comments: String?
)

data class DbFrontendCategoryIndicators(
    val categoryName: String,
    val indicators: List<DbFrontendIndicators>
)
data class DbFrontendIndicators(
    val code: String,
    val indicatorId: String,
    val categoryName: String,
    val indicatorName: String,
    val indicators: List<DbIndicators>
)
data class DbIndicators(
    val code: String,
    val name: String,
    val id: String
)

data class DbFrontendCategoryIndicatorsAnswers(
    val categoryName: String,
    val indicators: List<DbFrontendIndicatorAnswers>
)
data class DbFrontendIndicatorAnswers(
    val code: String,
    val indicatorId: String,
    val categoryName: String,
    val indicatorName: String,
    val indicators: List<DbRespondentSurvey>
)


data class DbSurveyRespondentDetails(
    val surveyId: String,
    val surveyName: String,
    val respondents: List<DbRespondentDetails>
)
data class DbRespondentDetails(
    val id:String,
    val emailAddress : String,
    val date: String
)
data class DbRespondents(
    val respondents: List<DbSurveyRespondentData>
)
data class DbSurveyRespondentData(
    val emailAddress:String,
    val expiryDate:String,
    val customUrl: String,
    val password:String
)
data class DbSurveyRespondentsDetails(
    val id: String,
    val name: String,
    val description: String,
    val status: String,
    val creatorId: String,
    val createdAt: String,
    val updatedAt: String,
    val indicators: List<Any>,
)