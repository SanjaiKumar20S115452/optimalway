package com.example.optimalway

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import java.text.DecimalFormat

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BMIScreen(viewModel: BMIViewModel) {
    val state = viewModel.state

    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest { event ->
            when(event) {
                is UIEvent.ShowSnackBar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Calculate your BMI !", textAlign = TextAlign.Center, fontSize = 20.sp)
                TextField(value = state.height, onValueChange = {
                    viewModel.onEvent(BMIEvents.OnHeightChange(it))
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp
                        ),
                    placeholder = {
                        Text(text = "Height in Meters Ex.(1.67)")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = state.weight, onValueChange = {
                    viewModel.onEvent(BMIEvents.OnWeightChange(it))
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp
                        ),
                    placeholder = {
                        Text(text = "Weight in KG")
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {
                    viewModel.onEvent(BMIEvents.OnCalculateBMI)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Calculate BMI")
                }
                val decimalFormat = DecimalFormat("#.##")
                val result = when(state.result) {
                    in 18.5..25.5 -> "BMI: ${decimalFormat.format(state.result)}/ Your in good shape💪!"
                    in 12.0..16.0 -> "BMI: ${decimalFormat.format(state.result)}/Severe Thinness🚶🏼"
                    in 25.0..30.0 -> "BMI: ${decimalFormat.format(state.result)}/ Your Overweight🎅🏻!"
                    in 30.0..35.0 -> "BMI: ${decimalFormat.format(state.result)}/ Obese class I😓!"
                    in 35.0..40.0 -> "BMI: ${decimalFormat.format(state.result)}/ Obese class II❌!"
                    in 40.0..100.0 -> "BMI: ${decimalFormat.format(state.result)}/ Obese class III🤯!"
                    else -> "BMI: ${decimalFormat.format(state.result)}"
                }
                if(state.result != 0.0) {
                    Text(
                        text = result,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        fontSize = 15.sp
                    )
                }
            }


            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Calculate your body fat %",
                    fontSize = 20.sp
                )
                TextField(
                    value = state.age,
                    onValueChange = {
                        viewModel.onEvent(BMIEvents.OnAgeChange(it))
                    },
                    placeholder = {
                        Text(text = "Age")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = state.bmi,
                    onValueChange = {
                        viewModel.onEvent(BMIEvents.OnBmiChange(it))
                    },
                    placeholder = {
                        Text(text = "BMI")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                val decimalFormat = DecimalFormat("#.##")
                val bodyFat = when(state.bodyFat) {
                    in 14.0..20.0 -> "Body fat: ${decimalFormat.format(state.bodyFat)} / Perfect Shape !"
                    in 20.0..25.0 -> "Body fat: ${decimalFormat.format(state.bodyFat)} / Normal fit !"
                    in 25.0..30.0 -> "Body fat: ${decimalFormat.format(state.bodyFat)} / Acceptable !"
                    in 30.0..35.0 -> "Body fat: ${decimalFormat.format(state.bodyFat)} / Obesity Max !"
                    else -> ""
                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(onClick = {
                    viewModel.onEvent(BMIEvents.OnCalculateBodyFat)
                }) {
                    Text(text = "Calculate")
                }
                if(state.bodyFat != 0.0) {
                    Text(text = bodyFat)
                }
            }

        }
    }
}