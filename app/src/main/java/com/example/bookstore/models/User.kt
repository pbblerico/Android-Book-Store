package com.example.bookstore.models

import java.sql.Timestamp

data class User(var email: String, var password: String, var timestamp: Long, var uid: String, var userType: String = "User") {

}
