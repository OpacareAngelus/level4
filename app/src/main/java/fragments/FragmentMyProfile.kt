package activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.level4.databinding.FragmentMyProfileBinding

class FragmentMyProfile : Fragment() {

    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentMyProfileBinding.inflate(layoutInflater)
        binding.tvName.text = this.activity?.intent?.getStringExtra("name")
        return binding.root
    }
}