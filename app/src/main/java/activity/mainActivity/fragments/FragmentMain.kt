package activity.mainActivity.fragments

import android.os.Bundle
import android.view.View
import activity.mainActivity.adapter.PagerAdapter
import base.BaseFragment
import com.example.level4.R
import com.example.level4.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentMain : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialPager()
    }

    private fun initialPager() {
        binding.apply {
            vpMain.adapter = PagerAdapter(this@FragmentMain)
            TabLayoutMediator(tlMain, vpMain){
                    tab, pos ->
                when(pos){
                    0 -> tab.text = getString(R.string.my_profile)
                    1 -> tab.text = getString(R.string.my_contacts)
                }
            }.attach()
        }
    }

}