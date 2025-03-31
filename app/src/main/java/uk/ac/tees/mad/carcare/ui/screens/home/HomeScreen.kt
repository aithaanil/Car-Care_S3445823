package uk.ac.tees.mad.carcare.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.carcare.ui.navigation.SubGraph


// Mock data for demonstration
data class Appointment(
    val serviceType: String,
    val dateTime: String,
    val serviceCenter: String,
    val status: String
)

val mockAppointments = listOf(
    Appointment("Oil Change", "2024-06-15 10:00", "Center A", "Confirmed"),
    Appointment("Tire Rotation", "2024-06-22 14:30", "Center B", "Pending"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    openAndPopUp: (Any, Any) -> Unit,
    viewmodel: HomeScreenViewModel = koinViewModel<HomeScreenViewModel>()
) {
    val user by viewmodel.userData.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "CarCare", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {
                        //open(SubGraph.ProfileGraph)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Greeting
            Text(
                text = "Hello, ${user?.userDetails?.displayName ?: "User"}!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Upcoming Appointment Section
            Text(
                text = "Your Next Appointment",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            AppointmentList(appointments = mockAppointments,
                open = {}
            )

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CarCareButton(
                    modifier = Modifier.weight(1f),
                    label = "Book a Service",
                    onClick = {
                        //open(SubGraph.BookingGraph)
                    }
                )

                CarCareButton(
                    modifier = Modifier.weight(1f),
                    label = "View History",
                    onClick = {
                        //open(SubGraph.HistoryGraph)
                    }
                )
            }
            CarCareButton(
                label = "Manage Profile",
                onClick = {
                    //open(SubGraph.ProfileGraph)
                }
            )

            // Logout Button
            Button(onClick = {
                viewmodel.logOut()
                openAndPopUp(SubGraph.AuthGraph, SubGraph.HomeGraph)
            }) {
                Text("Log Out")
            }
        }
    }
}

@Composable
fun AppointmentList(appointments: List<Appointment>, open: (Any) -> Unit) {
    LazyColumn {
        items(appointments) { appointment ->
            AppointmentCard(appointment = appointment, open = open)
        }
    }
}

@Composable
fun AppointmentCard(appointment: Appointment, open: (Any) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            //.clickable { open(SubGraph.BookingGraph) } // Navigate to booking details
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = appointment.serviceType, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = "Date/Time: ${appointment.dateTime}")
            Text(text = "Center: ${appointment.serviceCenter}")
            Text(text = "Status: ${appointment.status}")
        }
    }
}

@Composable
fun CarCareButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(text = label)
    }
}