package com.ql.health.ui.fragment

import android.graphics.Typeface
import android.util.Log
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.ql.health.R
import com.ql.health.config.Consts
import com.ql.health.custom.VMFragment
import com.ql.health.databinding.FragmentPersonalBinding
import com.ql.health.model.body.PersonalBody
import com.ql.health.model.entity.UserEntity
import com.ql.health.module.common.network.RFCallback
import com.ql.health.module.network.Client

class PersonalFragment : VMFragment<FragmentPersonalBinding>(R.layout.fragment_personal) {

    private var height = 170
    private var weight = 60
    private var year = 1990
    private var sex = 1 // 1: 男 2: 女

    override fun lazyInit(binding: FragmentPersonalBinding) {
//        super.lazyInit(binding)

        val picker = binding.loginAge
        setPicker(picker, 1930, 2025)
        picker.value = year
        picker.setOnValueChangedListener { p, oldVal, newVal ->
            Log.i("PersonalFragment", "Selected number: $newVal")
            year = newVal
        }

        val picker2 = binding.loginHeight
        setPicker(picker2, 50, 240)
        picker2.value = height
        picker2.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.i("PersonalFragment", "Selected number: $newVal")
            height = newVal
        }

        val picker3 = binding.loginWight
        setPicker(picker3, 10, 200)
        picker3.value = weight
        picker3.setOnValueChangedListener { picker, oldVal, newVal ->
            Log.i("PersonalFragment", "Selected number: $newVal")
            weight = newVal
        }

        binding.loginSex.setOnCheckedChangeListener { group, checkedId ->
            setOnChecked(checkedId)
        }

        binding.commitPersonal.setOnClickListener {
            if(binding.loginName.text.toString().trim().isEmpty()) {
                onToast("请输入姓名")
                return@setOnClickListener
            }
            onCommit()
        }

        val titleView = binding.topView.findViewById<TextView>(R.id.page_top_title)
        titleView.text = "为了提高检测报告的精准度,请您填写真实的性别与年龄~"
    }

    private fun setOnChecked(checkedId: Int) {
        if (checkedId == R.id.sex_man) {
            sex = 1
            binding.sexMan.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_radio)
            binding.sexWoman.background = ContextCompat.getDrawable(requireContext(), R.color.transparent)
            binding.sexMan.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.sexWoman.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
        } else {
            sex = 2
            binding.sexMan.background = ContextCompat.getDrawable(requireContext(), R.color.transparent)
            binding.sexWoman.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_radio)
            binding.sexMan.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.sexWoman.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
        }
    }


    private fun setPicker(picker: NumberPicker, min: Int, max: Int) {
        picker.minValue = min
        picker.maxValue = max
        picker.wrapSelectorWheel = true
    }

    private fun onPickerBg(picker: NumberPicker) {
        val count = picker.childCount
        for (i in 0 until count) {
            val child = picker.getChildAt(i)
            if (child is EditText) {
                child.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_picker)
                child.textSize = 20f
                child.setTypeface(child.typeface, Typeface.BOLD)
            }
        }
    }

    private fun onCommit() {
        val name = binding.loginName.text.toString().trim()
//        Log.i("PersonalFragment", "height: $height, weight: $weight, age: $year, sex: $sex, name: $name")
        Client.main.updateUserInfo(PersonalBody(name, year, sex, height, weight))
            .enqueue(object : RFCallback<Any>() {
                override fun onResult(res: Any) {
                    var user = Gson().fromJson(Consts.USER_JSON, UserEntity::class.java)
                    Consts.USER_NAME = name
                    user.realName = name
                    user.sn = Consts.DEVICE_SN
                    Consts.USER_JSON = Gson().toJson(res)

                    gotoCheck()
                }

                override fun onFail(code: Int, e: Throwable) {
                    Log.i("Login TAG", "onFail: $code ${e.message}")
                    onToast("提交失败失败")
                }
            })
    }

    private fun gotoCheck() {
        onToast("提交成功，开始测量")
        navigateTo(R.id.personal_to_pulse, null)
//        findNavController().popBackStack()
    }

    private fun onToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}