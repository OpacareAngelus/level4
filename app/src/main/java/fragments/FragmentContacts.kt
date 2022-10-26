package fragments

import SimpleCallBack
import adapter.RecyclerAdapterUserContacts
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.level4.databinding.DialogAddContactBinding
import com.example.level4.databinding.FragmentContactsBinding
import model.User
import model.UsersViewModel
import util.RecyclerAdapterLookUp
import util.Selector
import util.UserListController

class FragmentContacts : Fragment(), UserListController, Selector {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var dialogBinding: DialogAddContactBinding
    private val viewModel: UsersViewModel by viewModels()
    private val usersAdapter by lazy {
        RecyclerAdapterUserContacts(
            userListController = this,
            findNavController(),
            selector = this
        )
    }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var contactPhoto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    contactPhoto = data?.data!!
                    dialogBinding.ivUserPhoto.setImageURI(data.data!!)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater)

        binding.tvAddContact.setOnClickListener {
            dialogCreate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        val recyclerView: RecyclerView = binding.rvContacts.apply {
            adapter = usersAdapter
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)

        val tracker: SelectionTracker<Long>? = SelectionTracker.Builder(
            "selection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            RecyclerAdapterLookUp(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        usersAdapter.setTracker(tracker)

        ItemTouchHelper(
            SimpleCallBack(
                viewModel,
                findNavController(),
                userListController = this
            ).simpleCallback
        ).attachToRecyclerView(recyclerView)

        binding.btnDeleteSelectedContacts.apply {
            setOnClickListener {
                for (n in viewModel.size()!! - 1 downTo 0) {
                    println(viewModel.getUser(n)?.selected)
                    if (viewModel.getUser(n)?.selected == true) {
                        onDeleteUser(n)
                    }
                }
                usersAdapter.getTracker()?.clearSelection()
                visibility = View.INVISIBLE
            }
        }
    }

    override fun onContactAdd(user: User) {
        usersAdapter.submitList(viewModel.userListLiveData.value.also {
            viewModel.add(user)
        }?.toMutableList())
    }

    override fun onDeleteUser(user: User) {
        usersAdapter.submitList(viewModel.userListLiveData.value.also {
            viewModel.deleteUser(user)
        }?.toMutableList())
    }

    override fun onDeleteUser(position: Int) {
        usersAdapter.submitList(viewModel.userListLiveData.value.also {
            viewModel.deleteUser(position)
        }?.toMutableList())
    }

    private fun setObservers() {
        viewModel.userListLiveData.observe(viewLifecycleOwner) {
            usersAdapter.submitList(viewModel.userListLiveData.value?.toMutableList())
        }
    }

    private fun dialogCreate(inflater: LayoutInflater) {
        val dialog = Dialog(inflater.context)
        dialogBinding = DialogAddContactBinding.inflate(inflater)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        setDialogListeners(dialog)
        dialog.show()
    }

    private fun setDialogListeners(dialog: Dialog) {
        dialogBinding.apply {
            imgBtnBackArrow.setOnClickListener {
                dialog.dismiss()
            }
            btnSaveContact.setOnClickListener {
                saveButtonAction(dialog)
            }
            ivAddPhoto.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }
        }
    }

    override fun changeVisibility(selected: Boolean) {
        when (selected) {
            true -> binding.btnDeleteSelectedContacts.visibility = View.VISIBLE
            else -> binding.btnDeleteSelectedContacts.visibility = View.INVISIBLE
        }
    }

//    private fun saveButtonListener(dialog: Dialog) {
//        if (dialogBinding.tietUsername.text.toString() != "" &&
//            dialogBinding.tietCareer.text.toString() != "" &&
//            dialogBinding.tietEmail.text.toString() != "" &&
//            dialogBinding.tietPhone.text.toString() != "" &&
//            dialogBinding.tietAddress.text.toString() != "" &&
//            dialogBinding.tietDataOfBirth.text.toString() != ""
//        ) {
//            saveButtonAction(dialog)
//        } else {
//            Toast.makeText(
//                dialog.context,
//                getString(R.string.error_wrong_user_data),
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }

    private fun saveButtonAction(dialog: Dialog) {
        usersAdapter.submitList(viewModel.userListLiveData.value.also {
            viewModel.add(
                User(
                    0,
                    contactPhoto.toString(),
                    dialogBinding.tietUsername.text.toString(),
                    dialogBinding.tietCareer.text.toString(),
                    dialogBinding.tietEmail.text.toString(),
                    dialogBinding.tietPhone.text.toString(),
                    dialogBinding.tietAddress.text.toString(),
                    dialogBinding.tietDataOfBirth.text.toString(),
                    false
                )
            )
        }?.toMutableList())
        dialog.dismiss()
    }
}
