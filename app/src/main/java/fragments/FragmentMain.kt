package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import adapter.PagerAdapter
import com.example.level4.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class FragmentMain : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        initial()
        return binding.root
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