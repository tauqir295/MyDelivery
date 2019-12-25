package com.my.delivery.deliverylist.storage

import androidx.room.*
import com.my.delivery.deliverylist.model.Delivery

/**
 * Data Access Object for defining database interactions
 */
@Dao
interface DeliveryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg delivery: Delivery)

    @Query("SELECT * FROM delivery where id == :inputId ")
    fun getDelivery(inputId: String): Delivery

    @Query("SELECT * FROM delivery")
    fun getAllDeliveries(): Array<Delivery>

    @Query("Update delivery set isFavorite =  :isFavorite where id == :inputId ")
    fun updateDelivery(isFavorite: Boolean, inputId : String): Int

    @Query("DELETE FROM delivery")
    fun resetDatabaseTable()

    @Query("SELECT COUNT(id) FROM delivery")
    fun getTotalItemCountInDB(): Int
}