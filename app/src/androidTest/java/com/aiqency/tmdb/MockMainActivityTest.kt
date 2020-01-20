package com.aiqency.tmdb

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aiqency.tmdb.activity.detail.DetailActivity
import com.aiqency.tmdb.activity.main.presenter.MovieRepositoryInterface
import com.aiqency.tmdb.activity.main.view.MainActivity
import com.aiqency.tmdb.activity.main.view.adapters.MoviesAdapter
import com.aiqency.tmdb.realms.realmTransaction
import org.junit.AfterClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify


/**
 * This test suite mock the remote data fetching
 * @see [com.aiqency.tmdb.app.TestApp]
 */
@RunWith(AndroidJUnit4::class)
class MockMainActivityTest {

    @get:Rule
    val activityRule = IntentsTestRule(MainActivity::class.java)

    private val activity by lazy { activityRule.activity }

    companion object {

        @AfterClass
        @JvmStatic fun teardown() {
            realmTransaction { deleteAll() }
        }
        
    }

    /**
     * Ensure the mocking of the [MovieRepositoryInterface] is set correctly
     */
    @Test
    fun testMock(){
        verify(activity.presenter.repo, atLeastOnce()).getLiveMovies()
    }


    /**
     * Test the DetailActivity redirection
     */
    @Test
    fun testItemClickRedirection(){
        onView(withId(R.id.rv))
            .perform(actionOnItemAtPosition<MoviesAdapter.ViewHolder>(2, click()))
        // Confirm that the detail activity get launched
        intended(hasComponent(DetailActivity::class.java.name ?: ""))
    }

}