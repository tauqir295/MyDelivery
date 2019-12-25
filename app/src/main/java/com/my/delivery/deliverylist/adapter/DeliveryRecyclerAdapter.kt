package com.my.delivery.deliverylist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.delivery.R
import com.my.delivery.deliverylist.model.Delivery
import com.my.delivery.deliverylist.model.Route
import com.my.delivery.deliverylist.model.Sender
import com.my.delivery.utils.UtilityHelper
import kotlinx.android.synthetic.main.deliver_item.view.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class DeliveryRecyclerAdapter :
    RecyclerView.Adapter<DeliveryRecyclerAdapter.BaseViewHolder>() {

    private var mDeliveryList: ArrayList<Delivery> = ArrayList()
    private var mOnItemClickListener: OnRecyclerItemClickListener? = null
    private lateinit var mRecyclerView: RecyclerView

    // construction method for initiating variables
    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_ITEM = 1
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        (return when {
            viewType === VIEW_TYPE_ITEM -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.deliver_item, parent, false)
                DeliveryRecyclerViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.load_more_progress, parent, false)
                ProgressViewHolder(v)
            }
        })
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindItems(mDeliveryList[position])
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(it, mDeliveryList[position])
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (this.mDeliveryList[position].id.isEmpty())
            VIEW_TYPE_LOADING
        else
            VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return mDeliveryList.size
    }

    /**
     * adding the deliveries received from to adapter
     * @param:list - used to populate the adapter items
     */
    fun addDeliveryList(list: MutableList<Delivery>) {
        val oldSize = this.mDeliveryList.size
        this.mDeliveryList.addAll(list)
        notifyItemRangeInserted(oldSize, this.mDeliveryList.size - oldSize)
    }

    /**
     * adding the deliveries received from to adapter
     * @param:list - used to populate the adapter items
     */
    fun addAllItem(list: MutableList<Delivery>) {
        this.mDeliveryList.clear()
        this.mDeliveryList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * initiating the listener
     *
     * @param:listener - use this to assign the class level listener
     */
    fun setOnItemClickListener(listener: OnRecyclerItemClickListener) {
        mOnItemClickListener = listener
    }

    /**
     * initiating the dummy view to show the progressbar below the list
     */
    fun addProgressView() {
        this.mDeliveryList.add(
            Delivery(
                "",
                "",
                "",
                "",
                "",
                Route("", ""),
                Sender("", "", ""), "", false
            )
        )

        mRecyclerView.post {
            notifyItemInserted(this.mDeliveryList.size - 1)
        }
    }

    /**
     * removing the empty view
     */
    fun removeProgressView() {
        if (mDeliveryList.size > 1) {
            this.mDeliveryList.removeAt(this.mDeliveryList.size - 1)
            notifyItemRemoved(this.mDeliveryList.size)
        }
    }

    class ProgressViewHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun bindItems(delivery: Delivery) {
        }
    }

    abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindItems(delivery: Delivery)
    }

    class DeliveryRecyclerViewHolder(itemView: View) : BaseViewHolder(itemView) {

        override fun bindItems(delivery: Delivery) {

            //using glide library to download image from url
            Glide.with(itemView.context).load(delivery.goodsPicture).into(itemView.ivGoodsPic)
            itemView.senderTv.text = delivery.route.start
            itemView.receiverTv.text = delivery.route.end

            itemView.feesTv.text = UtilityHelper().formattedPrice(delivery) // formatting the price

            // check if delivery is favorite or not and show/hide star icon
            if (delivery.isFavorite) {
                itemView.ivFavorite.visibility = View.VISIBLE
            } else {
                itemView.ivFavorite.visibility = View.GONE
            }
        }
    }

    /**
     * custom interface to pass the click event on item to the listener
     */
    interface OnRecyclerItemClickListener {
        fun onItemClick(
            item: View?,
            delivery: Delivery
        )
    }
}