package com.example.test2

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatRecyclerAdapter (private val chatLis : ArrayList<Chat>) :
    RecyclerView.Adapter<ChatRecyclerAdapter.viewHolder>(){

    val imgArray : Array<Int> =  arrayOf(R.drawable.character1,R.drawable.character2,
        R.drawable.character3,R.drawable.character4,R.drawable.character5)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val  itemView= LayoutInflater.from(parent.context).inflate(R.layout.chat_item,
        parent,false);
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val curItem=chatLis[position]

        holder.chat.text=curItem.chat
        holder.senderName.text=curItem.senderName
        holder.senderChar.setImageResource(imgArray[curItem.charNum!!])

    }

    override fun getItemCount(): Int {
        return chatLis.size
    }



    class viewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val chat : TextView = itemView.findViewById(R.id.tv_chat)
        val senderName : TextView = itemView.findViewById(R.id.tv_sender_name)
        val senderChar : ImageView = itemView.findViewById(R.id.img_sender_char)
    }
}