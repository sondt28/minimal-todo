package com.son.minimaltodo.ui.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.son.minimaltodo.R
import com.son.minimaltodo.databinding.FragmentAddBinding
import com.son.minimaltodo.ui.reminder.ActionTask
import com.son.minimaltodo.ui.reminder.RemoveTaskDialogFragment
import com.son.minimaltodo.util.AlarmUtil
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFragment : Fragment(), ActionTask {
    private lateinit var binding: FragmentAddBinding
    private val taskArgs: AddFragmentArgs by navArgs()
    private val addViewModel: AddViewModel by viewModel { parametersOf(taskArgs.task) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.viewModel = addViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
        setupClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupViews() {
        // setup date, time
        addViewModel.setDate(formatCalendarDate(calendar))
        addViewModel.setTime(formatTimeIn12HourClock(hour, minute))

        // delete action
        setupMenu()
    }

    private fun setupMenu() {
        if (taskArgs.task != null) {
            binding.toolbar.inflateMenu(R.menu.menu_action)
            binding.toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.removeTask -> {
                        showRemoveTaskDialog()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun showRemoveTaskDialog() {
        val dialog = RemoveTaskDialogFragment()
        dialog.show(childFragmentManager, "dialog")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        addViewModel.isSaveSuccessful.observe(viewLifecycleOwner, Observer {
            if (it) {
                addViewModel.navigateToHome.value?.let { task ->
                    AlarmUtil.setupAlarm(requireContext(), task)
                }

                findNavController().popBackStack()
                addViewModel.doneNavigation()
            }
        })
    }

    private fun setupClickListeners() {
        binding.edtDate.setOnClickListener { showDatePickerDialog() }
        binding.edtTime.setOnClickListener { showTimePickerDialog() }

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }

                binding.edtDate.setText(formatCalendarDate(selectedDate))
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
            val formattedTime = formatTimeIn12HourClock(hourOfDay, minute)
            binding.edtTime.setText(formattedTime)
        }, hour, minute, false)

        timePickerDialog.show()
    }

    private fun formatCalendarDate(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun formatTimeIn12HourClock(hourOfDay: Int, minute: Int): String {
        val formattedHour =
            if (hourOfDay == 0) 12 else if (hourOfDay <= 12) hourOfDay else hourOfDay - 12
        val amPmIndicator = if (hourOfDay < 12) "AM" else "PM"
        return String.format(
            Locale.getDefault(),
            "%02d:%02d %s",
            formattedHour,
            minute,
            amPmIndicator
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun remove(isRemove: Boolean) {
        if (isRemove) {
            taskArgs.task?.let {
                AlarmUtil.cancelAlarm(requireContext(), taskArgs.task!!.id)
            }
            addViewModel.removeTask()

            findNavController().popBackStack()
            Toast.makeText(requireContext(), R.string.delete_success, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
    }
}