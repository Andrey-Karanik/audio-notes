package com.andreykaranik.audionotes

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _isRecordButtonGone: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRecordButtonGone: StateFlow<Boolean> = _isRecordButtonGone.asStateFlow()

    private val _isRecording: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    fun startRecording() {
        _isRecording.value = true
    }

    fun stopRecording() {
        _isRecording.value = false
    }

    fun showRecordButton() {
        _isRecordButtonGone.value = false
    }

    fun hideRecordButton() {
        _isRecordButtonGone.value = true
    }

}