package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.level4.databinding.FragmentContactProfileBinding
import extension.addImage

class FragmentContactProfile : Fragment() {

    private lateinit var binding: FragmentContactProfileBinding
    private val args: FragmentMainArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentContactProfileBinding.inflate(layoutInflater)

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

