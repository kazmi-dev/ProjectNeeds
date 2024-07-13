package com.cooptech.pdfreader.util.genericRecyclerView

import androidx.recyclerview.widget.DiffUtil

open class BaseDiffUtilCallback<T: Any>(
    private val areItemsSame: (oldItem: T, newItem: T)-> Boolean,
    private val areContentsSame: (oldItem: T, newItem: T)-> Boolean
): DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsSame(oldItem, newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsSame(oldItem, newItem)
}