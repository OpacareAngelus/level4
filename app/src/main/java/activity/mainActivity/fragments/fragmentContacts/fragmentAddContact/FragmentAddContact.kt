package activity.mainActivity.fragments.fragmentContacts.fragmentAddContact

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
import com.example.level4.R
import com.example.level4.databinding.DialogFragmentAddContactBinding
import activity.mainActivity.data.model.User
import activity.mainActivity.fragments.fragmentContacts.FragmentContactsUsersViewModel


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle(getString(R.string.tag_add_contact))
        with(binding) {
            btnAddPhoto.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }

            btnSaveContact.setOnClickListener {
                addUser()
                dismiss()
            }

            btnArrow.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun addUser() {
        with(binding) {
            sharedViewModel.add(
                User(
                    0,
                    contactPhoto.toString(),
                    etUsername.text.toString(),
                    etCareer.text.toString(),
                    etEmail.text.toString(),
                    etPhone.text.toString(),
                    etAddress.text.toString(),
                    etDataOfBirth.text.toString(),
                    false
                )
            )
        }
    }
}