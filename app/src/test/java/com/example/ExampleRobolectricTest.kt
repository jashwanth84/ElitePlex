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

  @Test
  fun `verify master catalog artwork URLs are valid HTTPS`() {
    val allContent = com.example.data.repository.ElitePlexRepository.getAllContent()
    assertTrue(allContent.isNotEmpty())

    for (item in allContent) {
      assertTrue("Poster URL for ${item.title} should start with https://", item.posterUrl.startsWith("https://"))
      assertTrue("Backdrop URL for ${item.title} should start with https://", item.backdropUrl.startsWith("https://"))
      assertFalse("Poster URL should not be blank", item.posterUrl.isBlank())
    }
  }

  @Test
  fun `verify ElitePlexApiClient normalization logic`() {
    val testJson = org.json.JSONObject().apply {
      put("id", "test_123")
      put("title", "Test Movie")
      put("poster", "https://image.tmdb.org/t/p/w500/sample.jpg")
      put("backdrop", "/sample_backdrop.jpg")
      put("rating", 8.4)
      put("year", "2024")
      put("type", "movie")
    }

    val item = com.example.data.remote.ElitePlexApiClient.parseNormalizedContentItem(testJson)
    org.junit.Assert.assertNotNull(item)
    assertEquals("test_123", item!!.id)
    assertEquals("Test Movie", item.title)
    assertEquals("https://image.tmdb.org/t/p/w500/sample.jpg", item.posterUrl)
    assertEquals("https://image.tmdb.org/t/p/w1280/sample_backdrop.jpg", item.backdropUrl)
    assertEquals(8.4, item.rating, 0.01)
    assertEquals(2024, item.year)
  }
}
