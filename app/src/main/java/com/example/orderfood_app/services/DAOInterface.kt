package com.example.orderfood_app.services

import com.example.orderfood_app.models.Notification

interface DAOInterface<T> {
    fun create(item: T): Notification<T>
    fun getById(id: String): T
    fun getAll()
    fun update(id: String, item: T): Notification<T>
    fun delete(id: String): Notification<T>
    fun findBy(attribute: String, value: String): ArrayList<T>
}


