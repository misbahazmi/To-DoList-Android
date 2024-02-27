package com.misbah.todo.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import com.misbah.todo.core.base.BaseFragment
import com.misbah.todo.ui.listeners.OnDateTimeListener
import com.misbah.todo.ui.utils.Utils
import com.nytimes.utils.AppLog
import java.util.Calendar
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 03-Oct-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class TimePickerFragment : BaseFragment<DialogViewModel>()  , TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private val calendar: Calendar = Calendar.getInstance()
    internal lateinit var viewModel: DialogViewModel
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    @Inject
    lateinit var utils: Utils
    companion object {
        private lateinit var dateTimeListener: OnDateTimeListener
        @JvmStatic
        fun newInstance(listener: OnDateTimeListener) = run {
            dateTimeListener = listener
            TimePickerFragment()
        }
    }
    override fun getViewModel(): DialogViewModel {
        viewModel = ViewModelProvider(viewModelStore, factory)[DialogViewModel::class.java]
        return viewModel
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return  DatePickerDialog(requireContext(), this, year, month, day)
    }
    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        try {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            AppLog.debugD("Date: ${java.text.DateFormat.getDateTimeInstance().format(calendar.time.time)}")
            dateTimeListener.onDateTimeSelected(calendar.time.time)
        }catch (e : Exception) {
            e.localizedMessage?.let { AppLog.debugE(it) }
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        calendar.set(year, month,day)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity)).show()
    }
}