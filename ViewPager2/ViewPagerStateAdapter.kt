import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerStateAdapter(
    private val screens: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(
    fragmentManager, lifecycle
) {
    override fun getItemCount(): Int = screens.size

    override fun createFragment(position: Int): Fragment = screens[position]
}
