package com.elvesapp.chatsample.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

import com.elvesapp.chatsample.Constants
import com.google.gson.annotations.SerializedName

import org.json.JSONException
import org.json.JSONObject

@Entity
class User {
    @SerializedName(Constants.PARAMETER_ID)
    @PrimaryKey
    @ColumnInfo(name = Constants.PARAMETER_ID)
    var id: Long = 0
    @SerializedName(Constants.PARAMETER_NAME)
    @ColumnInfo(name = Constants.PARAMETER_NAME)
    var name: String? = null
    @SerializedName(Constants.PARAMETER_FIRST_NAME)
    @ColumnInfo(name = Constants.PARAMETER_FIRST_NAME)
    var firstName: String? = null
    @SerializedName(Constants.PARAMETER_LAST_NAME)
    @ColumnInfo(name = Constants.PARAMETER_LAST_NAME)
    var lastName: String? = null
    @SerializedName(Constants.PARAMETER_EMAIL)
    @ColumnInfo(name = Constants.PARAMETER_EMAIL)
    private var email: String? = null
    @SerializedName(Constants.PARAMETER_PASSWORD)
    @ColumnInfo(name = Constants.PARAMETER_PASSWORD)
    var password: String? = null

    @SerializedName(Constants.PARAMETER_LOGIN_EMAIL)
    @Ignore
    var loginEmailData: JSONObject? = null

    init {
        this.id = 0
        this.name = ""
        this.firstName = ""
        this.lastName = ""
        this.email = ""
        this.password = ""
        loginEmailData = JSONObject()
    }

    fun getEmail(): String? {
        if (loginEmailData == null) {
            return this.email
        }
        try {
            return this.loginEmailData!!.getString(Constants.PARAMETER_EMAIL)
        } catch (e: JSONException) {

        }

        return this.email
    }

    fun setEmail(email: String) {
        if (loginEmailData == null) {
            loginEmailData = JSONObject()
        }
        try {
            this.loginEmailData!!.put(Constants.PARAMETER_EMAIL, email)
            this.email = email
        } catch (e: JSONException) {

        }

    }
}
