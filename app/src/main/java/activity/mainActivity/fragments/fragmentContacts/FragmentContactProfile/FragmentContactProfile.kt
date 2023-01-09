package activity.mainActivity.fragments.fragmentContacts.FragmentContactProfile

import activity.mainActivity.fragments.FragmentMainArgs
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import base.BaseFragment
import com.example.level4.databinding.FragmentContactProfileBinding
import extension.addImage

class FragmentContactProfile : BaseFragment<FragmentContactProfileBinding>(FragmentContactProfileBinding::inflate) {

    private val args: FragmentMainArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addData()

        binding.btnArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun addData() {
        val user = args.user
        binding.apply {
            ivAddContactPhoto.addImage(user.photo)
            tvName.text = user.name
            tvCareer.text = user.career
            tvUserAddress.text = user.homeAddress
        }
    }
}

