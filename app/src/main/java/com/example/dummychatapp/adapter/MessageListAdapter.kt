package com.example.dummychatapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dummychatapp.db.data.ChatData
import com.example.dummychatapp.databinding.ChatItemSendBinding

class MessageListAdapter :RecyclerView.Adapter<MessageListAdapter.MyHolderView>() {


   inner class MyHolderView(var binding:ChatItemSendBinding ):RecyclerView.ViewHolder(binding.root) {

       fun sendMsg(position: Int){
           if (differ.currentList[position].type==1) {
               binding.tvReceiveMsg.visibility =View.VISIBLE
               binding.tvSendMsg.visibility =View.GONE
               binding.tvReceiveMsg.text = differ.currentList[position].message
           }else{
               binding.tvReceiveMsg.visibility =View.GONE
               binding.tvSendMsg.visibility =View.VISIBLE
               binding.tvSendMsg.text = differ.currentList[position].message

           }
       }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        return MyHolderView(
            ChatItemSendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun getItemCount():Int{
       return differ.currentList.size
    }



    override fun onBindViewHolder(holder: MyHolderView, position: Int) {
      holder.sendMsg(position)
//        holder.setIsRecyclable(true)
    }

    private val differCallback = object : DiffUtil.ItemCallback<ChatData>(){
        override fun areItemsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
            return  oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)

}



