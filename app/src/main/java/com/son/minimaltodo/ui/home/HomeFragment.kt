package com.son.minimaltodo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.son.minimaltodo.R
import com.son.minimaltodo.databinding.FragmentHomeBinding
import com.son.minimaltodo.util.startAnimation
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var taskItemAdapter: TaskItemAdapter
    private val viewModel: HomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupClickListeners()
        setupTaskAdapter()

        return binding.root
    }
    

    private fun setupTaskAdapter() {
        taskItemAdapter = TaskItemAdapter(TaskItemAdapter.OnClickTaskItem {task ->
            val action = HomeFragmentDirections.actionHomeFragmentToAddFragment(task)
            findNavController().navigate(action)
        })

        binding.rcTasks.apply {
            adapter = taskItemAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun setupClickListeners() {
        binding.fab.setOnClickListener {
            binding.fab.isVisible = false
            binding.vCircle.isVisible = true
            binding.vCircle.startAnimation(setupFabAnimation()) {
                val navOption = NavOptions.Builder()
                    .setEnterAnim(R.anim.enter_fade_in)
                    .setPopExitAnim(R.anim.exit_from_top)
                    .build()
                findNavController().navigate(R.id.addFragment, null, navOption)
            }
        }
    }

    private fun setupFabAnimation(): Animation {
        return AnimationUtils.loadAnimation(requireContext(), R.anim.anim_circle_explosion).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.inflateMenu(R.menu.menu_overflow)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.aboutFragment -> {
                    findNavController().navigate(R.id.aboutFragment)
                    true
                }

                else -> false
            }
        }
    }
}