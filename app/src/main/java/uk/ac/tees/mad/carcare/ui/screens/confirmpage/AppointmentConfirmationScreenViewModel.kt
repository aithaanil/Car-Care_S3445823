package uk.ac.tees.mad.carcare.ui.screens.confirmpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.carcare.model.dataclass.firebase.AuthResult
import uk.ac.tees.mad.carcare.model.dataclass.firebase.FirestoreResult
import uk.ac.tees.mad.carcare.model.dataclass.firebase.UserData
import uk.ac.tees.mad.carcare.model.dataclass.firebase.UserDetails
import uk.ac.tees.mad.carcare.model.repository.AuthRepository
import uk.ac.tees.mad.carcare.model.repository.CarCareFirestoreRepository

class AppointmentConfirmationScreenViewModel(
    private val authRepository: AuthRepository,
    private val carCareFirestoreRepository: CarCareFirestoreRepository
) : ViewModel() {

    private val _userDetails = MutableStateFlow<AuthResult<UserDetails>>(AuthResult.Loading)
    val userDetails: StateFlow<AuthResult<UserDetails>> = _userDetails.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    private val _bookingState = MutableStateFlow<FirestoreResult<Any>>(FirestoreResult.Loading)
    val bookingState: StateFlow<FirestoreResult<Any>> = _bookingState.asStateFlow()

    init {
        fetchUserDetails()
    }

    fun fetchUserDetails() {
        viewModelScope.launch {
            authRepository.getCurrentUserDetails().collect { result ->
                _userDetails.value = result
                if (result is AuthResult.Success) {
                    _userData.update {
                        it.copy(
                            userDetails = result.data, userId = authRepository.getCurrentUserId()
                        )
                    }
                }
            }
        }
    }

    fun getAppointmentDetails(appointmentId: String) {
        viewModelScope.launch {
            carCareFirestoreRepository.getAppointmentDetails(
                _userData.value.userId!!, appointmentId
            ).collectLatest { firestoreResult ->
                when (firestoreResult) {
                    is FirestoreResult.Success -> {
                        //do something with the appointment details
                        _bookingState.value = FirestoreResult.Success(firestoreResult.data)
                    }

                    is FirestoreResult.Error -> {
                        //Show the Error
                        _bookingState.value = FirestoreResult.Error(firestoreResult.exception)
                    }

                    is FirestoreResult.Loading -> {
                        // Show Loading
                        _bookingState.value = FirestoreResult.Loading
                    }
                }
            }
        }
    }

}