package com.solutelabs.loginsignupapk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.core.graphics.toColorInt
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.edittext_email
import kotlinx.android.synthetic.main.activity_main.edittext_password
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {

    private lateinit var rootView: View

    private lateinit var sharedPreferences: SharedPreferences


    lateinit var database : UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val actionbar = supportActionBar
        actionbar!!.title = "Login Page"

        rootView = findViewById(android.R.id.content)


//        for link "Signup"
        val clickMeText = textview_line_signup.text
        val spannableString = SpannableString(clickMeText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                MainActivity.start(this@MainActivity,true,false,false,"")
            }
        }

        spannableString.setSpan(clickableSpan, 23, clickMeText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textview_line_signup.text = spannableString
        textview_line_signup.movementMethod = LinkMovementMethod.getInstance()


        button_login.setOnClickListener {

            val Uidata = Validation(this)
            val email = edittext_email.text.toString()
            val password = edittext_password.text.toString()


            if(email == "admin" && password == "admin"){
                MainActivity.start(this@MainActivity,false,false,true,"")
            }


//            shared pref in checkbox remember me && validate that input field is empty or not
            val rememberMe = checkbox_remember_me.isChecked
            if(Uidata.validemail(email) && Uidata.validpassword(password)) {
                database = UserDatabase.getDatabase(this)

//                check if user is exist or not
                GlobalScope.launch {
                    val user = database.userDao().getUserByEmailAndPassword(email, password)

                    if(user?.db_firstName == "admin"){
                        MainActivity.start(this@MainActivity,false,true,false,email)
                    }
                    else if(user != null) {
                        delay(500)
                        if(rememberMe) {
                            editor.putString("email", email)
                            editor.putString("password", password)
                            Log.i("hello","-----------enter in checkbox clicked if condition -----------")
                            editor.apply()
                        }
                        else if(!rememberMe){
                            editor.putString("email", null)
                            editor.putString("password", null)
                            edittext_email.setText(email)
                            edittext_password.setText(password)
                            Log.i("hello","-----------enter in checkbox clicked else if-----------")

                        }
                        MainActivity.start(this@MainActivity,false,true,false,email)
                    }else {
                        Errorsnackbar("----- User is not Exist--------")
                    }
                }

            }

        }

    }

//    for naviagate throw intents & also pass email for identify which user can log in.
    companion object {
        private const val keyLoginToSignup = "login_to_signup"
        private const val keyLoginToHome = "login_to_home"
        private const val keyCurrentUserEmail = "current_user_email"
        private const val keyLoginToAdmin = "login_to_admin"

        fun start(context: Context, logintosignup: Boolean, logintohome: Boolean, logintoadmin: Boolean,email: String?) {
            val intent = when {
                logintosignup -> Intent(context, SignupActivity::class.java)
                logintohome -> Intent(context, HomeActivity::class.java)
                logintoadmin -> Intent(context, AdminListActivity::class.java)
                else -> Intent(context, MainActivity::class.java)
            }
            intent.putExtra(if (logintosignup) keyLoginToSignup else keyLoginToHome, logintohome)
            if (email != null) {
                intent.putExtra(keyCurrentUserEmail, email)
            }
            (context as Activity).startActivity(intent)
        }
    }


    fun Errorsnackbar(message: String) {
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        snackbar.setTextColor("#fa0505".toColorInt())
        snackbar.setBackgroundTint("#f0f2f5".toColorInt())
        snackbar.show()
    }

}
