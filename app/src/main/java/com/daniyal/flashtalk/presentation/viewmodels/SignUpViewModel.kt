package com.daniyal.flashtalk.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniyal.flashtalk.data.model.User
import com.daniyal.flashtalk.data.repository.FirebaseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()

    val loading: StateFlow<Boolean>
        get() = firebaseRepository.loading


    var name = mutableStateOf("")
    var nameError = mutableStateOf<String?>(null)
    var email = mutableStateOf("")
    var emailError = mutableStateOf<String?>(null)
    var password = mutableStateOf("")
    var passwordError = mutableStateOf<String?>(null)
    var confirmPassword = mutableStateOf("")
    var confirmPasswordError = mutableStateOf<String?>(null)
    var phoneNumber = mutableStateOf("")
    var phoneError = mutableStateOf<String?>(null)
    var bio = mutableStateOf("")
    var bioError = mutableStateOf<String?>(null)

    // Function to validate inputs
    private fun validateInputs(): Boolean {
        return when {
            name.value.isEmpty() && email.value.isEmpty() && password.value.isEmpty() && confirmPassword.value.isEmpty() && phoneNumber.value.isEmpty() && bio.value.isEmpty() -> {
                nameError.value = "Name cannot be empty"
                emailError.value = "Email cannot be empty"
                passwordError.value = "Password cannot be empty"
                confirmPasswordError.value = "Confirm password cannot be empty"
                phoneError.value = "Phone number cannot be empty"
                bioError.value = "Bio cannot be empty"
                false
            }

            name.value.isEmpty() -> {
                nameError.value = "Name cannot be empty"
                false
            }

            email.value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value)
                .matches() -> {
                emailError.value = "Invalid email address"
                false
            }

            password.value.length < 6 -> {
                passwordError.value = "Password must be at least 6 characters"
                false
            }

            password.value != confirmPassword.value -> {
                passwordError.value = "Passwords do not match"
                confirmPasswordError.value = "Passwords do not match"
                false
            }

            phoneNumber.value.isEmpty() -> {
                phoneError.value = "Phone number cannot be empty"
                false
            }

            bio.value.isEmpty() -> {
                bioError.value = "Bio cannot be empty"
                false
            }

            else -> {
                true
            }
        }
    }

    // Function to handle form submission
    fun submitForm(onSuccess: ()-> Unit) {
        viewModelScope.launch {
            firebaseRepository.setLoading(true)
            if (validateInputs()) {
                firebaseRepository.signup(name = name.value,
                    email = email.value,
                    password = password.value,
                    phoneNumber = phoneNumber.value,
                    bio = bio.value,
                    image = "https://picsum.photos/200",
                    onSuccess = {
                        clearValues()
                        onSuccess()
                    },
                    onError = {})
            }
            firebaseRepository.setLoading(false)
        }

    }

    private fun clearValues() {
        name.value = ""
        email.value = ""
        password.value = ""
        password.value = ""
        confirmPassword.value = ""
        phoneNumber.value = ""
        bio.value = ""
    }
}