package com.my.delivery.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.my.delivery.R
import com.my.delivery.deliverylist.DeliveryListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // navigating to delivery list fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DeliveryListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // navigating back when home icon is pressed
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        IS_FROM_DELIVERY_DETAIL_SCREEN = true
        super.onBackPressed()
    }

    companion object {
        // use this variable to manage loading of data in adapter from Database
        // if user has navigated back from detail screen then discard the changes observed on list
        // via model view.
        var IS_FROM_DELIVERY_DETAIL_SCREEN: Boolean = false
    }
}
