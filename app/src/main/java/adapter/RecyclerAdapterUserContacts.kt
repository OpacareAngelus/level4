package adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.navigation.NavController
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.level4.R
import com.example.level4.databinding.RecyclerviewItemBinding
import com.google.android.material.snackbar.Snackbar
import extension.addImage
import fragments.FragmentContacts
import model.User
import util.DiffUtil
import util.UserListController

class RecyclerAdapterUserContacts(
    private val userListController: UserListController,
    private val navGraph: NavController,
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
        holder.bind()
    }

    private fun deleteUser(user: User, view: View) {
        userListController.onDeleteUser(user)
        val delMessage = Snackbar.make(view, "${user.name} has deleted.", Snackbar.LENGTH_LONG)
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
        fun bind() {
            RecyclerviewItemBinding.bind(itemView).run {
                tvName.text = currentList[absoluteAdapterPosition]?.name
                tvCareer.text = currentList[absoluteAdapterPosition]?.career
                currentList[absoluteAdapterPosition]?.let { ivUserPhoto.addImage(it) }
                currentList[absoluteAdapterPosition]?.id = absoluteAdapterPosition

                imgBtnTrashCan.setOnClickListener {
                    deleteUser(currentList[absoluteAdapterPosition]!!, itemView)
                }

                ThisRecyclerView.setOnClickListener {
                    navGraph.navigate(
                        R.id.action_fragmentMain_to_fragmentContactProfile,
                        bundleOf(
                            Pair("photo", currentList[absoluteAdapterPosition].photo),
                            Pair("name", tvName.text),
                            Pair("career", tvCareer.text),
                            Pair("address", tvHomeAddress.text)
                        )
                    )
                }

                startSelection(imgBtnTrashCan, ThisRecyclerView, ivUserPhoto, root, cbSelected)
            }
        }

        private fun startSelection(
            imgBtnTrashCan: ImageButton,
            ThisRecyclerView: ConstraintLayout,
            ivUserPhoto: ImageView,
            root: ConstraintLayout,
            cbSelected: CheckBox
        ) {
            if (tracker!!.isSelected(absoluteAdapterPosition.toLong())) {
                selector.changeVisibility(true)
                imgBtnTrashCan.visibility = View.INVISIBLE
                ThisRecyclerView.setBackgroundResource(R.drawable.frame_rounding_selected)
                ivUserPhoto.foreground = getDrawable(
                    root.context,
                    R.drawable.img_shaper_color_background_selected
                )
                val margin = root.context.resources.getDimension(R.dimen.margin_selection_item)
                cbSelected.visibility = View.VISIBLE
                cbSelected.isChecked = true
                ivUserPhoto.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    marginStart = margin.toInt()
                }
                currentList[absoluteAdapterPosition].selected = true
            } else {
                imgBtnTrashCan.visibility = View.VISIBLE
                ThisRecyclerView.setBackgroundResource(R.drawable.frame_rounding)
                ivUserPhoto.foreground = getDrawable(
                    root.context,
                    R.drawable.img_shaper_color_background
                )
                val margin = root.context.resources.getDimension(R.dimen.margin_extra_small)
                cbSelected.visibility = View.INVISIBLE
                cbSelected.isChecked = false
                ivUserPhoto.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    marginStart = margin.toInt()
                }
                currentList[absoluteAdapterPosition].selected = false
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

