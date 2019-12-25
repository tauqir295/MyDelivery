package com.my.delivery.deliverylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.delivery.AppViewModelFactory
import com.my.delivery.BuildConfig
import com.my.delivery.R
import com.my.delivery.datasource.AppRepository
import com.my.delivery.datasource.local.AppLocalDataSource
import com.my.delivery.datasource.remote.AppRemoteDataSource
import com.my.delivery.deliverydetails.DeliveryDetailsFragment
import com.my.delivery.deliverylist.adapter.DeliveryRecyclerAdapter
import com.my.delivery.deliverylist.adapter.LoadMoreListener
import com.my.delivery.deliverylist.model.Delivery
import com.my.delivery.deliverylist.storage.AppDatabase
import com.my.delivery.deliverylist.storage.DeliveryDao
import com.my.delivery.general.manager.ConfigurationManager
import com.my.delivery.ui.MainActivity
import com.my.delivery.utils.Logger
import com.my.delivery.utils.replaceWithNextFragment
import kotlinx.android.synthetic.main.fragment_main.*

class DeliveryListFragment : Fragment(), DeliveryRecyclerAdapter.OnRecyclerItemClickListener, LoadMoreListener.OnLoadMoreListener {
    companion object {
        fun newInstance() = DeliveryListFragment()
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var viewModel: DeliveryListViewModel
    private lateinit var adapter: DeliveryRecyclerAdapter
    private var offset = 0
    private var limit = 0
    private var total = 0
    private var appDatabase:AppDatabase? = null
    private var deliveryDao:DeliveryDao? = null
    private lateinit var loadMoreListener : LoadMoreListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getDatabase(requireContext())
        deliveryDao = appDatabase?.deliveryDao()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    /**
     * initiating recyclerView and view model, setting up pagination
     */
    private fun initView() {
        activity?.title = getString(R.string.delivery_list_title)
        //disabling the home back button
        val activity = activity as AppCompatActivity?
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        adapter = DeliveryRecyclerAdapter()
        adapter.setOnItemClickListener(this)
        layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        rvDeliveries.layoutManager = layoutManager
        rvDeliveries.adapter = adapter

        setupPagination()   // setting up on scroll listener for pagination
        setupViewModels()   // setting up view model
        setupAPICall()      // setting up api call if needed on app install
    }

    /**
     * setting up scroll listener on recycler view
     */
    private fun setupPagination() {
        loadMoreListener = LoadMoreListener(layoutManager, this)
        loadMoreListener.setLoaded()
        rvDeliveries.addOnScrollListener(loadMoreListener)
    }

    /**
     * initiating the view models and API calls
     */
    private fun setupViewModels() {
        activity?.let {

            // on the basis of build variants - use stub.json response from environment_stub/assets folder
            // OR use API call to fetch the data
            val repository = AppRepository(
                when (BuildConfig.FLAVOR_environment) {
                    getString(R.string.stub) -> AppLocalDataSource()
                    else -> AppRemoteDataSource()
                }
            )

            //using view model to create object for view model
            val factory = AppViewModelFactory.getInstance(repository)
            viewModel = ViewModelProviders.of(it, factory).get(DeliveryListViewModel::class.java)

            // fetching data from API
            viewModel.deliveryList.observe(viewLifecycleOwner,
                Observer { deliveries ->
                    if (deliveries.size > 0){ // checking the size of deliveries received from API
                        ConfigurationManager.instance.featureConfiguration?.next?.let {
                            total += it
                            this.offset = total + 1
                            this.limit = it
                        }

                        setProgressAndScrollVariablesStatus() // changing the status of the variables and progressbar
                        saveInDB(deliveries) // saving deliveries in database
                    } else {
                        setProgressAndScrollVariablesStatus()
                    }

                })
        }

        // observing the value change to error bean class - using view model
        handleAPIFail()
    }

    /**
     * initiating the API calls if needed and populating the adapter views
     */
    private fun setupAPICall() {
        ConfigurationManager.instance.featureConfiguration?.limit?.let { limit = it }
        val itemsInDb = deliveryDao?.getTotalItemCountInDB() //checking items in db

        // if item in db is 0 then make api call from 0 to 20
        // else if item in db more than 0 then make api call from item count in db to next 20
        itemsInDb?.let {
            if (itemsInDb.compareTo(0) == 0){
                ConfigurationManager.instance.featureConfiguration?.offset?.let { offset = it }
                // getting delivery list from API
                viewModel.getDeliveryList(offset, limit, requireActivity().applicationContext)

            } else {

                if (MainActivity.IS_FROM_DELIVERY_DETAIL_SCREEN) {
                    deliveryDao?.getAllDeliveries()?.let { it ->
                        adapter.addAllItem(it.toList().toMutableList()) // passing deliveries to adapter
                    }
                }

                // needed to set offset on load more after scrolling
                total = itemsInDb
                offset = total + 1

                // disabling the load progress as there are items already in adapter now
                deliverListProgressBar.visibility = View.GONE
            }
        }
    }

    /**
     * Saving the list in room database and passing the same to adapter
     */
    private fun saveInDB(deliveries: ArrayList<Delivery>) {
        if (!MainActivity.IS_FROM_DELIVERY_DETAIL_SCREEN) {
            deliveries.let {
                deliveries.forEach { delivery ->
                    deliveryDao?.insertAll(delivery) // database insertion
                }
                deliveryDao?.getAllDeliveries()?.let { it ->
                    adapter.addAllItem(it.toList().toMutableList()) // passing deliveries to adapter
                }
            }
        }

    }

    /**
     * handling click on item click and navigating to delivery detail screen
     * by passing the delivery item using bundle
     */
    override fun onItemClick(
        item: View?,
        delivery: Delivery
    ) {
        Logger.d("DeliveryListFragment", "item clicked")

        val bundle = Bundle()
        bundle.putSerializable(getString(R.string.delivery), delivery)

        // using extension method for navigation to DeliveryDetailsFragment
        replaceWithNextFragment(
            this.id, fragmentManager, DeliveryDetailsFragment(), bundle, true
        )
    }

    /**
     * listening to scroll scroll and fetching more data from API
     *
     */
    override fun onLoadMore() {
        MainActivity.IS_FROM_DELIVERY_DETAIL_SCREEN = false
        adapter.addProgressView() // setting the progress bar visible
        activity?.let {
            viewModel.getDeliveryList(offset, limit, it.applicationContext)
        }
    }

    /**
     * handing API failed case by showing alert dialog
     */
    private fun handleAPIFail() {
        viewModel.errorBean.observe(this,
            Observer {
                setProgressAndScrollVariablesStatus()

                if (!MainActivity.IS_FROM_DELIVERY_DETAIL_SCREEN) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.api_failed))
                        .setMessage(getString(R.string.something_went_wrong))
                        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }
            })
    }

    /**
     * disabling the progress view and loaded variable
     */
    private fun setProgressAndScrollVariablesStatus() {
        deliverListProgressBar.visibility = View.GONE
        adapter.removeProgressView()
        loadMoreListener.setLoaded()
    }
}