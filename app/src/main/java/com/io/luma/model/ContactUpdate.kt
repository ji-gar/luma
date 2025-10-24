package com.io.luma.model

data class ContactUpdate(
    val type: String,
    val contact_items: List<ContactItem>,
    val action: String,
    val session_id: String
)

data class ContactItem(
    val id: Int,
    val name: String,
    val relationship: String,
    val phone: String,
    val email: String,
    val type: String
)
