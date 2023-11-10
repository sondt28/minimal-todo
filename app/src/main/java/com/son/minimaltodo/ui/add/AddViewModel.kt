package com.son.minimaltodo.ui.add

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.son.minimaltodo.model.Task
import com.son.minimaltodo.repository.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Random

@RequiresApi(Build.VERSION_CODES.O)
class AddViewModel(private val task: Task?, private val repository: TaskRepository) : ViewModel() {
    var titleInput = MutableLiveData(task?.title)
    var isNotifyEnabled = MutableLiveData(task?.isNotify ?: false)
    var selectedDate = MutableLiveData<String>()
    var selectedTime = MutableLiveData<String>()

    private val _navigateToHome = MutableLiveData<Task?>(null)
    val navigateToHome: LiveData<Task?> = _navigateToHome

    private val _isSaveSuccessful = MutableLiveData(false)
    val isSaveSuccessful: LiveData<Boolean> = _isSaveSuccessful

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveTask() = viewModelScope.launch {
        if (isValidInput()) {
            val formattedDateTime = formatDateTime(selectedDate.value, selectedTime.value)

            task?.let { task ->
                val updateTask =
                    Task(task.id, titleInput.value!!, formattedDateTime, false, isNotifyEnabled.value!!, task.color)
                repository.upsertTask(updateTask)
                _navigateToHome.value = updateTask
            } ?: run {
                val color = generateRandomColor()
                val createTask = Task(0, titleInput.value!!, formattedDateTime, false, isNotifyEnabled.value!!, color)
                val taskId = repository.upsertTask(createTask)

                _navigateToHome.value = repository.getTask(taskId.toInt())
            }

            _isSaveSuccessful.value = true
        }
    }

    fun removeTask() = viewModelScope.launch {
        task?.let {
            repository.deleteTask(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(dateNow: String) {
        selectedDate.value = if (task == null) dateNow else splitDate(task.date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTime(timeNow: String) {
        selectedTime.value = if (task == null) timeNow else splitTime(task.date)
    }

    private fun isValidInput(): Boolean {
        return titleInput.value?.isNotBlank() == true
    }

    private fun generateRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateTime(date: String?, time: String?): LocalDateTime {
        val dateTimeString = "$date $time"
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy hh:mm a")
        return LocalDateTime.parse(dateTimeString, formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun splitDate(dateTime: LocalDateTime): String {
        val splitDate = dateTime.toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
        return splitDate.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun splitTime(dateTime: LocalDateTime): String {
        val splitTime = dateTime.toLocalTime()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return splitTime.format(formatter)
    }

    fun doneNavigation() {
        _isSaveSuccessful.value = false
        _navigateToHome.value = null
    }
}