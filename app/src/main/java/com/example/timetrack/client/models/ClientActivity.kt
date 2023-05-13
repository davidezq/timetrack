package com.example.timetrack.client.models

import java.util.Date

data class ClientActivity(
    var activityDescription:String,
    var activityDuration: Long,
    var activityName: String,
    var doneAt: Date,
    var id:String
)