package com.example.dummychatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dummychatapp.data.ChatData
import com.example.dummychatapp.databinding.ChatItemSendBinding

class MessageList(var chatData: List<ChatData>) :RecyclerView.Adapter<MessageList.MyViewHolder>() {

    private  var binding:ChatItemSendBinding?=null
    class MyViewHolder(var viewBinding: ChatItemSendBinding):RecyclerView.ViewHolder(viewBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       binding = ChatItemSendBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding!!)
    }

    override fun getItemCount(): Int {
        return chatData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding?.tvSendMsg?.text= chatData[position].message
    }
}