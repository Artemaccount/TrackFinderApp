package com.example.myapplication.data.api

data class ApiState(
    var state: State = State.Success
)

enum class State {
    Loading, Error, Success
}
