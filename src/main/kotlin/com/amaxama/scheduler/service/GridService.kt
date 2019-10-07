package com.amaxama.scheduler.service

class GridService {

    fun test() {
        val rows = listOf(
                "0000",
                "0800",
                "0880"
        )
        Grid(rows).fill()
    }

    class Grid(rows: List<String>) {
        private val grid = IntArray(12)
        private var filled = false

        init {
            require(rows.size == 3 && rows.all { it.length == 4 }) {
                "Grid must be 3 x 4"
            }
            for (i in 0..2) {
                for (j in 0..3 ) {

                    grid[3 * i + j] = rows[i][j] - '0'
                }
            }
        }

        fun fill() {
            println("Initial grid:\n\n$this")
            placeStudent(0)
            println(if (filled) "Filled grid:\n\n$this" else "Unfillable!")
        }

        private fun placeStudent(spot: Int) {
            if (filled) return
            if (spot == 12) {
                filled = true
                return
            }
            if (grid[spot] > 0) {
                placeStudent(spot + 1)
                return
            }
            println(spot)
            when(spot) {
                in 0..3 -> fillRow(spot, mutableListOf(1,2,3,4))
                in 4..7 -> fillRow(spot, mutableListOf(1,4,5))
                in 8..11 -> fillRow(spot, mutableListOf(5,6))
            }
        }

        private fun fillRow(spot: Int, students: MutableList<Int>) {
            println(students)
            for (n in students) {
                if (checkValidity(n, spot % 4, spot / 3)) {
                    grid[spot] = n
                    placeStudent(spot + 1)
                    if (filled) return
                    println("spot: $spot n: $n ")
                    grid[spot] = 0
                }
            }
        }

        private fun checkValidity(v: Int, x: Int, y: Int): Boolean {
            for (i in 0..2) {
                if (grid[y * 3 + i] == v || grid[i * 4 + x] == v) return false
            }
            return true
        }

        override fun toString(): String {
            val sb = StringBuilder()
            for (i in 0..2) {
                for (j in 0..3) {
                    sb.append(grid[i * 3 + j])
                    sb.append(" ")
                    if (j == 3 || j == 5) sb.append("| ")
                }
                sb.append("\n")
                if (i == 3 || i == 5) sb.append("------+\n")
            }
            return sb.toString()
        }
    }
}

