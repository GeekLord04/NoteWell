package com.geekster.notewell.utlis

import android.text.TextUtils
import android.util.Patterns

class inputValidationHelper {

    fun userInputValidation(username : String, email : String, password : String) : Pair<Boolean, String>{
        var result = Pair(true, "")
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please fillup all the details")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false,"Please provide valid email address")
        }
        else if (password.length <=5){
            result = Pair(false, "Password must be greater than 5")
        }
        return result
    }
}