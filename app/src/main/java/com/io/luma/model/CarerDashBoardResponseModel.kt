package com.io.luma.model

import com.google.gson.annotations.SerializedName

data class CarerDashBoardResponseModel(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("data")
    val data: CarerData? = null,

    @SerializedName("message")
    val message: String? = null
)

data class CarerData(
    @SerializedName("user_id")
    val userId: Int? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean? = null,

    @SerializedName("is_email_verified")
    val isEmailVerified: Boolean? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("country_code")
    val countryCode: String? = null,

    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("last_name")
    val lastName: String? = null,

    @SerializedName("full_name")
    val fullName: String? = null,

    @SerializedName("known_as")
    val knownAs: String? = null,

    @SerializedName("language")
    val language: String? = null,

    @SerializedName("date_of_birth")
    val dateOfBirth: String? = null,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("profile_picture_url")
    val profilePictureUrl: String? = null,

    @SerializedName("avatar_gender")
    val avatarGender: String? = null,

    @SerializedName("completion_stage")
    val completionStage: Int? = null,

    @SerializedName("user_stage")
    val userStage: Int? = null,

    @SerializedName("agent_stage")
    val agentStage: Int? = null,

    @SerializedName("all_agents_complete")
    val allAgentsComplete: Boolean? = null,

    @SerializedName("patients")
    val patients: List<PatientModel>? = null
)

data class PatientModel(
    @SerializedName("patient_id")
    val patientId: Int? = null,

    @SerializedName("patient_name")
    val patientName: String? = null,

    @SerializedName("is_primary_carer")
    val isPrimaryCarer: Boolean? = null
)
