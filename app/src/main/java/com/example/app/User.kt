package com.example.app

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
    var UID: String? = "",
    var email: String? = ""
): Serializable