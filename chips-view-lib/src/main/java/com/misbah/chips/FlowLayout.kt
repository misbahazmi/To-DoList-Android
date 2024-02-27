package com.misbah.chips

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * @author RAW
 */
abstract class FlowLayout : ViewGroup {
    private var lineHeight = 0
    private val layoutProcessor = LayoutProcessor()
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        assert(MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED)
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        val count = childCount
        var lineHeight = 0
        var xPos = paddingLeft
        var yPos = paddingTop
        val childHeightMeasureSpec: Int
        childHeightMeasureSpec =
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST)
            } else {
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            }
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                child.measure(
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                    childHeightMeasureSpec
                )
                val childW = child.measuredWidth
                lineHeight = Math.max(lineHeight, child.measuredHeight + verticalSpacing)
                if (xPos + childW > width) {
                    xPos = paddingLeft
                    yPos += lineHeight
                }
                xPos += childW + minimumHorizontalSpacing
            }
        }
        this.lineHeight = lineHeight
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED || MeasureSpec.getMode(
                heightMeasureSpec
            ) == MeasureSpec.AT_MOST && yPos + lineHeight < height
        ) {
            height = yPos + lineHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val width = r - l
        var xPos = paddingLeft
        var yPos = paddingTop
        layoutProcessor.setWidth(width)
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                val childW = child.measuredWidth
                val childH = child.measuredHeight
                if (xPos + childW > width) {
                    xPos = paddingLeft
                    yPos += lineHeight
                    layoutProcessor.layoutPreviousRow()
                }
                layoutProcessor.addViewForLayout(child, yPos, childW, childH)
                xPos += childW + minimumHorizontalSpacing
            }
        }
        layoutProcessor.layoutPreviousRow()
        layoutProcessor.clear()
    }

    protected abstract val minimumHorizontalSpacing: Int
    protected abstract val verticalSpacing: Int
    protected abstract val gravity: Gravity

    private inner class LayoutProcessor internal constructor() {
        private var rowY = 0
        private val viewsInCurrentRow: MutableList<View>
        private val viewWidths: MutableList<Int>
        private val viewHeights: MutableList<Int>
        private var width = 0

        init {
            viewsInCurrentRow = ArrayList()
            viewWidths = ArrayList()
            viewHeights = ArrayList()
        }

        fun addViewForLayout(view: View, yPos: Int, childW: Int, childH: Int) {
            rowY = yPos
            viewsInCurrentRow.add(view)
            viewWidths.add(childW)
            viewHeights.add(childH)
        }

        fun clear() {
            viewsInCurrentRow.clear()
            viewWidths.clear()
            viewHeights.clear()
        }

        fun layoutPreviousRow() {
            val gravity: Gravity = gravity
            val minimumHorizontalSpacing: Int = minimumHorizontalSpacing
            when (gravity) {
                Gravity.LEFT -> {
                    var xPos = paddingLeft
                    var i = 0
                    while (i < viewsInCurrentRow.size) {
                        viewsInCurrentRow[i].layout(
                            xPos,
                            rowY,
                            xPos + viewWidths[i],
                            rowY + viewHeights[i]
                        )
                        xPos += viewWidths[i] + minimumHorizontalSpacing
                        i++
                    }
                }

                Gravity.RIGHT -> {
                    var xEnd = width - paddingRight
                    var i = viewsInCurrentRow.size - 1
                    while (i >= 0) {
                        val xStart = xEnd - viewWidths[i]
                        viewsInCurrentRow[i].layout(xStart, rowY, xEnd, rowY + viewHeights[i])
                        xEnd = xStart - minimumHorizontalSpacing
                        i--
                    }
                }

                Gravity.STAGGERED -> {
                    var totalWidthOfChildren = 0
                    run {
                        var i = 0
                        while (i < viewWidths.size) {
                            totalWidthOfChildren += viewWidths[i]
                            i++
                        }
                    }
                    val horizontalSpacingForStaggered = (width - totalWidthOfChildren - paddingLeft
                            - paddingRight) / (viewsInCurrentRow.size + 1)
                   var xPos = paddingLeft + horizontalSpacingForStaggered
                    var i = 0
                    while (i < viewsInCurrentRow.size) {
                        viewsInCurrentRow[i].layout(
                            xPos,
                            rowY,
                            xPos + viewWidths[i],
                            rowY + viewHeights[i]
                        )
                        xPos += viewWidths[i] + horizontalSpacingForStaggered
                        i++
                    }
                }

                Gravity.CENTER -> {
                    var totalWidthOfChildren = 0
                    run {
                        var i = 0
                        while (i < viewWidths.size) {
                            totalWidthOfChildren += viewWidths[i]
                            i++
                        }
                    }
                    var xPos = paddingLeft + (width - paddingLeft - paddingRight -
                            totalWidthOfChildren - minimumHorizontalSpacing * (viewsInCurrentRow.size - 1)) / 2
                    var i = 0
                    while (i < viewsInCurrentRow.size) {
                        viewsInCurrentRow[i].layout(
                            xPos,
                            rowY,
                            xPos + viewWidths[i],
                            rowY + viewHeights[i]
                        )
                        xPos += viewWidths[i] + minimumHorizontalSpacing
                        i++
                    }
                }
            }
            clear()
        }

        fun setWidth(width: Int) {
            this.width = width
        }
    }
}