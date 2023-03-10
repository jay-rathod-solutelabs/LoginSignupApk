package com.solutelabs.loginsignupapk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.solutelabs.loginsignupapk.databinding.ActivityAdminListBinding
import kotlinx.android.synthetic.main.activity_admin_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AdminListActivity : AppCompatActivity() {

    lateinit var database : UserDatabase
    private lateinit var binding: ActivityAdminListBinding
    private var mAdapter :UserAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = UserDatabase.getDatabase(this)

        Log.i("hello","2222222222222222222")


        GlobalScope.launch {

            val userlist = database.userDao().getAllUsers()
            mAdapter = UserAdapter()

            recycleview.apply {
                layoutManager = LinearLayoutManager(this@AdminListActivity)
                adapter = mAdapter
                setAdapter(userlist)

                mAdapter?.setOnActionEditListener {
                        val builder = AlertDialog.Builder(this@AdminListActivity)
                        builder.setTitle(R.string.userdelete)
                        builder.setMessage(R.string.areyoursurefordelete)
                        builder.setIcon(R.drawable.baseline_delete_24_black)

                        builder.setPositiveButton("Yes"){p0, p1 ->
                            GlobalScope.launch {
                                database.userDao().deleteUser(it)
                                val updateduserlist = database.userDao().getAllUsers()
                                setAdapter(updateduserlist)
                            }
                            p0.dismiss()
                        }


                        //performing cancel action
                        builder.setNeutralButton("No"){p0 , p1 ->
                        }

                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.setCancelable(false)
                        alertDialog.show()
                    }
                }


        }

    }

    private fun setAdapter(list: List<User>) {
        runOnUiThread {
            mAdapter?.setData(list)
        }
    }


}