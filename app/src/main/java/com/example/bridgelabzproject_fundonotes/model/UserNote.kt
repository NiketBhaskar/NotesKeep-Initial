package com.example.bridgelabzproject_fundonotes.model

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import com.google.type.DateTime
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserNote(var userId : String = "",var noteId : String = "", var noteTitle : String = "", var noteSubtitle : String = "", var noteContent : String = "", var noteLabel : String = "", var timeStamp : String = ""): Parcelable {
}
