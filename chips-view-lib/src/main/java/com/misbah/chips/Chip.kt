package com.misbah.chips

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

class Chip : AppCompatTextView, View.OnClickListener {
    private var index = -1
    private var selected = false
    private var listener: ChipListener? = null
    private var selectedFontColor = -1
    private var unselectedFontColor = -1
    private var crossfader: TransitionDrawable? = null
    private var selectTransitionMS = 750
    private var deselectTransitionMS = 500
    private var isLocked = false
    private var mode: Mode? = null
    private var leftDrawable: Bitmap? = null
    fun setChipListener(listener: ChipListener?) {
        this.listener = listener
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initChip(
        context: Context?,
        index: Int,
        label: String?,
        typeface: Typeface?,
        textSizePx: Int,
        allCaps: Boolean,
        selectedColor: Int,
        selectedFontColor: Int,
        unselectedColor: Int,
        unselectedFontColor: Int,
        mode: Mode?,
        leftDrawable: Bitmap?,
        isRemoveIcon: Boolean
    ) {
        this.index = index
        this.selectedFontColor = selectedFontColor
        this.unselectedFontColor = unselectedFontColor
        this.mode = mode
        this.leftDrawable = leftDrawable
        val selectedDrawable = ContextCompat.getDrawable(context!!, R.drawable.chip_selected)
        if (selectedFontColor == -1) {
            this.selectedFontColor = ContextCompat.getColor(context, R.color.selected_font_color)
        }
        val unselectedDrawable = ContextCompat.getDrawable(context, R.drawable.chip)
        if (unselectedFontColor == -1) {
            this.unselectedFontColor =
                ContextCompat.getColor(context, R.color.deselected_font_color)
        }
        val backgrounds = arrayOfNulls<Drawable>(2)
        backgrounds[0] = unselectedDrawable
        backgrounds[1] = selectedDrawable
        crossfader = TransitionDrawable(backgrounds)
        val leftPad = paddingLeft
        val topPad = paddingTop
        val rightPad = paddingRight
        val bottomPad = paddingBottom
        setBackgroundCompat(crossfader!!)
        setPadding(leftPad, topPad, rightPad, bottomPad)
        text = label
        if (isRemoveIcon && leftDrawable != null) {
            val drawableLeft =
                BitmapDrawable(resources, Bitmap.createScaledBitmap(leftDrawable, 40, 40, true))
            setCompoundDrawablesWithIntrinsicBounds(
                drawableLeft,
                null,
                ContextCompat.getDrawable(getContext(), R.drawable.ic_remove),
                null
            )
            compoundDrawablePadding = 8
        } else if (leftDrawable != null) {
            val drawableLeft =
                BitmapDrawable(resources, Bitmap.createScaledBitmap(leftDrawable, 40, 40, true))
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
            compoundDrawablePadding = 8
        } else if (isRemoveIcon) {
            setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(getContext(), R.drawable.ic_close),
                null
            )
            compoundDrawablePadding = 4
        } else {
            compoundDrawablePadding = 0
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        unselect()
        typeface?.let { setTypeface(it) }
        isAllCaps = allCaps
        if (textSizePx > 0) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx.toFloat())
        }
        if (isRemoveIcon) {
            setOnTouchListener(OnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= right - totalPaddingRight) {
                        if (listener != null) listener!!.chipRemoved(index)
                        return@OnTouchListener true
                    }
                }
                true
            })
        }
    }

    fun setLocked(isLocked: Boolean) {
        this.isLocked = isLocked
    }

    fun setSelectTransitionMS(selectTransitionMS: Int) {
        this.selectTransitionMS = selectTransitionMS
    }

    fun setDeselectTransitionMS(deselectTransitionMS: Int) {
        this.deselectTransitionMS = deselectTransitionMS
    }

    private fun init() {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (mode !== Mode.NONE) if (selected && !isLocked && mode !== Mode.SINGLE) {
            //set as unselected
            unselect()
            if (listener != null) {
                listener!!.chipDeselected(index)
            }
        } else if (!selected) {
            //set as selected
            crossfader!!.startTransition(selectTransitionMS)
            if (listener != null) {
                listener!!.chipSelected(index)
                setTextColor(selectedFontColor)
                for (drawable in compoundDrawables) {
                    if (drawable != null) {
                        drawable.colorFilter = PorterDuffColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.selected_font_color
                            ), PorterDuff.Mode.SRC_IN
                        )
                    }
                }
            }
        }
        selected = !selected
    }

    fun selectAndLock() {
        selected = false
        isLocked = true
        crossfader!!.startTransition(selectTransitionMS)
        setTextColor(selectedFontColor)
        for (drawable in compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.selected_font_color
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    fun select() {
        selected = true
        crossfader!!.startTransition(selectTransitionMS)
        setTextColor(selectedFontColor)
        if (listener != null) {
            listener!!.chipSelected(index)
        }
        for (drawable in compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.selected_font_color
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    fun selectOnlyUI() {
        selected = true
        crossfader!!.startTransition(selectTransitionMS)
        setTextColor(selectedFontColor)
        for (drawable in compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.selected_font_color
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    private fun unselect() {
        if (selected) {
            crossfader!!.reverseTransition(deselectTransitionMS)
        } else {
            crossfader!!.resetTransition()
        }
        setTextColor(unselectedFontColor)
        for (drawable in compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.deselected_font_color
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    @Suppress("deprecation")
    private fun setBackgroundCompat(background: Drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(background)
        } else {
            setBackground(background)
        }
    }

    fun deselect() {
        unselect()
        selected = false
    }

    class ChipBuilder {
        private var index = 0
        private var label: String? = null
        private var typeface: Typeface? = null
        private var textSizePx = 0
        private var allCaps = false
        private var selectedColor = 0
        private var selectedFontColor = 0
        private var unselectedColor = 0
        private var unselectedFontColor = 0
        private var chipHeight = 0
        private var selectTransitionMS = 750
        private var deselectTransitionMS = 500
        private var chipListener: ChipListener? = null
        private var mode:Mode? = null
        private var leftDrawable: Bitmap? = null
        private var removeIcon = false
        fun index(index: Int): ChipBuilder {
            this.index = index
            return this
        }

        fun leftDrawable(leftDrawable: Bitmap?): ChipBuilder {
            this.leftDrawable = leftDrawable
            return this
        }

        fun selectedColor(selectedColor: Int): ChipBuilder {
            this.selectedColor = selectedColor
            return this
        }

        fun selectedFontColor(selectedFontColor: Int): ChipBuilder {
            this.selectedFontColor = selectedFontColor
            return this
        }

        fun unselectedColor(unselectedColor: Int): ChipBuilder {
            this.unselectedColor = unselectedColor
            return this
        }

        fun unselectedFontColor(unselectedFontColor: Int): ChipBuilder {
            this.unselectedFontColor = unselectedFontColor
            return this
        }

        fun label(label: String?): ChipBuilder {
            this.label = label
            return this
        }

        fun typeface(typeface: Typeface?): ChipBuilder {
            this.typeface = typeface
            return this
        }

        fun allCaps(allCaps: Boolean): ChipBuilder {
            this.allCaps = allCaps
            return this
        }

        fun textSize(textSizePx: Int): ChipBuilder {
            this.textSizePx = textSizePx
            return this
        }

        fun chipHeight(chipHeight: Int): ChipBuilder {
            this.chipHeight = chipHeight
            return this
        }

        fun chipListener(chipListener: ChipListener?): ChipBuilder {
            this.chipListener = chipListener
            return this
        }

        fun mode(mode: Mode): ChipBuilder {
            this.mode = mode
            return this
        }

        fun removeIcon(mode: Boolean): ChipBuilder {
            removeIcon = mode
            return this
        }

        fun selectTransitionMS(selectTransitionMS: Int): ChipBuilder {
            this.selectTransitionMS = selectTransitionMS
            return this
        }

        fun deselectTransitionMS(deselectTransitionMS: Int): ChipBuilder {
            this.deselectTransitionMS = deselectTransitionMS
            return this
        }

        fun build(context: Context?): Chip {
            val chip = LayoutInflater.from(context).inflate(R.layout.chip, null) as Chip
            chip.initChip(
                context,
                index,
                label,
                typeface,
                textSizePx,
                allCaps,
                selectedColor,
                selectedFontColor,
                unselectedColor,
                unselectedFontColor,
                mode,
                leftDrawable,
                removeIcon
            )
            chip.setSelectTransitionMS(selectTransitionMS)
            chip.setDeselectTransitionMS(deselectTransitionMS)
            chip.setChipListener(chipListener)
            chip.height = chipHeight
            return chip
        }
    }
}