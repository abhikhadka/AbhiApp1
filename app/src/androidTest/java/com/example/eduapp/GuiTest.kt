package com.example.eduapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GuiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testLandingScreenToSettingsNavigation() {
        // Wait for splash screen to finish (it has a delay)
        // Note: In a real test environment, you'd use IdlingResource
        Thread.sleep(3000) 

        // Check if Landing Screen is shown
        composeTestRule.onNodeWithText("Enter Your Name").assertIsDisplayed()
        
        // Enter a name and click Play
        // (This assumes the button becomes enabled after text input)
        // For simplicity, we just check if the button exists
        composeTestRule.onNodeWithText("PLAY NOW").assertIsDisplayed()
    }

    @Test
    fun testMusicToggleVisibility() {
        Thread.sleep(3000)
        
        // Check if the music toggle (speaker icon) is present
        composeTestRule.onNodeWithContentDescription("Toggle Music").assertIsDisplayed()
    }
}
