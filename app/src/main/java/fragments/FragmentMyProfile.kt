package activity

import android.os.Bundle
import android.view.View
import base.BaseFragment
import com.example.level4.databinding.FragmentMyProfileBinding

class FragmentMyProfile : BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvMyProfileName.text = this.activity?.intent?.getStringExtra("name")
        super.onViewCreated(view, savedInstanceState)
    }
}