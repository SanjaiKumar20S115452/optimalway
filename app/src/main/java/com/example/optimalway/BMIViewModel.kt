package com.example.optimalway

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BMIViewModel: ViewModel() {

    private var _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(BMIState())

    fun onEvent(event: BMIEvents) {
        when(event){
            is BMIEvents.OnCalculateBMI -> {
                if(state.height.isBlank() || state.weight.isBlank()) {
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Fill in all the fields to calculate BMI!"))
                    return
                }
                var result = state.weight.toDouble() / (state.height.toDouble() * state.height.toDouble())
                state = state.copy(
                    result = result
                )

            }
            is BMIEvents.OnHeightChange -> {

                state = state.copy(
                    height = event.height
                )
            }
            is BMIEvents.OnWeightChange -> {

                state = state.copy(
                    weight = event.weight
                )
            }

            is BMIEvents.OnAgeChange -> {
                state = state.copy(
                    age = event.age
                )
            }
            is BMIEvents.OnBmiChange -> {
                state = state.copy(
                    bmi = event.bmi
                )
            }
            is BMIEvents.OnCalculateBodyFat -> {
                if(state.age.isBlank() || state.bmi.isBlank()) {
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Fill in all the fields to calculate body fat !"))
                    return
                }
                val result = (1.20 * state.bmi.toDouble()) + (0.23 * state.age.toDouble()) - 5.4
                state = state.copy(
                    bodyFat = result
                )
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}

data class BMIState(
    val weight: String = "",
    val height: String = "",
    val result: Double = 0.0,
    val age: String = "",
    val bmi: String = "",
    val bodyFat: Double = 0.0
)

sealed class BMIEvents {
    data class OnWeightChange(val weight: String): BMIEvents()
    data class OnHeightChange(val height: String): BMIEvents()
    object OnCalculateBMI: BMIEvents()
    data class OnAgeChange(val age: String): BMIEvents()
    data class OnBmiChange(val bmi: String): BMIEvents()
    object OnCalculateBodyFat: BMIEvents()
}

sealed class UIEvent {
    data class ShowSnackBar(val message: String): UIEvent()
}