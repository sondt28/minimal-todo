package com.son.minimaltodo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.son.minimaltodo.model.Task
import com.son.minimaltodo.repository.TaskRepository

class HomeViewModel(repository: TaskRepository) : ViewModel() {
    var tasks: LiveData<List<Task>> = repository.getTasks()

}