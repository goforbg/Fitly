package com.androar.fitly

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class RecyclerPhoneListAdapter(
    val userList: ArrayList<PhoneListClass>
) : RecyclerView.Adapter<RecyclerPhoneListAdapter.ViewHolder>() {

    private val limit = 10


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerPhoneListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_phonelist, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerPhoneListAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    override fun getItemCount(): Int {
        if(userList.size > limit){
            return limit;
        }
        else
        {
            return userList.size
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: PhoneListClass) {
            val contactName  = itemView.findViewById(R.id.tvContactName) as TextView
            contactName.text = item.name
            val invite = itemView.findViewById<TextView>(R.id.btnInvite)
            invite.setOnClickListener {
                try {
                    val i = Intent(Intent.ACTION_VIEW)
                    val invitetext = "Hey, I found an app where we can Workout together \uD83D\uDE0D ! It's called *Fitly* and it's on the Playstore!"
                    val url =
                        "https://api.whatsapp.com/send?phone=" + item.number.toString() + "&text=" + invitetext
                    i.setPackage("com.whatsapp")
                    i.setData(Uri.parse(url))
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    itemView.rootView.context.startActivity(i)
                } catch (e: PackageManager.NameNotFoundException) {
                    Toast.makeText(itemView.rootView.context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                        .show()
                }


            }
        }
    }
}