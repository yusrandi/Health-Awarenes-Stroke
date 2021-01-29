package com.use.stroke.utils

import android.content.Context

class Constants {


    companion object{
//        const val API = "http://192.168.43.43/stroke/public/api/user/"

        const val BASE_API = "http://primarystrokeapp.com"
        const val API = "$BASE_API/api/user/"
        fun getID(context : Context) : Int{

            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)

            return pref.getInt("id", 0)
        }
        fun getRole(context : Context) : Int{

            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)

            return pref.getInt("role", 0)
        }

        fun getName(context : Context) : String? {

            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)

            return pref.getString("user", "undefined")
        }


        fun setAuth(context : Context, id:Int, user:String, role: Int){
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putBoolean("LOGIN", true)
                putInt("id", id)
                putString("user", user)
                putInt("role", role)
                apply()
            }
        }
        fun getAuth(context : Context) : Boolean {

            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)

            return pref.getBoolean("LOGIN", false)
        }
        fun clearToken(context : Context){
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidEmail (email : String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword (passwrod : String) = passwrod.length > 7

        fun setResult(context : Context, result:Int){
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putInt("result", result)
                apply()
            }
        }
        fun getResult(context : Context) : Int {

            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)

            return pref.getInt("result", 0)
        }

    }


}