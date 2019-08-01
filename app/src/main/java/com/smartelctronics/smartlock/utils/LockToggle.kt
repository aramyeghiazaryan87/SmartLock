package com.smartelctronics.smartlock.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.smartelctronics.smartlock.R

class LockToggle : LinearLayout, View.OnClickListener {

    private lateinit var stateText: TextView
    private lateinit var lockImage: ImageView
    private var open: Boolean = false
    var onToggleListener: OnToggleListener? = null

    constructor(context: Context): super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet? = null) {
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.LockToggle, 0, 0)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.lock_toggle, this, true)

        open = attrArray.getBoolean(R.styleable.LockToggle_open, false)

        stateText = layout.findViewById(R.id.toggle_tv)
        lockImage = layout.findViewById(R.id.toggle_btn)
        lockImage.setOnClickListener(this)

        update()
    }

    private fun update() {
        if (open) {
            stateText.setTextColor(ContextCompat.getColor(context, R.color.green1))
            stateText.text = context.resources.getString(R.string.close)
            lockImage.background = ContextCompat.getDrawable(context, R.drawable.close)
        } else {
            stateText.setTextColor(ContextCompat.getColor(context, R.color.red))
            stateText.text = context.resources.getString(R.string.open)
            lockImage.background = ContextCompat.getDrawable(context, R.drawable.open)
        }
        open = !open
    }

    fun setOpen(open: Boolean) {
        this.open = open
        update()
    }

    fun getOpen(): Boolean = open

    override fun onClick(v: View?) {
        onToggleListener?.onToggle(open)
    }

    interface OnToggleListener {
        fun onToggle(openState: Boolean)
    }

}