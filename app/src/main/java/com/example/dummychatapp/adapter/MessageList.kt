package com.example.dummychatapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dummychatapp.data.ChatData
import com.example.dummychatapp.data.ChatReceiveData
import com.example.dummychatapp.databinding.ChatItemReceiveBinding
import com.example.dummychatapp.databinding.ChatItemSendBinding

class MessageList(
    var chatData: List<ChatData>, var type:Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var binding: ChatItemSendBinding? = null
    private var mbinding: ChatItemReceiveBinding? = null


    inner class MyViewHolderSend(var viewBinding: ChatItemSendBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun sendMsg(position: Int) {
            viewBinding.tvSendMsg?.text = chatData[position].message
        }
    }

    inner class MyViewHolderReceive(var viewBindingM: ChatItemReceiveBinding) :
        RecyclerView.ViewHolder(viewBindingM.root) {
        fun receiveMsg(position: Int) {
            viewBindingM.tvRecMsg.text = chatData[position].message

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       if (type==1){
          return MyViewHolderSend(
                ChatItemSendBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false)
          )
       }
        else{
         return MyViewHolderReceive(
                ChatItemReceiveBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // binding?.tvSendMsg?.text= chatData[position].message
//                val message=messages[position]
//        val messageReceive = chatReceiveData[position]


        if (type==1) {
            val data=chatData[position].message
            binding?.tvSendMsg?.text = data
            Log.d("SendMsg3",chatData[position].message)
        }
//        if (chatReceiveData!=null && type==0)
//                mbinding?.tvRecMsg?.text =chatReceiveData[position].message

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
//       if (type==1) return chatData.size
//        else return chatReceiveData.size
        return  chatData.size
    }

}