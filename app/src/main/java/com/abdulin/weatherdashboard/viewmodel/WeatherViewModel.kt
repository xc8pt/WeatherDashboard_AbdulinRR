package com.abdulin.weatherdashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulin.weatherdashboard.data.WeatherData
import com.abdulin.weatherdashboard.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _weatherState = MutableStateFlow(WeatherData())
    val weatherState: StateFlow<WeatherData> = _weatherState.asStateFlow()
    init {
        loadWeatherData()
    }
    fun loadWeatherData() {
        viewModelScope.launch {
            // Start
            _weatherState.value = _weatherState.value.copy(
                isLoading = true,
                error = null
            )
            try {
                // Загружаем по очереди
                val temperature = repository.fetchTemperature()
                _weatherState.value = _weatherState.value.copy(temperature = temperature)
                val humidity = repository.fetchHumidity()
                _weatherState.value = _weatherState.value.copy(humidity = humidity)
                val windSpeed = repository.fetchTemperature()
                _weatherState.value = _weatherState.value.copy(windSpeed = windSpeed)
            } catch (e: Exception) {
                // Stop
                _weatherState.value = _weatherState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки: ${e.message}"
                )
            }
        }
    }
}