package com.example.eventmanagement.repository

import com.example.eventmanagement.model.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class EventRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val eventsCollection = firestore.collection("events")

    fun getEvents(onSuccess: (List<Event>) -> Unit, onFailure: (Exception) -> Unit) {
        eventsCollection.get(Source.SERVER) // Force fresh fetch from server
            .addOnSuccessListener { snapshot ->
                val eventList = snapshot.documents.mapNotNull { it.toObject(Event::class.java) }
                onSuccess(eventList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun addEvent(event: Event, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val eventData = event.copy(id = "") // Firestore will auto-generate the ID
        eventsCollection.add(eventData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}
