package com.androar.fitly
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerPhoneListAdapter(
    val userList: ArrayList<PhoneListClass>
) : RecyclerView.Adapter<RecyclerPhoneListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerPhoneListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_phonelist, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerPhoneListAdapter.ViewHolder, position: Int) {
        holder.bindItems(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: PhoneListClass) {
            val contactName  = itemView.findViewById(R.id.tvContactName) as TextView
            contactName.text = item.name
        }
    }
}