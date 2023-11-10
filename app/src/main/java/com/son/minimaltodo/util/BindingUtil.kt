package com.son.minimaltodo.util

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.son.minimaltodo.R
import com.son.minimaltodo.model.Task
import com.son.minimaltodo.ui.home.TaskItemAdapter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@BindingAdapter("taskList")
fun bindTaskList(recyclerView: RecyclerView, data: List<Task>?) {
    val adapter = recyclerView.adapter as TaskItemAdapter
    adapter.submitList(data)
}

@BindingAdapter("toggleEmptyListIcon")
fun toggleEmptyListIconVisibility(imageView: ImageView, data: List<Task>?) {
    if (data.isNullOrEmpty()) {
        imageView.visibility = View.VISIBLE
    } else {
        imageView.visibility = View.GONE
    }
}

@BindingAdapter("toggleEmptyListText")
fun toggleEmptyListText(textView: TextView, data: List<Task>?) {
    if (data.isNullOrEmpty()) {
        textView.visibility = View.VISIBLE
    } else {
        textView.visibility = View.GONE
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("formatDateTime")
fun formatDateTime(textView: TextView, date: LocalDateTime) {
    val timeZone = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    if (timeZone <= System.currentTimeMillis()) {
        textView.text = textView.context.getString(com.son.minimaltodo.R.string.release)
        textView.textSize = 14F
        val colorNormal = ContextCompat.getColor(textView.context, R.color.colorSecondaryVariant)
        textView.setTextColor(colorNormal)
    } else {
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy hh:mm a")
        val dateFormat = formatter.format(date)
        textView.text = dateFormat.toString()
    }
}

@BindingAdapter("setShortTitle")
fun setShortTitle(textView: TextView, title: String) {
    textView.text = title[0].toString()
}