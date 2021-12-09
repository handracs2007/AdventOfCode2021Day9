import java.io.File

fun isLowest(map: List<List<Int>>, y: Int, x: Int, curr: Int): Boolean {
    // Check left, if the number to the left is lower, skip checking.
    if ((x - 1) >= 0 && map[y][x - 1] <= curr) {
        return false
    }

    // Check right, if the number to the right is lower, skip checking.
    if ((x + 1) < map[y].size && map[y][x + 1] <= curr) {
        return false
    }

    // Check top, if the number to the top is lower, skip checking.
    if ((y - 1) >= 0 && map[y - 1][x] <= curr) {
        return false
    }

    // Check bottom, if the number to the bottom is lower, skip checking.
    if ((y + 1) < map.size && map[y + 1][x] <= curr) {
        return false
    }

    return true
}

fun solvePart1(map: List<List<Int>>) {
    var riskLevel = 0

    for (y in map.indices) {
        for (x in map[y].indices) {
            val curr = map[y][x]

            if (isLowest(map, y, x, curr)) {
                riskLevel += curr + 1
            }
        }
    }

    println("PART 1 ANSWER")
    println(riskLevel)
}

fun solvePart2(map: List<List<Int>>) {
    val sizes = mutableListOf<Int>()

    fun deepCopy(ori: List<List<Int>>): List<MutableList<Int>> {
        val copy = mutableListOf<MutableList<Int>>()
        for (row in ori) {
            val cols = mutableListOf<Int>()
            row.forEach(cols::add)
            copy.add(cols)
        }

        return copy
    }

    fun checkSurroundings(map: List<MutableList<Int>>, y: Int, x: Int, curr: Int): Int {
        // Mark it as part of the basin
        map[y][x] = -1

        // Check left, if the number to the left is lower, skip checking.
        if ((x - 1) >= 0 && map[y][x - 1] > curr && map[y][x - 1] != 9) {
            checkSurroundings(map, y, x - 1, map[y][x])
        }

        // Check right, if the number to the right is lower, skip checking.
        if ((x + 1) < map[y].size && map[y][x + 1] > curr && map[y][x + 1] != 9) {
            checkSurroundings(map, y, x + 1, map[y][x])
        }

        // Check top, if the number to the top is lower, skip checking.
        if ((y - 1) >= 0 && map[y - 1][x] > curr && map[y - 1][x] != 9) {
            checkSurroundings(map, y - 1, x, map[y][x])
        }

        // Check bottom, if the number to the bottom is lower, skip checking.
        if ((y + 1) < map.size && map[y + 1][x] > curr && map[y + 1][x] != 9) {
            checkSurroundings(map, y + 1, x, map[y][x])
        }

        // Let's consolidate
        return map.flatten().count { it == -1 }
    }

    for (y in map.indices) {
        for (x in map[y].indices) {
            val curr = map[y][x]

            if (isLowest(map, y, x, curr)) {
                val size = checkSurroundings(deepCopy(map), y, x, curr)
                sizes.add(size)
            }
        }
    }

    println("PART 2 ANSWER")
    println(sizes.sortedDescending().take(3).reduce { acc, i -> acc * i })
}

fun main() {
    val map = File("input.txt").readLines().map { row -> row.map { it.digitToInt() }.toMutableList() }

    solvePart1(map)
    solvePart2(map)
}