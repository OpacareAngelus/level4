package activity.mainActivity.fragments.fragmentContacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import activity.mainActivity.data.UserData
import activity.mainActivity.data.model.User

class FragmentContactsUsersViewModel : ViewModel() {

    private val _userListLiveData = MutableLiveData<List<User>>()
    val userListLiveData: LiveData<List<User>> = _userListLiveData

    init {
        _userListLiveData.value = UserData.getUsers()
    }

    fun getUser(position: Int) = _userListLiveData.value?.get(position)

    fun deleteUser(user: User) {
        _userListLiveData.value = _userListLiveData.value?.minus(user) ?: emptyList()
    }

    fun add(user: User?) {
        if (user != null) _userListLiveData.value =
            _userListLiveData.value?.plus(user) ?: listOf(user)
    }

    fun deleteSelectedUsers(){
        for (n in userListLiveData.value?.size!! - 1 downTo 0) {
            if (getUser(n)?.isSelected == true) {
                deleteUser(getUser(n)!!)
            }
        }
    }
}