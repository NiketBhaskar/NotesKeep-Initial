package com.example.bridgelabzproject_fundonotes.model

interface Communicater {
    fun passData(position: Int, userId: String, noteId: String)
}