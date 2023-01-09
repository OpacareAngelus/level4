package activity.mainActivity.fragments.fragmentContacts

import activity.mainActivity.adapter.RecyclerAdapterUserContacts
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import base.BaseFragment
import com.example.level4.R
import com.example.level4.databinding.FragmentContactsBinding
import com.google.android.material.snackbar.Snackbar
import activity.mainActivity.data.model.User
import activity.mainActivity.fragments.fragmentContacts.fragmentAddContact.FragmentAddContact
import activity.mainActivity.fragments.FragmentMainDirections
import util.RecyclerAdapterLookUp
import util.Selector
import util.UserListController

class FragmentContacts : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate),
    UserListController, Selector {

    private val sharedViewModel: FragmentContactsUsersViewModel by activityViewModels()
    private val usersAdapter by lazy {
        RecyclerAdapterUserContacts(userListController = this, selector = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAddContact.setOnClickListener {
            FragmentAddContact().apply {
                show(
                    this@FragmentContacts.requireActivity().supportFragmentManager,
                    context?.getString(R.string.tag_add_contact),
                )
            }
        }

        binding.btnDeleteSelectedContacts.apply {
            setOnClickListener {
                sharedViewModel.deleteSelectedUsers()
                usersAdapter.getTracker()?.clearSelection()
                visibility = View.INVISIBLE
            }
        }

        val recyclerView: RecyclerView = binding.rvContacts.apply { adapter = usersAdapter }
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.itemAnimator = null

        val tracker: SelectionTracker<Long> = SelectionTracker.Builder(
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
        sharedViewModel.add(user)
    }

    override fun onDeleteUser(user: User) {
        sharedViewModel.deleteUser(user)
    }

    override fun onOpenContactProfile(user: User) {
        findNavController().navigate(
            FragmentMainDirections.actionFragmentMainToFragmentContactProfile(user)
        )
    }

    override fun changeVisibility(selected: Boolean) {
        when (selected) {
            true -> binding.btnDeleteSelectedContacts.visibility = View.VISIBLE
            else -> binding.btnDeleteSelectedContacts.visibility = View.INVISIBLE
        }
    }

    private fun setObservers() {
        sharedViewModel.userListLiveData.observe(viewLifecycleOwner) {
            usersAdapter.submitList(it)
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
            val user = sharedViewModel.getUser(viewHolder.absoluteAdapterPosition) as User
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    onDeleteUser(user)
                    val messageText = String.format(getString(R.string.has_been_deleted), user.name)
                    val delMessage = Snackbar.make(
                        viewHolder.itemView,
                        messageText,
                        Snackbar.LENGTH_LONG
                    )
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
