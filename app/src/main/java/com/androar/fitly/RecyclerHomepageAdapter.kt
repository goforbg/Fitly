package com.androar.fitly

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener


class RecyclerHomepageAdapter (
    private val context: Context,
    @Nullable internal var list: ArrayList<PhoneListClass>?,
    @Nullable internal var excercisesList: ArrayList<ExcercisesListClass>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal val VIEW_TYPE_CONTACT = 1
    internal val VIEW_TYPE_VIDEO = 2

    private inner class ViewHolderContact internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val contactName = itemView.findViewById(R.id.tvContactName) as TextView
        val contactScore = itemView.findViewById<TextView>(R.id.tvScore)
        val contactimage = itemView.findViewById<ImageView>(R.id.userEmoji)
        val invite = itemView.findViewById<ConstraintLayout>(R.id.item_rv_phonelist)

        internal fun bind(position: Int) {
            contactName.text = list!![position].name

            if (list!![position].name.equals("Your Trainer")) {
                contactimage.setImageResource(R.drawable.ic_batman)
                contactScore.text = "🦇"
            }

            invite.setOnClickListener {
                if (list!![position].name.equals("Your Trainer")) {
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
                            "https://api.whatsapp.com/send?phone=" + list!![position].number.toString() + "&text=" + invitetext
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

    private inner class ViewHolderVideo internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var rvExcercises: RecyclerView = itemView.findViewById(R.id.rvExcercises)

        @SuppressLint("ClickableViewAccessibility")
        internal fun bind(position: Int) {
            rvExcercises.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvExcercises.addOnItemTouchListener(object : OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val action = e.action
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent
                            .requestDisallowInterceptTouchEvent(true)
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })

            val adapter = ExcercisesListAdapter(excercisesList!!)
            rvExcercises.adapter = adapter

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CONTACT) {
            ViewHolderContact(LayoutInflater.from(context).inflate(R.layout.item_rv_phonelist, parent, false))
        }
        else ViewHolderVideo(LayoutInflater.from(context).inflate(R.layout.item_rv_adapter,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_VIDEO) {
            (holder as ViewHolderVideo).bind(position)
        } else {
            (holder as ViewHolderContact).bind(position)
        }
    }

    override fun getItemCount(): Int {
        var size = 1
        if (list.isNullOrEmpty()) { size = excercisesList!!.size }
        else size = list!!.size
        return size
    }

    override fun getItemViewType(position: Int): Int {
        if (list!![position].type ===  "video") {
           return VIEW_TYPE_VIDEO
        } else  {
            return VIEW_TYPE_CONTACT }
    }
}
