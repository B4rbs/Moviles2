package com.example.moviles2primerparcial.ui.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(private val spacePx: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val pos = parent.getChildAdapterPosition(view)
        outRect.left = spacePx
        outRect.right = spacePx
        outRect.bottom = spacePx
        if (pos == 0) outRect.top = spacePx
    }
}