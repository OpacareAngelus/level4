package util

import adapter.RecyclerAdapterUserContacts
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapterLookUp(private val recyclerView: RecyclerView)
    : ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent)
            : ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if(view != null) {
            return (recyclerView.getChildViewHolder(view) as RecyclerAdapterUserContacts.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}
