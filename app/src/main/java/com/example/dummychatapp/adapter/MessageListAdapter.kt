package com.example.dummychatapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dummychatapp.db.data.ChatData
import com.example.dummychatapp.databinding.LayoutChatItemBinding
import java.text.SimpleDateFormat
import java.util.*

class MessageListAdapter : RecyclerView.Adapter<MessageListAdapter.MyHolderView>() {

var isNotPre=""
    inner class MyHolderView(private var binding: LayoutChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun sendMsg(position: Int) {


            val date = Date(differ.currentList[position].chatTime)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = sdf.format(calendar.time)

            if (isNotPre!=formattedDate){
               isNotPre=formattedDate
                binding.tvDateDay.visibility=View.VISIBLE
                binding.tvDateDay.text=formattedDate
            }

            val simpleDateFormat = SimpleDateFormat("hh:mm")
            if (differ.currentList[position].type == 1) {
                binding.tvReceiveMsg.visibility = View.VISIBLE
                binding.tvSendMsg.visibility = View.GONE
                binding.tvReceiveMsg.text = differ.currentList[position].message
                binding.tvReceiveTime.text = simpleDateFormat.format(differ.currentList[position].chatTime)
            } else {
                binding.tvReceiveMsg.visibility = View.GONE
                binding.tvSendMsg.visibility = View.VISIBLE
                binding.tvSendMsg.text = differ.currentList[position].message
                binding.tvSendTime.text = simpleDateFormat.format(differ.currentList[position].chatTime)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        return MyHolderView(
            LayoutChatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) {
        holder.sendMsg(position)
    }

    private val differCallback = object : DiffUtil.ItemCallback<ChatData>() {
        override fun areItemsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}



