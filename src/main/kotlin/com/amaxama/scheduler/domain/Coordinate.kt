package com.amaxama.scheduler.domain

data class Coordinate (


    val x: Int = 0,

    val y: Int = 0,

    var isBarren: Boolean = false,

    var isVisited: Boolean = false

) {
    constructor(x: Int, y: Int) : this(x, y, false, false)
}