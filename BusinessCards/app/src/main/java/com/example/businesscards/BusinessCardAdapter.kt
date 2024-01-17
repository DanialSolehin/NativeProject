package com.example.businesscards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.businesscards.databinding.ListItemBinding

class BusinessCardAdapter(private val businessCardList: List<BusinessCard>, private val onDeleteClickListener: OnDeleteClickListener? = null) :
    RecyclerView.Adapter<BusinessCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onDeleteClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = businessCardList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return businessCardList.size
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(businessCard: BusinessCard)
    }

    class ViewHolder(private val binding: ListItemBinding, private val onDeleteClickListener: OnDeleteClickListener?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(businessCard: BusinessCard) {
            binding.nameTextView.text = "Name: ${businessCard.name}"
            binding.businessTextView.text = "Business: ${businessCard.business}"
            binding.locationTextView.text = "Location: ${businessCard.location}"
            binding.phoneTextView.text = "Phone: ${businessCard.phone}"

            // Set onClickListener for delete icon
            binding.deleteIcon.setOnClickListener {
                onDeleteClickListener?.onDeleteClick(businessCard)
            }
        }
    }
}