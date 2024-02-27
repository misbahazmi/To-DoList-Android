package com.misbah.chips

interface ChipListener {
    fun chipSelected(index: Int)
    fun chipDeselected(index: Int)
    fun chipRemoved(index: Int)
}