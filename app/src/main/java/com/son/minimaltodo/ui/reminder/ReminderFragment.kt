package com.son.minimaltodo.ui.reminder

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
import com.son.minimaltodo.databinding.FragmentReminderBinding
import com.son.minimaltodo.util.AlarmUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ReminderFragment : Fragment(), ActionTask {
    private lateinit var binding: FragmentReminderBinding
    private val taskArgs: ReminderFragmentArgs by navArgs()
    private val reminderViewModel: ReminderViewModel by viewModel { parametersOf(taskArgs.task) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderBinding.inflate(inflater, container, false)

        binding.viewModel = reminderViewModel
        binding.lifecycleOwner = this

        setupClickListeners()
        observeViewModel()

        return binding.root
    }

    private fun setupClickListeners() {
        binding.btnRemove.setOnClickListener {
            val removeTask = RemoveTaskDialogFragment()
            removeTask.show(childFragmentManager, "dialog")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {
        reminderViewModel.isNavigateToHome.observe(viewLifecycleOwner, Observer { isDone ->
            if (isDone) {
                val task = reminderViewModel.isTaskChange.value

                task?.let {
                    AlarmUtil.setupAlarm(requireContext(), task)
                }

                findNavController().popBackStack()
                reminderViewModel.navigateToHomeCompleted()
            }
        })
    }

    override fun remove(isRemove: Boolean) {
        if (isRemove) {
            AlarmUtil.cancelAlarm(requireContext(), taskArgs.task.id)
            reminderViewModel.removeTask()

            findNavController().popBackStack()

            Toast.makeText(requireContext(), R.string.delete_success, Toast.LENGTH_SHORT).show()
        }
    }
}