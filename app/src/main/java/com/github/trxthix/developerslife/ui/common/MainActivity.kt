package com.github.trxthix.developerslife.ui.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.trxthix.developerslife.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tabLayout.setupWithViewPager(viewPager)

        with(viewPager) {
            offscreenPageLimit = FragmentAdapter.FRAG_COUNT
            adapter = FragmentAdapter(supportFragmentManager, context)
        }
    }
}

