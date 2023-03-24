package com.example.dummychatapp.adapter

import android.annotation.SuppressLint
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
import javax.inject.Inject

class MessageListAdapter @Inject constructor(): RecyclerView.Adapter<MessageListAdapter.MyHolderView>() {
    val smsTime: Calendar? = Calendar.getInstance()
    inner class MyHolderView(private var binding: LayoutChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun sendMsg(position: Int) {

            val date = Date(differ.currentList[position].chatTime)
            val currentTime = Calendar.getInstance()
            currentTime.time = date
            val sdf = SimpleDateFormat("EEE, dd/MM/yyyy", Locale.getDefault())
            val formattedDate = sdf.format(currentTime.time)

            if (currentTime.get(Calendar.DATE) == smsTime?.get(Calendar.DATE) ) {
                "Today".also { binding.tvDateDay.text = it }
            } else if (currentTime.get(Calendar.DATE) - (smsTime?.get(Calendar.DATE)!!) == 1  ){
                "Yesterday".also { binding.tvDateDay.text = it }
            }  else {
                binding.tvDateDay.text=formattedDate
            }


            val simpleDateFormat = SimpleDateFormat("hh:mm",Locale.getDefault())
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



