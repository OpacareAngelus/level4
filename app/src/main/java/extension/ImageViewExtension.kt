package extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import data.model.User

fun ImageView.addImage(user: User) {
    Glide.with(this.context)
        .load(user.photo)
        .into(this)
}

fun ImageView.addImage(photo: String?) {
    Glide.with(this.context)
        .load(photo)
        .into(this)
}