package com.lucasmontano.jakewharton

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.lucasmontano.jakewharton.view.activities.MainActivity
import org.hamcrest.Matchers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

  @Rule
  val activityActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java)

  @Before
  fun init() {
    activityActivityTestRule.activity.supportFragmentManager.beginTransaction()
  }

  @Test
  fun useAppContext() {
    val appContext = InstrumentationRegistry.getTargetContext()
    assertEquals("com.lucasmontano.jakewharton", appContext.packageName)
  }

  @Test
  fun checkRepoFragmentDisplayed() {
    Espresso.onView(ViewMatchers.withId(R.id.content))
        .check(ViewAssertions.matches((ViewMatchers.isDisplayed())))
  }

  @Test
  fun checkEmptyStateHidden() {
    Espresso.onView(ViewMatchers.withId(R.id.relativeLayout_empty))
        .check(ViewAssertions.matches((Matchers.not(ViewMatchers.isDisplayed()))))
  }
}
