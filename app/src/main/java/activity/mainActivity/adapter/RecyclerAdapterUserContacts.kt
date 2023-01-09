package activity.mainActivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isInvisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.level4.R
import com.example.level4.databinding.RecyclerviewItemBinding
import com.google.android.material.snackbar.Snackbar
import activity.mainActivity.data.model.User
import extension.addImage
import activity.mainActivity.fragments.fragmentContacts.FragmentContacts
import util.DiffUtil
import util.UserListController

class RecyclerAdapterUserContacts(
    private val userListController: UserListController,
    private val selector: FragmentContacts
) :
    ListAdapter<User, RecyclerAdapterUserContacts.ViewHolder>(DiffUtil) {

    private var tracker: SelectionTracker<Long>? = null

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }

    fun getTracker(): SelectionTracker<Long>? {
        return this.tracker
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    private fun deleteUser(user: User, view: View) {
        userListController.onDeleteUser(user)
        val messageText =
            String.format(view.context.getString(R.string.has_been_deleted), user.name)
        val delMessage = Snackbar.make(view, messageText, Snackbar.LENGTH_LONG)
        undoUserDeletion(user, delMessage)
        delMessage.show()
    }

    /**Method back to list of contacts deleted contact if user push "Cancel" on the Snackbar.*/
    private fun undoUserDeletion(user: User, delMessage: Snackbar) {
        delMessage.setAction(R.string.cancel) {
            userListController.onContactAdd(user)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            RecyclerviewItemBinding.bind(itemView).run {
                val user = currentList[position]
                tvName.text = user.name
                tvCareer.text = user.career
                ivRecyclerItemUserPhoto.addImage(user.photo)
                user.id = position

                btnTrashCan.setOnClickListener {
                    deleteUser(user, itemView)
                }
                containerItem.setOnClickListener {
                    userListController.onOpenContactProfile(user)
                }

                startSelection(
                    btnTrashCan,
                    containerItem,
                    ivRecyclerItemUserPhoto,
                    root,
                    cbSelected
                )
            }
        }

        private fun startSelection(
            btnTrashCan: AppCompatImageView,
            itemContactsRecyclerView: ConstraintLayout,
            ivRecyclerItemUserPhoto: AppCompatImageView,
            root: ConstraintLayout,
            cbSelected: CheckBox
        ) {
            cbSelected.isChecked = tracker?.isSelected(absoluteAdapterPosition.toLong()) == true
            cbSelected.isInvisible = tracker?.isSelected(absoluteAdapterPosition.toLong()) != true
            btnTrashCan.isInvisible = tracker?.isSelected(absoluteAdapterPosition.toLong()) == true

            if (tracker?.isSelected(absoluteAdapterPosition.toLong()) == true) {
                selector.changeVisibility(true)
                itemContactsRecyclerView.setBackgroundResource(R.drawable.frame_rounding_selected)
                ivRecyclerItemUserPhoto.foreground = getDrawable(
                    root.context,
                    R.drawable.shaper_color_background_selected
                )
                val margin = root.context.resources.getDimension(R.dimen.margin_selection_item)
                ivRecyclerItemUserPhoto.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    marginStart = margin.toInt()
                }
                currentList[absoluteAdapterPosition].isSelected = true
            } else {
                itemContactsRecyclerView.setBackgroundResource(R.drawable.frame_rounded)
                ivRecyclerItemUserPhoto.foreground = getDrawable(
                    root.context,
                    R.drawable.shaper_color_background
                )
                val margin = root.context.resources.getDimension(R.dimen.margin_extra_small)
                ivRecyclerItemUserPhoto.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    marginStart = margin.toInt()
                }
                currentList[absoluteAdapterPosition].isSelected = false
            }
            if (!tracker!!.hasSelection()) {
                selector.changeVisibility(false)
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {

                override fun getPosition(): Int = absoluteAdapterPosition

                override fun getSelectionKey(): Long = itemId

            }
    }
}

