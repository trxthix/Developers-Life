package com.github.trxthix.developerslife.ui.common

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.trxthix.developerslife.R
import com.github.trxthix.developerslife.data.PostCategory
import com.github.trxthix.developerslife.ui.category.PostCategoryFragment
import com.github.trxthix.developerslife.ui.rand.RandPostFragment
import kotlinx.android.synthetic.main.activity_main.*

private const val FRAG_COUNT = 3
private const val POSITION_RAND = 0
private const val POSITION_LATEST = 1
private const val POSITION_TOP = 2

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tabLayout.setupWithViewPager(viewPager)

        with(viewPager) {
            offscreenPageLimit = FRAG_COUNT
            adapter = PageAdapter(supportFragmentManager, context)
        }
    }
}

private class PageAdapter(
    fm: FragmentManager,
    private val context: Context
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            POSITION_RAND -> RandPostFragment.newInstance()
            POSITION_LATEST -> PostCategoryFragment.newInstance(PostCategory.LATEST)
            POSITION_TOP -> PostCategoryFragment.newInstance(PostCategory.TOP)
            else -> error("unexpected page position = $position")
        }
    }

    override fun getCount(): Int = FRAG_COUNT

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(getTitleResId(position))
    }

    private fun getTitleResId(position: Int): Int {
        return when (position) {
            POSITION_RAND -> R.string.title_rand
            POSITION_LATEST -> R.string.title_latest
            POSITION_TOP -> R.string.title_top
            else -> 0
        }
    }
}