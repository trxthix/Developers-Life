package com.github.trxthix.developerslife.ui.common

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.trxthix.developerslife.R
import com.github.trxthix.developerslife.data.PostCategory
import com.github.trxthix.developerslife.ui.category.PostCategoryFragment
import com.github.trxthix.developerslife.ui.rand.RandPostFragment

class FragmentAdapter(
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

    override fun getPageTitle(position: Int) = context.getString(getTitleResId(position))

    private fun getTitleResId(position: Int): Int {
        return when (position) {
            POSITION_RAND -> R.string.title_rand
            POSITION_LATEST -> R.string.title_latest
            POSITION_TOP -> R.string.title_top
            else -> 0
        }
    }

    companion object {
        const val FRAG_COUNT = 3

        const val POSITION_RAND = 0
        const val POSITION_LATEST = 1
        const val POSITION_TOP = 2
    }
}