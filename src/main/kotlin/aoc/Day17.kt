package aoc

import java.io.File

interface Coordinates<T> {
    val neighbours: Set<T>
}

interface State<T : Coordinates<T>> {
    val coordinates: Set<T>
    val numberOfActiveCells: Int
    fun isActive(coordinates: T): Boolean
}

data class TesseractCoordinates(val x: Int, val y: Int, val z: Int, val w: Int) : Coordinates<TesseractCoordinates> {
    override val neighbours
        get() = (w - 1..w + 1).flatMap { w ->
            (z - 1..z + 1).flatMap { z ->
                (y - 1..y + 1).flatMap { y ->
                    (x - 1..x + 1).map { x ->
                        TesseractCoordinates(x, y, z, w)
                    }
                }
            }
        }.filter { it != this }.toSet()
}

data class CubeCoordinates(val x: Int, val y: Int, val z: Int) : Coordinates<CubeCoordinates> {
    override val neighbours
        get() = (z - 1..z + 1).flatMap { z ->
            (y - 1..y + 1).flatMap { y ->
                (x - 1..x + 1).map { x ->
                    CubeCoordinates(x, y, z)
                }
            }
        }.filter { it != this }.toSet()
}

class TesseractState(private val activatedCubes: Set<TesseractCoordinates>) : State<TesseractCoordinates> {

    private val wCoordinates
        get() = activatedCubes.map(TesseractCoordinates::w)
            .minOf { it } - 1..activatedCubes.map(TesseractCoordinates::w)
            .maxOf { it } + 1

    private val zCoordinates
        get() = activatedCubes.map(TesseractCoordinates::z)
            .minOf { it } - 1..activatedCubes.map(TesseractCoordinates::z)
            .maxOf { it } + 1

    private val yCoordinates
        get() = activatedCubes.map(TesseractCoordinates::y)
            .minOf { it } - 1..activatedCubes.map(TesseractCoordinates::y)
            .maxOf { it } + 1

    private val xCoordinates
        get() = activatedCubes.map(TesseractCoordinates::x)
            .minOf { it } - 1..activatedCubes.map(TesseractCoordinates::x)
            .maxOf { it } + 1

    override val coordinates
        get() =
            wCoordinates.flatMap { w ->
                zCoordinates.flatMap { z ->
                    yCoordinates.flatMap { y ->
                        xCoordinates.map { x ->
                            TesseractCoordinates(x, y, z, w)
                        }
                    }
                }
            }.toSet()

    override val numberOfActiveCells
        get() = activatedCubes.size

    override fun isActive(coordinates: TesseractCoordinates): Boolean {
        return activatedCubes.contains(coordinates)
    }


}

class CubeState(private val activatedCubes: Set<CubeCoordinates>) : State<CubeCoordinates> {

    private val zCoordinates
        get() = activatedCubes.map(CubeCoordinates::z).minOf { it } - 1..activatedCubes.map(CubeCoordinates::z)
            .maxOf { it } + 1

    private val yCoordinates
        get() = activatedCubes.map(CubeCoordinates::y).minOf { it } - 1..activatedCubes.map(CubeCoordinates::y)
            .maxOf { it } + 1

    private val xCoordinates
        get() = activatedCubes.map(CubeCoordinates::x).minOf { it } - 1..activatedCubes.map(CubeCoordinates::x)
            .maxOf { it } + 1

    override val coordinates
        get() =
            zCoordinates.flatMap { z ->
                yCoordinates.flatMap { y ->
                    xCoordinates.map { x ->
                        CubeCoordinates(x, y, z)
                    }
                }
            }.toSet()

    override val numberOfActiveCells
        get() = activatedCubes.size

    override fun isActive(coordinates: CubeCoordinates): Boolean {
        return activatedCubes.contains(coordinates)
    }

}

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input17.txt").readLines()

    // Part 1
    val initialState = CubeState(
        activatedCubes = lines.indices.flatMap { y ->
            lines[y].indices
                .filter { x ->
                    lines[y][x] == '#'
                }.map { x ->
                    CubeCoordinates(x, y, 0)
                }
        }.toSet()
    )

    var currentState = initialState
    repeat(6) {
        currentState = createNewState(currentState)
    }
    println(currentState.numberOfActiveCells)

    // Part 2
    val initialState2 = TesseractState(
        activatedCubes = lines.indices.flatMap { y ->
            lines[y].indices
                .filter { x ->
                    lines[y][x] == '#'
                }.map { x ->
                    TesseractCoordinates(x, y, 0, 0)
                }
        }.toSet()
    )

    var currentState2 = initialState2
    repeat(6) {
        currentState2 = createNewState(currentState2)
    }
    println(currentState2.numberOfActiveCells)

}

fun createNewState(inputState: TesseractState) =
    TesseractState(
        activatedCubes = inputState.coordinates
            .filter {
                if (inputState.isActive(it)) {
                    !deactivate(it, inputState)
                } else {
                    activate(it, inputState)
                }
            }.toSet()
    )

fun createNewState(inputState: CubeState) =
    CubeState(
        activatedCubes = inputState.coordinates
            .filter {
                if (inputState.isActive(it)) {
                    !deactivate(it, inputState)
                } else {
                    activate(it, inputState)
                }
            }.toSet()
    )


fun <T : Coordinates<T>> deactivate(coordinates: T, state: State<T>): Boolean =
    countActiveNeighbours(coordinates, state).let { it < 2 || it > 3 }

fun <T : Coordinates<T>> activate(coordinates: T, state: State<T>): Boolean =
    countActiveNeighbours(coordinates, state) == 3

fun <T : Coordinates<T>> countActiveNeighbours(coordinates: T, state: State<T>) =
    coordinates.neighbours.filter { state.isActive(it) }.count()



