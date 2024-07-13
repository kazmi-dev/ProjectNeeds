package com.cooptech.pdfreader.util.genericRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class GenericRecyclerViewAdapter<T: Any>(
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean)-> ViewBinding,
    private val bindView: (ViewBinding, T, Int)-> Unit,
    private val diffUtil: DiffUtil.ItemCallback<T>
): RecyclerView.Adapter<BaseViewHolder<T>>() {

    private val adapterData: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val binding = bindingInflater(LayoutInflater.from(parent.context), parent, false)
        return object : BaseViewHolder<T>(binding){
            override fun bind(item: T) {
                bindView(binding, item, adapterPosition )
            }
        }
    }

    override fun getItemCount(): Int = adapterData.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(adapterData[position])
    }

    fun setAdapterData(list: List<T>){
        val diffUtil = GenericDiffCallback(adapterData, list, diffUtil)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        adapterData.clear()
        adapterData.addAll(list)
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun updateAdapterOnRemove(list: List<T>, pos: Int){
        adapterData.clear()
        adapterData.addAll(list)
        notifyItemRemoved(pos)
    }

    class GenericDiffCallback<T: Any>(
        private val oldList: List<T>,
        private val newList: List<T>,
        private val diffUtil: DiffUtil.ItemCallback<T>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return diffUtil.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return diffUtil.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
        }
    }

}