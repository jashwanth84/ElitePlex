package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ElitePlex", appName)
  }

  @Test
  fun `verify ElitePlex repository catalog and anime module`() {
    val allContent = com.example.data.repository.ElitePlexRepository.getAllContent()
    assertTrue(allContent.isNotEmpty())

    val anime = com.example.data.repository.ElitePlexRepository.getAnimeContent()
    assertTrue(anime.isNotEmpty())

    val hero = com.example.data.repository.ElitePlexRepository.getHeroItems()
    assertTrue(hero.isNotEmpty())

    val animeProviders = com.example.data.repository.ElitePlexRepository.getAnimeProviders()
    assertTrue(animeProviders.any { it.providerId == "freakyaniki" })
    assertFalse(animeProviders.any { it.providerId == "moviebox" })

    val episodes = com.example.data.repository.ElitePlexRepository.getEpisodesForSeason("solo_leveling", 1)
    assertTrue(episodes.isNotEmpty())
    assertEquals(24, episodes.first().durationMinutes)

    val sources = com.example.data.repository.ElitePlexRepository.getAvailableStreamSources("solo_leveling", episodes.first().id)
    assertTrue(sources.any { it.providerName.contains("FreakyAniki") })
  }

  @Test
  fun `verify SQLite storage watchlist persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val storage = com.example.data.local.ElitePlexStorage(context)

    storage.addToWatchlist("drishyam_conclusion")
    assertTrue(storage.isInWatchlist("drishyam_conclusion"))

    storage.removeFromWatchlist("drishyam_conclusion")
    assertFalse(storage.isInWatchlist("drishyam_conclusion"))
    storage.close()
  }
}
