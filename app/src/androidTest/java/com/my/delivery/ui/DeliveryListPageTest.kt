package com.my.delivery.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.my.delivery.R
import com.my.delivery.deliverylist.adapter.DeliveryRecyclerAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DeliveryListPageTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun appNavigationTest() {
        val viewGroup = onView(
            Matchers.allOf(
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.rvDeliveries),
                        childAtPosition(
                            IsInstanceOf.instanceOf(ViewGroup::class.java),
                            0
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        viewGroup.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val viewGroup2 = onView(
            Matchers.allOf(
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.rvDeliveries),
                        childAtPosition(
                            IsInstanceOf.instanceOf(ViewGroup::class.java),
                            0
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        viewGroup2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val constraintLayout = onView(
            Matchers.allOf(
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.rvDeliveries),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        constraintLayout.perform(ViewActions.click())

        val linearLayout = onView(
            Matchers.allOf(
                withId(R.id.llAddToFavorite),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.container),
                        0
                    ),
                    3
                ),
                ViewMatchers.isDisplayed()
            )
        )
        linearLayout.perform(ViewActions.click())

        val appCompatImageButton = onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("Navigate up"),
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageButton.perform(ViewActions.click())

    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    @Test
    fun scrollToLastItem() {
        val recyclerView = mActivityTestRule.activity.findViewById<RecyclerView>(R.id.rvDeliveries)
        val itemCount = recyclerView.adapter!!.itemCount
        onView(withId(R.id.rvDeliveries))
            .perform(scrollToPosition<DeliveryRecyclerAdapter.BaseViewHolder>(itemCount))
    }

}
