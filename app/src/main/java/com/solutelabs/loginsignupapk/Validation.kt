package com.solutelabs.loginsignupapk

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.graphics.toColorInt
import com.google.android.material.snackbar.Snackbar


class Validation(private val context: Context) {

    val rootView = (context as Activity).findViewById<View>(android.R.id.content)

    fun validFname(string_firstname:String):Boolean{
        if(string_firstname.isNotEmpty()){
            return true
        }else{
            Errorsnackbar("First name cannot be empty")
        }
        return false
    }

    fun validemail(edittext_email:String):Boolean{
        if(edittext_email.isNotEmpty()){
            if(Patterns.EMAIL_ADDRESS.matcher(edittext_email).matches() == true)
            {
                return true
            }
            else if(edittext_email == "admin"){
                return true
            }
            else{
                Errorsnackbar("Email is not valid")
            }
        }
        else{
            Errorsnackbar("Email cannot be empty")
        }
        return false
    }

    fun validpassword(edittext_password:String): Boolean{
        if(edittext_password.isNotEmpty()){
            val containsUppercase = edittext_password.any { it.isUpperCase() }
            val containsLowercase = edittext_password.any { it.isLowerCase() }
            val containsDigit = edittext_password.any { it.isDigit() }
            if(containsUppercase && containsLowercase && containsDigit == true) {
                return true
            }
            else if(edittext_password == "admin"){
                return true
            }
            else{
                Errorsnackbar("Password is not valid")
            }

        }else{
            Errorsnackbar("Password cannot be empty")
        }
        return false
    }

    fun validLname(string_lastname:String):Boolean{
        if(string_lastname.isNotEmpty()){
            return true
        }else{
            Errorsnackbar("Last name cannot be empty")
        }
        return false
    }

    fun validdate(edittext_dob:String):Boolean{
        if(edittext_dob.isNotEmpty()){
            return true
        }else{
            Errorsnackbar("Date cannot be empty")
        }
        return false
    }

    fun validGender(selectedRadioButton:Int):Boolean{
        if(selectedRadioButton == -1){
            Errorsnackbar("Gender cannot be empty")
        }else{
            return true
        }
        return false
    }

    fun validHobby(checkedValues:MutableList<String>):Boolean{
        if(checkedValues.isNotEmpty()){
            return true
        }else{
            Errorsnackbar("Hobby cannot be empty")
        }
        return false
    }


    fun Errorsnackbar(message: String) {
        val snackbar = Snackbar.make(rootView,message, Snackbar.LENGTH_LONG)
        snackbar.setTextColor("#fa0505".toColorInt())
        snackbar.setBackgroundTint("#f0f2f5".toColorInt())
        snackbar.show()
    }
}
