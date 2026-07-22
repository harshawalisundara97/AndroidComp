package com.androidcomp.app.features.buttons.playground

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaygroundViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `default state has enabled true and default label`() {
        val viewModel = PlaygroundViewModel()

        val state = viewModel.state.value

        assertThat(state.enabled).isTrue()
        assertThat(state.label).isEqualTo("Button")
    }

    @Test
    fun `setLabel updates state label`() {
        val viewModel = PlaygroundViewModel()

        viewModel.setLabel("Click me")

        assertThat(viewModel.state.value.label).isEqualTo("Click me")
    }

    @Test
    fun `setEnabled updates state enabled flag`() {
        val viewModel = PlaygroundViewModel()

        viewModel.setEnabled(false)

        assertThat(viewModel.state.value.enabled).isFalse()
    }
}
