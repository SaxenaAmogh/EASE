package com.example.eventmanagement.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.eventmanagement.model.Event
import com.example.eventmanagement.repository.EventRepository

class EventViewModel : ViewModel() {

    private val repository = EventRepository()

    private val _events = mutableStateOf<List<Event>>(emptyList())
    val events: State<List<Event>> = _events

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun addNewEvent(event: Event) {
        _loading.value = true
        repository.addEvent(
            event = event,
            onSuccess = {
                _loading.value = false
                //fetchEvents() // Refresh events list after adding
            },
            onFailure = { exception ->
                _error.value = exception.message
                _loading.value = false
            }
        )
    }

    fun fetchEvents() {
        _loading.value = true
        repository.getEvents(
            onSuccess = { eventList ->
                _events.value = eventList // Updating UI state
                _loading.value = false
            },
            onFailure = { exception ->
                _error.value = exception.message
                _loading.value = false
            }
        )
    }
}
