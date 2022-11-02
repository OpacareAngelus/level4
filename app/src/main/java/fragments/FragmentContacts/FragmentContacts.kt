package fragments.FragmentContacts

import adapter.RecyclerAdapterUserContacts
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
import com.example.level4.R
import com.example.level4.databinding.FragmentContactsBinding
import com.google.android.material.snackbar.Snackbar
import data.model.User
import fragments.FragmentAddContact
import util.RecyclerAdapterLookUp
import util.Selector
import util.UserListController

class FragmentContacts : Fragment(), UserListController, Selector {

    private lateinit var binding: FragmentContactsBinding

    private val viewModel: FragmentContactsUsersViewModel by viewModels()

    private val usersAdapter by lazy {
        RecyclerAdapterUserContacts(userListController = this, selector = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater)

        binding.tvAddContact.setOnClickListener {
            FragmentAddContact(userListController = this).apply {
                show(
                    this@FragmentContacts.requireActivity().supportFragmentManager,
                    "AddContact",
                )
            }
        }

        binding.btnDeleteSelectedContacts.apply {
            setOnClickListener {
                for (n in viewModel.size()!! - 1 downTo 0) {
                    println(viewModel.getUser(n)?.selected)
                    if (viewModel.getUser(n)?.selected == true) {
                        onDeleteUser(viewModel.getUser(n)!!)
                    }
                }
                usersAdapter.getTracker()?.clearSelection()
                visibility = View.INVISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rvContacts.apply { adapter = usersAdapter }
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.itemAnimator = null

        val tracker: SelectionTracker<Long>? = SelectionTracker.Builder(
            getString(R.string.selection_id),
            recyclerView,
            StableIdKeyProvider(recyclerView),
            RecyclerAdapterLookUp(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        usersAdapter.setTracker(tracker)

        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView)

        setObservers()
    }

    override fun onContactAdd(user: User) {
        viewModel.add(user)
    }

    override fun onDeleteUser(user: User) {
        viewModel.deleteUser(user)
    }

    override fun onOpenContactProfile(user: User) {
        findNavController().navigate(
            R.id.action_fragmentMain_to_fragmentContactProfile,
            bundleOf(
                Pair(getString(R.string.argument_contacts_to_contact_profile), user)
            )
        )
    }

    override fun changeVisibility(selected: Boolean) {
        when (selected) {
            true -> binding.btnDeleteSelectedContacts.visibility = View.VISIBLE
            else -> binding.btnDeleteSelectedContacts.visibility = View.INVISIBLE
        }
    }

    private fun setObservers() {
        viewModel.userListLiveData.observe(viewLifecycleOwner) {
            usersAdapter.submitList(it.toMutableList())
        }
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val user = viewModel.getUser(viewHolder.absoluteAdapterPosition) as User
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    val delMessage = Snackbar.make(
                        viewHolder.itemView,
                        "${user.name} has deleted.",
                        Snackbar.LENGTH_LONG
                    )
                    onDeleteUser(user)
                    delMessage.setAction(R.string.cancel) {
                        onContactAdd(user)
                    }
                    delMessage.show()
                }
                ItemTouchHelper.RIGHT -> {
                    onOpenContactProfile(user)
                }
            }
        }
    }
}
