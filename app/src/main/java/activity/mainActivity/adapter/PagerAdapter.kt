package activity.mainActivity.adapter

import activity.mainActivity.fragments.fragmentMyProfile.FragmentMyProfile
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import activity.mainActivity.fragments.fragmentContacts.FragmentContacts

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentMyProfile()
            else -> FragmentContacts()
        }
    }

}