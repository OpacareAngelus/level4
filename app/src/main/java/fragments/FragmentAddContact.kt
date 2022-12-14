package fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.level4.databinding.DialogFragmentAddContactBinding
import data.model.User
import fragments.fragmentContacts.FragmentContactsUsersViewModel


class FragmentAddContact : DialogFragment() {

    private lateinit var binding: DialogFragmentAddContactBinding
    private val sharedViewModel: FragmentContactsUsersViewModel by activityViewModels()
    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                contactPhoto = data?.data
                binding.ivUserPhoto.setImageURI(data?.data)
            }
        }
    private var contactPhoto: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentAddContactBinding.inflate(layoutInflater)
        dialog?.setTitle("AddContact")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPhoto.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }

        binding.btnSaveContact.setOnClickListener {
            addUser()
            dismiss()
        }

        binding.btnArrow.setOnClickListener {
            dismiss()
        }
    }

    private fun addUser() {
        sharedViewModel.add(
            User(
                0,
                contactPhoto.toString(),
                binding.etUsername.text.toString(),
                binding.etCareer.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPhone.text.toString(),
                binding.etAddress.text.toString(),
                binding.etDataOfBirth.text.toString(),
                false
            )
        )
    }
}