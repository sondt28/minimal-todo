package com.son.minimaltodo.ui.reminder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.son.minimaltodo.model.Task
import com.son.minimaltodo.repository.TaskRepository
import kotlinx.coroutines.launch

class ReminderViewModel(val task: Task, private val taskRepository: TaskRepository) : ViewModel() {

    var selectedSpinnerValue = MutableLiveData<Int>()

    private var _isNavigateToHome = MutableLiveData(false)
    val isNavigateToHome: LiveData<Boolean> = _isNavigateToHome

    private var _isTaskChange = MutableLiveData<Task?>(null)
    val isTaskChange: LiveData<Task?> = _isTaskChange

    fun removeTask() = viewModelScope.launch {
        taskRepository.deleteTask(task)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTaskBasedOnSnooze() = viewModelScope.launch {
        when (selectedSpinnerValue.value) {
            1 -> {
                task.date = task.date.plusMinutes(30)
            }

            2 -> {
                task.date = task.date.plusHours(1)
            }

            3 -> {
                task.date = task.date.plusDays(1)
            }

            else -> {
                _isNavigateToHome.value = true
                return@launch
            }
        }

        val oldTask = taskRepository.getTask(task.id)

        oldTask?.let {
            taskRepository.upsertTask(task)
            _isTaskChange.value = task
        }
        _isNavigateToHome.value = true
    }

    fun navigateToHomeCompleted() {
        _isTaskChange.value = null
        _isNavigateToHome.value = false
    }
}