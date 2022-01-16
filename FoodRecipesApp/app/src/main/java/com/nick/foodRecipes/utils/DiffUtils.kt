package com.nick.foodRecipes.utils

import androidx.recyclerview.widget.DiffUtil

class DiffUtils<T>(
    private val oldList: List<T> = emptyList(),
    private val newList: List<T> = emptyList()
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}