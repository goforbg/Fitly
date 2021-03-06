package com.androar.fitly.ui.adapter

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.androar.fitly.R
import com.androar.fitly.ui.activity.VideoCallActivity
import com.androar.fitly.model.ExcercisesListClass
import com.androar.fitly.model.PhoneListClass


class RecyclerPhoneListAdapter(
    @Nullable val userList: ArrayList<PhoneListClass>?,
    @Nullable val activityList: List<ExcercisesListClass>?
) : RecyclerView.Adapter<RecyclerPhoneListAdapter.ViewHolder>() {

    private val limit = 10


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rv_phonelist, parent, false)
            return ViewHolder(
                v
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(userList!![position])
    }


    override fun getItemCount(): Int {
        if (userList!!.size > limit) {
            return limit;
        } else {
            return userList.size
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: PhoneListClass) {
            val contactName = itemView.findViewById(R.id.tvContactName) as TextView
            val contactScore = itemView.findViewById<TextView>(R.id.tvScore)
            contactName.text = item.name
            val contactimage = itemView.findViewById<ImageView>(R.id.userEmoji)
            val invite = itemView.findViewById<ConstraintLayout>(R.id.item_rv_phonelist)

            if (item.name.equals("Your Trainer")) {
                contactimage.setImageResource(R.drawable.ic_batman)
                contactScore.text = "🦇"
            }

            invite.setOnClickListener {
                if (item.name.equals("Your Trainer")) {
                    contactimage.setImageResource(R.drawable.ic_batman)
                    contactScore.text = "🦇"
                    val intent = Intent(itemView.rootView.context, VideoCallActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("roomID", "test")
                    itemView.rootView.context.startActivity(intent)
                } else {
                    try {
                        val i = Intent(Intent.ACTION_VIEW)
                        val invitetext =
                            "Hey, I found an app where we can Workout together \uD83D\uDE0D ! It's called *Fitly* and it's on the Playstore!"
                        val url =
                            "https://api.whatsapp.com/send?phone=" + item.number.toString() + "&text=" + invitetext
                        i.setPackage("com.whatsapp")
                        i.setData(Uri.parse(url))
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        itemView.rootView.context.startActivity(i)
                    } catch (e: PackageManager.NameNotFoundException) {
                        Toast.makeText(
                            itemView.rootView.context,
                            "WhatsApp not Installed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }


}