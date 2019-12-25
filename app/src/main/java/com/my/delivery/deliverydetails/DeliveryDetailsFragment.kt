package com.my.delivery.deliverydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.my.delivery.R
import com.my.delivery.deliverylist.model.Delivery
import com.my.delivery.deliverylist.storage.AppDatabase
import com.my.delivery.utils.UtilityHelper
import kotlinx.android.synthetic.main.fragment_delivery_details.*

class DeliveryDetailsFragment : Fragment() {

    lateinit var delivery:Delivery

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_delivery_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //enabling the home back button
        val activity = activity as AppCompatActivity?
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        delivery = if (savedInstanceState == null) {
            // getting delivery from arguments
            val args = arguments
            args?.getSerializable(getString(R.string.delivery)) as Delivery
        } else {
            savedInstanceState.getSerializable(getString(R.string.delivery)) as Delivery
        }

        initView(delivery)
        manageFavoriteClickListener(delivery)
    }

    /**
     * initializing the views
     *
     * @param:delivery - populate the data on UI based on [delivery]
     */
    private fun initView(delivery: Delivery) {
        senderTv.text = delivery.route.start
        receiverTv.text = delivery.route.end

        // using glide library to load image from url - goodsPicture
        context?.let { Glide.with(it).load(delivery.goodsPicture).into(ivGoods) }

        // formatting the price
        tvFees.text = UtilityHelper().formattedPrice(delivery)

        // check the favorite variable and add the star for favorite
        if (delivery.isFavorite) {
            tvAddToFavorite.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.star_big_on,0)
        }
    }

    /**
     * managing the click listener on favorite button
     *
     * @param:delivery - populate the data on UI based on [delivery]
     */
    private fun manageFavoriteClickListener(delivery: Delivery) {
        // click handling on favorite tab
        llAddToFavorite.setOnClickListener {
            // initializing the room database
            val appDatabase = AppDatabase.getDatabase(requireContext())
            val deliveryDao = appDatabase.deliveryDao()
            val data = deliveryDao.getDelivery(delivery.id)

            // check if delivery is favorite or not
            if (data.isFavorite) {
                data.isFavorite = false
                delivery.isFavorite = false
                tvAddToFavorite.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.star_big_off,0)
            } else {
                data.isFavorite = true
                delivery.isFavorite = true
                tvAddToFavorite.setCompoundDrawablesWithIntrinsicBounds(0,0,android.R.drawable.star_big_on,0)
            }

            // save the updated favorite delivery item
            deliveryDao.updateDelivery(data.isFavorite, data.id)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(getString(R.string.delivery), delivery)
    }
}