package com.smartelctronics.smartlock.data.entitiy

data class Results(val results : List<Result>)

data class Result(val timestamp : Long, val value : Long)