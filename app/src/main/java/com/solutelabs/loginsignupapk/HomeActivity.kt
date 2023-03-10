package com.solutelabs.loginsignupapk

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.toColorInt
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class HomeActivity : AppCompatActivity() {

    lateinit var database : UserDatabase
    private lateinit var rootView: View
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rootView = findViewById(android.R.id.content)

        //create db object
        database = UserDatabase.getDatabase(this)



        val email = intent.getStringExtra("current_user_email")

        GlobalScope.launch {
            val user = database.userDao().getDetailsFromEmail(email.toString())
            label_fullname.setText(user?.db_firstName.toString() + " " + user?.db_lastName.toString())
            label_gender.setText(user?.db_gender.toString())
            label_dateofbirth.setText(user?.db_dateOfBirth.toString())
            label_age.setText(user?.db_age.toString())
            label_hobby.setText(user?.db_hobby.toString())

        }

        label_gender.imeOptions = EditorInfo.IME_ACTION_DONE

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        label_gender.isEnabled = false
        label_age.isEnabled = false
        label_dateofbirth.isEnabled = false
        label_hobby.isEnabled = false

        editicon_gender.setOnClickListener { showKeyboard(label_gender, imm) }
        editicon_age.setOnClickListener { showKeyboard(label_age, imm) }
        editicon_hobby.setOnClickListener { showKeyboard(label_hobby, imm) }

        editicon_dateofbirth.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val minYear = year - 60
            val maxYear = year - 15

            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val formattedDate= String.format("%02d/%02d/%04d",dayOfMonth,monthOfYear,year)
                calendar.set(maxYear,month,day)

                label_dateofbirth.setText(formattedDate)

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val age = currentYear - year

                if(age in 16..60){
                    label_age.setText(age.toString())
                }

            }, year, month, day)

            datePickerDialog.show()

        }

        // button update query from email
        button_update.setOnClickListener {
            GlobalScope.launch {
                val user = database.userDao().getDetailsFromEmail(email.toString())
                val updatedUser = user!!.copy(
                    db_gender = label_gender.text.toString(),
                    db_dateOfBirth = label_dateofbirth.text.toString(),
                    db_age = label_age.text.toString().toInt(),
                    db_hobby = label_hobby.text.toString()
                )

                database.userDao().updateUser(updatedUser)
                Errorsnackbar(" ------- Sucessfully Updated --------- ")
            }
        }

        button_delete.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.userdelete)
            builder.setMessage(R.string.areyoursurefordelete)
            builder.setIcon(R.drawable.baseline_delete_24_black)

            builder.setPositiveButton("Yes"){dialogInterface, which ->
                GlobalScope.launch {
                    val user = database.userDao().getDetailsFromEmail(email.toString())
                    database.userDao().deleteUserFromEmail(email.toString())
                    Errorsnackbar(" ------- User is Deleted --------- ")
                    delay(1000)
                    HomeActivity.start(this@HomeActivity,true)
                }
            }

            //performing cancel action
            builder.setNeutralButton("No"){dialogInterface , which ->
                    Log.i("hello","NOONONONONONONO")
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit,menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun showKeyboard(editText: EditText, imm: InputMethodManager) {
        editText.isEnabled = true
        editText.requestFocus()
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        editText.setSelection(editText.text!!.length)
    }

    fun Errorsnackbar(message: String) {
        val snackbar = Snackbar.make(rootView,message, Snackbar.LENGTH_LONG)
        snackbar.setTextColor("#fa0505".toColorInt())
        snackbar.setBackgroundTint("#f0f2f5".toColorInt())
        snackbar.show()
    }

    companion object {
        private const val keyHomeToLogout = "home_to_logout"
        fun start(context: Context, isFromAdmin: Boolean) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(keyHomeToLogout, isFromAdmin)
            (context as Activity).startActivity(intent)
        }
    }

}

