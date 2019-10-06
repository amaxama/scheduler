package com.amaxama.scheduler.service

import com.amaxama.scheduler.domain.Coordinate
import org.springframework.stereotype.Service
import java.util.ArrayList
import java.util.Stack

private const val TILE_SIZE = 1
private const val WIDTH = 400
private const val HEIGHT = 600

@Service
class Service {

    private val X_TILES = WIDTH / TILE_SIZE
    private val Y_TILES = HEIGHT / TILE_SIZE

    private val grid = Array(X_TILES) { Array(Y_TILES) { Coordinate() } }

    /**
     * Visit all coordinates in a fertile land space and find the area
     * @param grid
     * @param x
     * @param y
     * @return area (int) of the current fertile land space
     */
    fun floodFill(grid: Array<Array<Coordinate>>, x: Int, y: Int): Int {

        var count = 0 // Count of grid squares being filled/visited

        val stack = Stack<Coordinate>()
        stack.push(Coordinate(x, y))

        while (!stack.isEmpty()) {
            val c = stack.pop()

            //         If Coordinate c is unvisited, visit it, increase count by 1, and add neighbors to the stack;
            if (isCoordinateUnvisited(grid, c)) {
                count += 1
                if (c.y - 1 >= 0 && !grid[c.x][c.y - 1].isVisited) {
                    stack.push(Coordinate(c.x, c.y - 1))
                }
                if (c.y + 1 < Y_TILES && !grid[c.x][c.y + 1].isVisited) {
                    stack.push(Coordinate(c.x, c.y + 1))
                }
                if (c.x - 1 >= 0 && !grid[c.x - 1][c.y].isVisited) {
                    stack.push(Coordinate(c.x - 1, c.y))
                }
                if (c.x + 1 < X_TILES && !grid[c.x + 1][c.y].isVisited) {
                    stack.push(Coordinate(c.x + 1, c.y))
                }
            }

        }
        return count
    }




        fun getFertileLand(points: String): String {
//            val STDIN = arrayOf("0 292 399 307")
            val STDIN = arrayOf(points)
//        String[] STDIN = {"48 192 351 207", "48 392 351 407", "120 52 135 547", "260 52 275 547"};

            val fertileLand = findFertileLand(STDIN)
            println(fertileLand)

            return fertileLand

        }

        /**
         * Find total fertile land in grid based on String array of rectangle endpoints
         * @param rectangleCornersArray
         * @return String of all the fertile land area in square meters, sorted from smallest area to greatest, separated by a space
         */
        private fun findFertileLand(rectangleCornersArray: Array<String>): String {
            var fertileLand: MutableList<Int> = ArrayList()

            val barrenLandEndPoints = getBarrenLandCoordinates(rectangleCornersArray)

            val totalBarrenLand = ArrayList<Coordinate>()
            //        Fill barrenLand list
            for (rectangle in barrenLandEndPoints) {
                rectangle?.let { totalBarrenLand.addAll(findTotalBarrenLandForRectangle(it)) }
            }

            //        Loop through bounds of the grid filling a multidimentional array with each of the coordinate points in it
            for (y in 0 until Y_TILES) {
                for (x in 0 until X_TILES) {
                    val co = Coordinate(x, y)
                    //                for each coordinate, if it's present in the totalBarrenLand list, mark that coordinate as barren and visited
                    for (c in totalBarrenLand) {
                        if (c.x == x && c.y == y) {
                            co.isBarren = (true)
                            co.isVisited = (true)
                            break
                        } else {
                            co.isBarren = (false)
                        }
                    }
                    grid[x][y] = co
                }
            }

            fertileLand = checkForUnvisitedAreasAndCountFertileLand(fertileLand, 0, 0)

            fertileLand.sort()

            var STDOUT = ""

            if (fertileLand.isNotEmpty()) {
                for (land in fertileLand) {
                    STDOUT += "$land "
                }
            } else {
                STDOUT = "No fertile land available."
            }

            return STDOUT

        }

        /**
         * Get list of integer arrays for each string of rectangle endpoints
         * @param rectangleCornersArray
         * @return Integer array of rectangle endpoint arrays
         */
        private fun getBarrenLandCoordinates(rectangleCornersArray: Array<String>): ArrayList<IntArray?> {

            //        List of arrays of rectangle points
            val rectanglePoints = ArrayList<IntArray?>()
            //        For each rectangle coordinates, split into array of strings, convert to array of ints, add array to list of arrays of rectangles
            for (h in rectangleCornersArray.indices) {
                val strRectangleCorner = rectangleCornersArray[h].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                val intRectangleCorner: Array<Int?> = arrayOfNulls<Int?>(strRectangleCorner.size)
                val intRectangleCorner = IntArray(strRectangleCorner.size)
                for (i in strRectangleCorner.indices) {
                    intRectangleCorner[i] = Integer.parseInt(strRectangleCorner[i])
                }
                rectanglePoints.add(intRectangleCorner)
            }

            return rectanglePoints
        }

        /**
         * Populate a barren land rectangle with all the coordinates in that space
         * @param bounds
         * @return List of coordinates in barren land rectangle
         */
        private fun findTotalBarrenLandForRectangle(bounds: IntArray): List<Coordinate> {
//        Pass in 4 endpoints rather than hardcoding
            val allBarrenLandCoordinates = ArrayList<Coordinate>()

//        Loop through endpoints and create new coordinate for each coordinate within rectangle endpoints - then add to allBarrenLandCoordinates list

            for (i in bounds[0]..bounds[2]) {
                for (j in bounds[1]..bounds[3]) {
                    val coordinates = Coordinate(i, j)
                    allBarrenLandCoordinates.add(coordinates)
                }
            }
            return allBarrenLandCoordinates
        }

        /**
         * Check through grid, find first unvisited point, flood fill the fertile area directly connected to that point, and return the total area
         * @param land
         * @param xVal
         * @param yVal
         * @return List of area of each fertile land plot
         */
        private fun checkForUnvisitedAreasAndCountFertileLand(land: MutableList<Int>, xVal: Int, yVal: Int): MutableList<Int> {
            for (y in yVal until Y_TILES) {
                for (x in xVal until X_TILES) {
                    val tile = grid[x][y]
                    if (!tile.isVisited) {
                        val totalFertileArea = floodFill(grid, x, y)
                        land.add(totalFertileArea)
                        checkForUnvisitedAreasAndCountFertileLand(land, x, y)
                    }
                }
            }
            return land

        }

        /**
         * Check if coordinate has been visited already - if not, switch visited to true
         * @param grid
         * @param c
         * @return boolean value representing whether coordinate c has been visited or not
         */
        private fun isCoordinateUnvisited(grid: Array<Array<Coordinate>>, c: Coordinate): Boolean {

            //        Check that Coordinate c is not outside bounds of the grid
            if (c.x < 0 || c.y < 0 || c.x >= X_TILES || c.y >= Y_TILES) {
                return false
            }

            val coordinateToCheck = grid[c.x][c.y]

            if (coordinateToCheck.isVisited) {
                return false
            }

            coordinateToCheck.isVisited = true

            return true
        }
    }



