package data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: Int,
    val photo: String,
    val name: String,
    val career: String,
    val email: String,
    val phone: String,
    val homeAddress: String,
    val dataBirth: String,
    var isSelected: Boolean
) : Parcelable


