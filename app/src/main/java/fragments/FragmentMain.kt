package fragments

import android.os.Bundle
import android.view.View
import adapter.PagerAdapter
import base.BaseFragment
import com.example.level4.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentMain : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initial()
    }

    private fun initial() {
        binding.vpMain.adapter = PagerAdapter(this)
        binding.tlMain.tabIconTint = null
        TabLayoutMediator(binding.tlMain, binding.vpMain){
            tab, pos ->
            when(pos){
                0 -> tab.text = "My Profile"
                1 -> tab.text = "My Contacts"
            }
        }.attach()
    }
}