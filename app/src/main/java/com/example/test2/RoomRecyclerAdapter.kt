package com.example.test2

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_make_room.view.*
import kotlinx.android.synthetic.main.activity_signup.view.*
import kotlinx.android.synthetic.main.room_item.view.*

class RoomRecyclerAdapter(private val roomLis : ArrayList<Room>,
                          ): RecyclerView.Adapter<RoomRecyclerAdapter.MyViewHolder>() {

    private lateinit var mListener : onItemClickListener


    interface onItemClickListener{

        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener){
        mListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)


        return MyViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = roomLis[position]
        holder.title.text = currentitem.title
        holder.players.text = currentitem.players.toString()

    }

    override fun getItemCount(): Int {
        return roomLis.size
    }


    class MyViewHolder(itemView: View,listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){


        val title: TextView = itemView.findViewById(R.id.tv_roomTitle)
        val players: TextView = itemView.findViewById(R.id.tv_players)

        init{

            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}

