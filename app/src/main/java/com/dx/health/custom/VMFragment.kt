package com.dx.health.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment


/**
 * Author: Meng
 * Date: 2024/11/21
 * Modify: 2024/11/21
 * Desc:
 */

open class VMFragment<VB : ViewDataBinding>(@LayoutRes val layoutId: Int) : Fragment() {

    private var isLoaded = false
    protected lateinit var binding: VB
    protected lateinit var activity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<VB>(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        onBindView(binding as VB, savedInstanceState)
        return binding.root
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    // 视图绑定 -同onCreateView
    open fun onBindView(binding: VB, savedInstanceState: Bundle?) {}

    // 懒加载
    open fun lazyInit(binding: VB) {}

    private fun navigation(): NavController {
        return NavHostFragment.findNavController(this)
    }

    protected fun navigateTo(
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        navExtras: Navigator.Extras? = null
    ) {
        navigation().navigate(resId, args, navOptions, navExtras)
    }

    protected fun onBackPressed() {
//        val canBack = navigation().navigateUp()
        val canBack = navigation().popBackStack()
        if(canBack) {
            activity.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            isLoaded = true
            lazyInit(binding)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }
}