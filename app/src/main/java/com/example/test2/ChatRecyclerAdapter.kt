package com.example.test2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.NonDisposableHandle.parent


class ChatRecyclerAdapter(
    val curEmail : String,
    val context: Context,
    val messageList: ArrayList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    val ITEM_RECEIVE=1
    val ITEM_SENT=2



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==1){
            val view : View=LayoutInflater.from(context).inflate(R.layout.receive,parent,
            false)

            return ReceiveViewHolder(view)
        }else{
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]



        if(holder.javaClass==SentViewHolder::class.java){

            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text=currentMessage.chat

        }else{

            val viewHolder=holder as ReceiveViewHolder
            viewHolder.receiveMessage.text=currentMessage.chat
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        if((curEmail).equals(currentMessage.senderEmail)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    class SentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val sentMessage=itemView.findViewById<TextView>(R.id.tv_sent_message)
    }

    class ReceiveViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val receiveMessage=itemView.findViewById<TextView>(R.id.tv_receive_message)
        val receiveName = itemView.findViewById<TextView>(R.id.tv_name)
    }
}