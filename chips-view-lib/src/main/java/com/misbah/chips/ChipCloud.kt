package com.misbah.chips

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.util.AttributeSet
import com.misbah.chips.Chip.ChipBuilder

class ChipCloud : FlowLayout, ChipListener {
    private var context: Context?
    private var chipHeight = 0
    private var selectedColor = -1
    private var leftDrawable: Bitmap? = null
    private var selectedFontColor = -1
    private var unselectedColor = -1
    private var unselectedFontColor = -1
    private var selectTransitionMS = 750
    private var deselectTransitionMS = 500
    private var mode = Mode.SINGLE
    override var gravity = Gravity.LEFT
        set
    private var typeface: Typeface? = null
    private var allCaps = false
    private var rightRemove = false
    private var textSizePx = -1
    override var verticalSpacing = 0
        set
    override var minimumHorizontalSpacing = 0
        set
    private var chipListener: ChipListener? = null

    constructor(context: Context?) : super(context) {
        this.context = context
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context = context
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.ChipCloud, 0, 0)
        var arrayReference = -1
        try {
            selectedColor = a.getColor(R.styleable.ChipCloud_selectedColor, -1)
            selectedFontColor = a.getColor(R.styleable.ChipCloud_selectedFontColor, -1)
            unselectedColor = a.getColor(R.styleable.ChipCloud_deselectedColor, -1)
            unselectedFontColor = a.getColor(R.styleable.ChipCloud_deselectedFontColor, -1)
            selectTransitionMS = a.getInt(R.styleable.ChipCloud_selectTransitionMS, 750)
            deselectTransitionMS = a.getInt(R.styleable.ChipCloud_deselectTransitionMS, 500)
            val typefaceString = a.getString(R.styleable.ChipCloud_typeface)
            if (typefaceString != null) {
                typeface = Typeface.createFromAsset(getContext().assets, typefaceString)
            }
            textSizePx = a.getDimensionPixelSize(
                R.styleable.ChipCloud_textSize,
                resources.getDimensionPixelSize(R.dimen.default_textsize)
            )
            allCaps = a.getBoolean(R.styleable.ChipCloud_allCaps, false)
            val selectMode = a.getInt(R.styleable.ChipCloud_selectMode, 1)
            mode = when (selectMode) {
                0 -> Mode.SINGLE
                1 -> Mode.MULTI
                2 -> Mode.REQUIRED
                3 -> Mode.NONE
                else -> Mode.SINGLE
            }
            val gravityType = a.getInt(R.styleable.ChipCloud_gravity, 0)
            gravity = when (gravityType) {
                0 -> Gravity.LEFT
                1 -> Gravity.RIGHT
                2 -> Gravity.CENTER
                3 -> Gravity.STAGGERED
                else -> Gravity.LEFT
            }
            minimumHorizontalSpacing = a.getDimensionPixelSize(
                R.styleable.ChipCloud_minHorizontalSpacing,
                resources.getDimensionPixelSize(R.dimen.min_horizontal_spacing)
            )
            verticalSpacing = a.getDimensionPixelSize(
                R.styleable.ChipCloud_verticalSpacing,
                resources.getDimensionPixelSize(R.dimen.vertical_spacing)
            )
            arrayReference = a.getResourceId(R.styleable.ChipCloud_labels, -1)
        } finally {
            a.recycle()
        }
        init()
        if (arrayReference != -1) {
            val labels = resources.getStringArray(arrayReference)
            addChips(labels)
        }
    }

    private fun init() {
        chipHeight = resources.getDimensionPixelSize(R.dimen.material_chip_height)
    }

    fun setSelectedColor(selectedColor: Int) {
        this.selectedColor = selectedColor
    }

    fun setLeftDrawable(leftDrawable: Bitmap?) {
        this.leftDrawable = leftDrawable
    }

    fun setRightRemove(remove: Boolean) {
        rightRemove = remove
    }

    fun setSelectedFontColor(selectedFontColor: Int) {
        this.selectedFontColor = selectedFontColor
    }

    fun setUnselectedColor(unselectedColor: Int) {
        this.unselectedColor = unselectedColor
    }

    fun setUnselectedFontColor(unselectedFontColor: Int) {
        this.unselectedFontColor = unselectedFontColor
    }

    fun setSelectTransitionMS(selectTransitionMS: Int) {
        this.selectTransitionMS = selectTransitionMS
    }

    fun setDeselectTransitionMS(deselectTransitionMS: Int) {
        this.deselectTransitionMS = deselectTransitionMS
    }

    fun setMode(mode: Mode) {
        this.mode = mode
        for (i in 0 until childCount) {
            val chip = getChildAt(i) as Chip
            chip.deselect()
            chip.setLocked(false)
        }
    }

    fun setTypeface(typeface: Typeface?) {
        this.typeface = typeface
    }

    fun setTextSize(textSize: Int) {
        textSizePx = textSize
    }

    fun setAllCaps(isAllCaps: Boolean) {
        allCaps = isAllCaps
    }

    fun setChipListener(chipListener: ChipListener?) {
        this.chipListener = chipListener
    }

    fun addChips(labels: Array<String?>?) {
        for (label in labels!!) {
            addChip(label)
        }
    }

    fun addChip(label: String?) {
        val chip = ChipBuilder().index(childCount)
            .label(label)
            .typeface(typeface)
            .textSize(textSizePx)
            .allCaps(allCaps)
            .selectedColor(selectedColor)
            .leftDrawable(leftDrawable)
            .selectedFontColor(selectedFontColor)
            .unselectedColor(unselectedColor)
            .unselectedFontColor(unselectedFontColor)
            .selectTransitionMS(selectTransitionMS)
            .deselectTransitionMS(deselectTransitionMS)
            .chipHeight(chipHeight)
            .chipListener(this)
            .mode(mode)
            .build(context)
        addView(chip)
    }

    fun addChipView(label: String?) : Chip {
        return ChipBuilder().index(childCount)
            .label(label)
            .typeface(typeface)
            .textSize(textSizePx)
            .allCaps(allCaps)
            .selectedColor(selectedColor)
            .leftDrawable(leftDrawable)
            .selectedFontColor(selectedFontColor)
            .unselectedColor(unselectedColor)
            .unselectedFontColor(unselectedFontColor)
            .selectTransitionMS(selectTransitionMS)
            .deselectTransitionMS(deselectTransitionMS)
            .chipHeight(chipHeight)
            .chipListener(this)
            .mode(mode)
            .build(context)
    }

    fun removeChip(index: Int) {
        val chip = getChildAt(index) as Chip
        removeView(chip)
    }

    fun addChip(label: String?, leftDrawable: Bitmap?) {
        val chip = ChipBuilder().index(childCount)
            .label(label)
            .typeface(typeface)
            .textSize(textSizePx)
            .allCaps(allCaps)
            .selectedColor(selectedColor)
            .leftDrawable(leftDrawable)
            .selectedFontColor(selectedFontColor)
            .unselectedColor(unselectedColor)
            .unselectedFontColor(unselectedFontColor)
            .selectTransitionMS(selectTransitionMS)
            .deselectTransitionMS(deselectTransitionMS)
            .chipHeight(chipHeight)
            .chipListener(this)
            .mode(mode)
            .build(context)
        addView(chip)
    }

    fun addChip(label: String?, isRightRemove: Boolean) {
        val chip = ChipBuilder().index(childCount)
            .label(label)
            .typeface(typeface)
            .textSize(textSizePx)
            .allCaps(allCaps)
            .selectedColor(selectedColor)
            .leftDrawable(leftDrawable)
            .removeIcon(isRightRemove)
            .selectedFontColor(selectedFontColor)
            .unselectedColor(unselectedColor)
            .unselectedFontColor(unselectedFontColor)
            .selectTransitionMS(selectTransitionMS)
            .deselectTransitionMS(deselectTransitionMS)
            .chipHeight(chipHeight)
            .chipListener(this)
            .mode(mode)
            .build(context)
        addView(chip)
    }

    fun setSelectedChip(index: Int) {
        val chip = getChildAt(index) as Chip ?: return
        chip.select()
        if (mode == Mode.REQUIRED) {
            for (i in 0 until childCount) {
                val chipp = getChildAt(i) as Chip
                if (i == index) {
                    chipp.setLocked(true)
                } else {
                    chipp.setLocked(false)
                }
            }
        }
    }

    fun removeAllSelection() {
        val childCount = childCount
        for (index in 0 until childCount) {
            val chip = getChildAt(index) as Chip
            chip.deselect()
        }
    }

    override fun chipSelected(index: Int) {
        when (mode) {
            Mode.SINGLE, Mode.REQUIRED -> {
                var i = 0
                while (i < childCount) {
                    val chip = getChildAt(i) as Chip
                    if (i == index) {
                        if (mode == Mode.REQUIRED) chip.setLocked(true)
                    } else {
                        chip.deselect()
                        chip.setLocked(false)
                    }
                    i++
                }
            }

            else -> {}
        }
        if (chipListener != null) {
            chipListener!!.chipSelected(index)
        }
    }

    override fun chipDeselected(index: Int) {
        if (chipListener != null) {
            chipListener!!.chipDeselected(index)
        }
    }

    override fun chipRemoved(index: Int) {
        if (chipListener != null) {
            chipListener!!.chipRemoved(index)
        }
    }

    fun isSelected(index: Int): Boolean {
        if (index > 0 && index < childCount) {
            val chip = getChildAt(index) as Chip
            return chip.isSelected
        }
        return false
    }

    //Apparently using the builder pattern to configure an object has been labelled a 'Bloch Builder'.
    class Configure {
        private var chipCloud: ChipCloud? = null
        private var selectedColor = -1
        private var selectedFontColor = -1
        private var deselectedColor = -1
        private var deselectedFontColor = -1
        private var selectTransitionMS = -1
        private var deselectTransitionMS = -1
        private var mode: Mode? = null
        private var labels: Array<String?>? = null
        private var chipListener: ChipListener? = null
        private var gravity: Gravity? = null
        private var typeface: Typeface? = null
        private var allCaps: Boolean? = null
        private var textSize = -1
        private var minHorizontalSpacing = -1
        private var verticalSpacing = -1
        private var leftDrawable: Bitmap? = null
        private val rightRemove = false
        fun chipCloud(chipCloud: ChipCloud?): Configure {
            this.chipCloud = chipCloud
            return this
        }

        fun mode(mode: Mode?): Configure {
            this.mode = mode
            return this
        }

        fun leftDrawable(leftDrawable: Bitmap?): Configure {
            this.leftDrawable = leftDrawable
            return this
        }

        fun selectedColor(selectedColor: Int): Configure {
            this.selectedColor = selectedColor
            return this
        }

        fun selectedFontColor(selectedFontColor: Int): Configure {
            this.selectedFontColor = selectedFontColor
            return this
        }

        fun deselectedColor(deselectedColor: Int): Configure {
            this.deselectedColor = deselectedColor
            return this
        }

        fun deselectedFontColor(deselectedFontColor: Int): Configure {
            this.deselectedFontColor = deselectedFontColor
            return this
        }

        fun selectTransitionMS(selectTransitionMS: Int): Configure {
            this.selectTransitionMS = selectTransitionMS
            return this
        }

        fun deselectTransitionMS(deselectTransitionMS: Int): Configure {
            this.deselectTransitionMS = deselectTransitionMS
            return this
        }

        fun labels(labels: Array<String?>?): Configure {
            this.labels = labels
            return this
        }

        fun chipListener(chipListener: ChipListener?): Configure {
            this.chipListener = chipListener
            return this
        }

        fun gravity(gravity: Gravity?): Configure {
            this.gravity = gravity
            return this
        }

        fun typeface(typeface: Typeface?): Configure {
            this.typeface = typeface
            return this
        }

        /**
         * @param textSize value in pixels as obtained from @[android.content.res.Resources.getDimensionPixelSize]
         */
        fun textSize(textSize: Int): Configure {
            this.textSize = textSize
            return this
        }

        fun allCaps(isAllCaps: Boolean): Configure {
            allCaps = isAllCaps
            return this
        }

        fun minHorizontalSpacing(spacingInPx: Int): Configure {
            minHorizontalSpacing = spacingInPx
            return this
        }

        fun verticalSpacing(spacingInPx: Int): Configure {
            verticalSpacing = spacingInPx
            return this
        }

        fun build() {
            chipCloud!!.removeAllViews()
            if (mode != null) chipCloud!!.setMode(mode!!)
            if (gravity != null) chipCloud!!.gravity = gravity as Gravity
            if (typeface != null) chipCloud!!.setTypeface(typeface)
            if (textSize != -1) chipCloud!!.setTextSize(textSize)
            if (allCaps != null) chipCloud!!.setAllCaps(allCaps!!)
            if (selectedColor != -1) chipCloud!!.setSelectedColor(selectedColor)
            if (selectedFontColor != -1) chipCloud!!.setSelectedFontColor(selectedFontColor)
            if (deselectedColor != -1) chipCloud!!.setUnselectedColor(deselectedColor)
            if (deselectedFontColor != -1) chipCloud!!.setUnselectedFontColor(deselectedFontColor)
            if (selectTransitionMS != -1) chipCloud!!.setSelectTransitionMS(selectTransitionMS)
            if (deselectTransitionMS != -1) chipCloud!!.setDeselectTransitionMS(deselectTransitionMS)
            if (minHorizontalSpacing != -1) chipCloud!!.minimumHorizontalSpacing =
                minHorizontalSpacing
            if (leftDrawable != null) chipCloud!!.setLeftDrawable(leftDrawable)
            if (rightRemove) chipCloud!!.setRightRemove(rightRemove)
            if (verticalSpacing != -1) chipCloud!!.verticalSpacing = verticalSpacing
            chipCloud!!.setChipListener(chipListener)
            chipCloud!!.addChips(labels)
        }

        fun update() {
            val childCount = chipCloud!!.childCount
            for (i in 0 until childCount) {
                val chip = chipCloud!!.getChildAt(i) as Chip
                chip.text = labels!![i]
                chip.invalidate()
            }
            chipCloud!!.invalidate()
            chipCloud!!.requestLayout()
        }
    }
}