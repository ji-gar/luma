package com.io.luma.model
typealias PatientContactsResponseModel = List<PatientContact>

data class PatientContact(
    val user_id: Int,
    val full_name: String,
    val known_as: String?,
    val email: String,
    val phone_number: String,
    val avatar_gender: String?,
    val location: String?,
    val is_active: Boolean,
    val completion_stage: Int,
    val relationship_type: String,
    val is_primary_carer: Boolean,
    val last_conversation: String?
)

//data class PatientContactsResponseModel(
//    val success: Boolean? = true,
//    val message: String? = null,
//    val data: List<PatientContact>? = null
//)
//
//data class PatientContact(
//    val user_id: Int,
//    val full_name: String,
//    val known_as: String?,
//    val email: String,
//    val phone_number: String,
//    val avatar_gender: String?,
//    val location: String?,
//    val is_active: Boolean,
//    val completion_stage: Int,
//    val relationship_type: String,
//    val is_primary_carer: Boolean,
//    val last_conversation: String?
//)