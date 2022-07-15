package com.mirkamol.proguardpinterest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mirkamol.proguardpinterest.databinding.ItemHomePhotoBinding
import com.mirkamol.proguardpinterest.model.HomePhotoItem

class HomePhotoAdapter : RecyclerView.Adapter<HomePhotoAdapter.VH>() {
    private val dif = AsyncListDiffer(this, HomePhotoAdapter.ITEM_DIFF)

    inner class VH(private val binding: ItemHomePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val books = dif.currentList[adapterPosition]
            Glide.with(binding.root.context)
                .load(books.urls.regular)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivHomePhoto)

            binding.countLike.text = books.likes.toString()

        }
    }

    fun submitList(list: List<HomePhotoItem>) {
        dif.submitList(list)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HomePhotoAdapter.VH {
        return VH(
            ItemHomePhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: HomePhotoAdapter.VH, position: Int) =
        holder.bind()

    override fun getItemCount(): Int = dif.currentList.size

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<HomePhotoItem>() {
            override fun areItemsTheSame(
                oldItem: HomePhotoItem,
                newItem: HomePhotoItem): Boolean =
                oldItem.urls == newItem.urls && oldItem.likes == newItem.likes


            override fun areContentsTheSame(
                oldItem: HomePhotoItem,
                newItem: HomePhotoItem
            ): Boolean =
                oldItem == newItem
        }
    }
}
