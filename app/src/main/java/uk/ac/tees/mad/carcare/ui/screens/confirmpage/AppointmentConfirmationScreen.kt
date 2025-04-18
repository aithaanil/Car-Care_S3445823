package uk.ac.tees.mad.carcare.ui.screens.confirmpage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.carcare.R
import uk.ac.tees.mad.carcare.model.dataclass.firebase.CarCareAppointment
import uk.ac.tees.mad.carcare.model.dataclass.firebase.FirestoreResult
import uk.ac.tees.mad.carcare.ui.util.CarCareScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AppointmentConfirmationScreen(
    modifier: Modifier = Modifier,
    popUp: () -> Unit,
    appointmentId: String,
    viewModel: AppointmentConfirmationScreenViewModel = koinViewModel<AppointmentConfirmationScreenViewModel>()
) {
    val bookingState by viewModel.bookingState.collectAsStateWithLifecycle()

    val cal = Calendar.getInstance()
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    LaunchedEffect(Unit) {
        viewModel.getAppointmentDetails(appointmentId)
    }
    CarCareScreen(
        title = stringResource(id = R.string.appointment_confirmation),
        popUp = popUp,
        showBackArrow = true
    ) { innerModifier ->
        LazyColumn(
            modifier = innerModifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Text(
                    text = "Your appointment has been confirmed!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Appointment Details",
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        when (val state = bookingState) {
                            is FirestoreResult.Error -> {
                                Text(
                                    text = "Error",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(text = state.exception.message.toString())
                            }

                            is FirestoreResult.Loading -> {
                                Text(
                                    text = "Loading",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Please wait...",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .height(48.dp)
                                        .width(48.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }

                            is FirestoreResult.Success -> {
                                val appointment = state.data as CarCareAppointment
                                val date = Date(appointment.appointmentDate.toLong())
                                cal.set(Calendar.DATE, date.date)
                                val formattedDate = formatter.format(date)

                                Text(
                                    text = "User ID: ${appointment.userId}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Appointment ID: ${appointment.firestoreId}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Service: ${appointment.service}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Service Center: ${appointment.serviceCenter}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Appointment Date: ${formattedDate}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Appointment Time: ${appointment.appointmentTime}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .background(color = MaterialTheme.colorScheme.surfaceContainerHigh),
                                    contentAlignment = Alignment.Center
                                ) {
                                    fun decodeBase64ToBitmap(base64String: String): Bitmap? {
                                        return try {
                                            val decodedByteArray = Base64.decode(base64String, Base64.DEFAULT)
                                            BitmapFactory.decodeByteArray(
                                                decodedByteArray,
                                                0,
                                                decodedByteArray.size
                                            )
                                        } catch (e: Exception) {
                                            // Handle decoding errors (e.g., invalid Base64 string)
                                            null
                                        }
                                    }
                                    val bitmap = if(appointment.carImage.isNotEmpty()){
                                        decodeBase64ToBitmap(appointment.carImage)
                                    }else{
                                        null
                                    }
                                    if (bitmap != null) {
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            modifier = Modifier.fillMaxSize(),
                                            contentDescription = "Car Image",
                                        )
                                    } else {
                                        // Photo placeholder
                                        Icon(
                                            imageVector = Icons.Default.Image,
                                            contentDescription = "No Image",
                                            modifier = Modifier.size(40.dp),
                                            tint = Color.Gray
                                        )
                                    }
                                }
                                if (appointment.appointmentServiceDescription.isNotEmpty() || appointment.appointmentServiceDescription.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Appointment Service Description: \n${appointment.appointmentServiceDescription}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}