package com.solutelabs.loginsignupapk

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import javax.security.auth.callback.Callback


class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    private var list = mutableListOf<User>()
    private var actiondelete : ((User) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]
        holder.listname?.text = user.db_firstName + " "  + user.db_lastName
        holder.listemail?.text = user.db_email
        holder.admindeleteicon.setOnClickListener {
            actiondelete?.invoke(user)
        }
    }

    override fun getItemCount() = list.size

    fun setData(data : List<User>){
        list.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()

    }

    fun setOnActionEditListener(callback: (User) -> Unit){
        this.actiondelete = callback
    }


    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val listname = itemView.listname
        val listemail = itemView.listemail
        val admindeleteicon = itemView.admin_delete_icon

    }

}

