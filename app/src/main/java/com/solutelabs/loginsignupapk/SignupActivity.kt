package com.solutelabs.loginsignupapk

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_signup.*
import android.widget.*
import androidx.core.graphics.toColorInt
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.edittext_email
import kotlinx.android.synthetic.main.activity_signup.edittext_password
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class SignupActivity : AppCompatActivity() {

    private lateinit var rootView: View

    private var calendar = Calendar.getInstance()
    lateinit var database : UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        rootView = findViewById(android.R.id.content)

        val actionbar = supportActionBar
        actionbar!!.title = "Signup Page"
        actionbar.setDisplayHomeAsUpEnabled(true)

//        Number scroller for age
        numberpicker.minValue = 15
        numberpicker.maxValue = 60
        numberpicker.wrapSelectorWheel = false
        numberpicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS


        button_submit.setOnClickListener {
        val string_firstname = edittext_firstname.text.toString()
        val string_lastname = edittext_lastname.text.toString()
        val string_date = edittext_dob.text.toString()

        val selectedRadioButton:Int = gender_radio_group.checkedRadioButtonId
        val gender_selected= findViewById<RadioButton>(selectedRadioButton)

        val checkBoxes = listOf(checkbox_hobby_reading, checkbox_hobby_traveling, checkbox_hobby_shopping, checkbox_hobby_cooking)
        val values = listOf("Reading", "Traveling", "Shopping", "Cooking")
        val checkedValues = mutableListOf<String>()

        for (i in checkBoxes.indices) {
            if (checkBoxes[i].isChecked) {
                checkedValues.add(values[i])
            }
        }

            val Uidata1 = Validation(this)

            if(Uidata1.validFname(string_firstname) &&
                Uidata1.validLname(string_lastname) &&
                Uidata1.validGender(selectedRadioButton)  &&
                Uidata1.validdate(string_date) &&
                Uidata1.validemail(edittext_email.text.toString().trim()) &&
                Uidata1.validpassword(edittext_password.text.toString().trim()) &&
                Uidata1.validHobby(checkedValues))  {

                database = UserDatabase.getDatabase(this)

    //            for signup page if user is already not exist then create a user or else throw message
                GlobalScope.launch {
                    val userWithEmail = database.userDao().checkUserIsExist(edittext_email.text.toString().trim())
                    if (userWithEmail == null) {

                        database.userDao().insertUser(User(id = 0,
                            db_firstName = string_firstname,
                            db_lastName = string_lastname,
                            db_email = edittext_email.text.toString().trim(),
                            db_password = edittext_password.text.toString().trim(),
                            db_gender = gender_selected.text.toString(),
                            db_dateOfBirth = string_date,
                            db_age = numberpicker.value,
                            db_hobby = checkedValues.joinToString(", ")))
                            Errorsnackbar(" -----User is Added Successfully -------")
                            delay(1000)

                        SignupActivity.start(this@SignupActivity,true)

                    } else {
                        Errorsnackbar(" ----- Already Exist ------- ")
                    }
                }
            }
        }
        edittext_dob.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val minYear = year - 60
            val maxYear = year - 15

            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val formattedDate= String.format("%02d/%02d/%04d",dayOfMonth,monthOfYear,year)

                calendar.set(maxYear,month,day)
                edittext_dob.setText(formattedDate)

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val age = currentYear - year

                if (numberpicker.value < minYear || numberpicker.value > maxYear) {
                    numberpicker.value = age
                } else {
                    Log.i("hello","are $age year")
                }

            }, year, month, day)

            datePickerDialog.show()

        }

    }

    companion object {
        private const val keySignupToLogin = "signup_to_login"
        fun start(context: Context, isFromAdmin: Boolean) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(keySignupToLogin, isFromAdmin)
            (context as Activity).startActivity(intent)
        }
    }

    fun Errorsnackbar(message: String) {
        val snackbar = Snackbar.make(rootView,message, Snackbar.LENGTH_LONG)
        snackbar.setTextColor("#fa0505".toColorInt())
        snackbar.setBackgroundTint("#f0f2f5".toColorInt())
        snackbar.show()
    }

}






