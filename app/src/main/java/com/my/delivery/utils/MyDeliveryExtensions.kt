// JvmName defines the name of the target in the bytecode,
// which is also the name we can use when referring to the target from Java.
@file:JvmName("MyDeliveryUtils")

package com.my.delivery.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * using extension function to extend a class with new functionality.
 * Basically, an extension function is a member function of a class
 * that is defined outside the class.
 *
 * extension method for navigating to other fragment
 *
 */
fun Fragment.replaceWithNextFragment(
    containerID:Int,
    fragmentManager: FragmentManager?,
    fragment: Fragment,
    arguments: Bundle?,
    addToBackStack: Boolean = true
) {
    arguments.let {
        fragment.arguments = it
    }

    fragmentManager?.let {
        it.beginTransaction().apply {
            replace(containerID, fragment)
            if (addToBackStack) {
                addToBackStack(fragment::class.simpleName)
            }
            commit()
        }
    }

}