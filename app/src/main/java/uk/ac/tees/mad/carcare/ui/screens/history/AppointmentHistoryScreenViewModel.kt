package uk.ac.tees.mad.carcare.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.carcare.model.dataclass.firebase.AuthResult
import uk.ac.tees.mad.carcare.model.dataclass.firebase.CarCareAppointment
import uk.ac.tees.mad.carcare.model.dataclass.firebase.UserData
import uk.ac.tees.mad.carcare.model.dataclass.firebase.UserDetails
import uk.ac.tees.mad.carcare.model.repository.AuthRepository
import uk.ac.tees.mad.carcare.model.repository.CarCareAppointmentRepository
import uk.ac.tees.mad.carcare.model.repository.CarCareFirestoreRepository

class AppointmentHistoryScreenViewModel(
    private val authRepository: AuthRepository,
    private val carCareFirestoreRepository: CarCareFirestoreRepository,
    private val carCareAppointmentRepository: CarCareAppointmentRepository,
) : ViewModel() {
    private val _userDetails = MutableStateFlow<AuthResult<UserDetails>>(AuthResult.Loading)
    val userDetails: StateFlow<AuthResult<UserDetails>> = _userDetails.asStateFlow()

    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData.asStateFlow()

    private val _appointmentHistory = MutableStateFlow<List<CarCareAppointment>>(emptyList())
    val appointmentHistory: StateFlow<List<CarCareAppointment>> = _appointmentHistory.asStateFlow()

    init {
        fetchUserDetails()
        getAppointmentDataForUser(_userData.value.userId!!)
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

    fun getAppointmentDataForUser(userId: String) {
        viewModelScope.launch {
            carCareAppointmentRepository.getAllAppointmentDataForUser(userId)
                .collectLatest { appointmentList ->
                    _appointmentHistory.value = appointmentList
                }
        }
    }

    fun deleteAppointmentData(appointmentData: CarCareAppointment) {
        viewModelScope.launch {
            //soft delete
            carCareAppointmentRepository.updateAppointmentFromFirestore(
                appointmentData.copy(
                    isDeleted = true
                )
            )
        }
    }

}