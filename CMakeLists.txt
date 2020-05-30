cmake_minimum_required(VERSION 3.17)

find_package(Java REQUIRED)
include(UseJava)

project(minesweeper3d NONE)

set(CMAKE_JAVA_COMPILER_FLAGS -source 14 -target 14)

add_jar(minesweeper3d
    SOURCES
    Test.java
    Cell.java
    CellHint.java
    CellStatus.java
    CellType.java
    Cube.java
    CubeStatus.java
    Dimension3D.java
    Game.java
    GameStatus.java
    MinesThrower.java
    ENTRY_POINT minesweeper3d.Test)