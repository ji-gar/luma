package com.io.luma.uiscreen.carrercontanct

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.network.Resource
import com.io.luma.ui.theme.manropebold
import com.io.luma.viewmodel.PatientContactsViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CarersContactList(
    navController: NavController,
    viewModel: PatientContactsViewModel = viewModel()
) {
    val goldenYellow = Color(0xFFFFE68A)
    val skyBlue = Color(0xFFD6F6FF)

    val gradientBrush = Brush.linearGradient(
        listOf(
            goldenYellow,
            Color.White,
            Color.White,
            Color.White,
            skyBlue
        )
    )

    // Collect the state from ViewModel
    val patientContactsState by viewModel.patientContacts.collectAsState()

    // Fetch data when composable is first launched
    LaunchedEffect(Unit) {
        viewModel.getPatientContacts()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(16.sdp)
    ) {
        Column {
            HeaderSection()

            Spacer(modifier = Modifier.height(20.sdp))

            // Handle different states
            when (val state = patientContactsState) {
                is Resource.Loading -> {
                    // Show loading indicator
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    val patients = state.data ?: emptyList()

                    if (patients.isEmpty()) {
                        // Show empty state
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No contacts found",
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 16.ssp,
                                    fontFamily = manropebold
                                )
                            )
                        }
                    } else {
                        // Display patient contacts
                        LazyColumn {
                            itemsIndexed(patients) { index, patient ->
                                CarerItem(
                                    name = patient.known_as ?: patient.full_name,
                                    isMainContact = patient.is_primary_carer,
                                    phoneNumber = patient.phone_number,
                                    email = patient.email
                                )

                                if (index < patients.size - 1) {
                                    Divider(modifier = Modifier.padding(vertical = 8.sdp))
                                }
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    // Show error message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Failed to load contacts",
                                style = TextStyle(
                                    color = Color.Red,
                                    fontSize = 16.ssp,
                                    fontFamily = manropebold
                                )
                            )
                            Spacer(modifier = Modifier.height(8.sdp))
                            Text(
                                text = state.message ?: "Unknown error",
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 12.ssp
                                )
                            )
                            Spacer(modifier = Modifier.height(16.sdp))
                            Button(
                                onClick = { viewModel.getPatientContacts() }
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }

                null -> {
                    // Initial state - can show placeholder or nothing
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    com.io.luma.customcompose.height(30)
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.iv_smallleftarrow),
            contentDescription = "",
        )
        Text(
            "Amy Bishop",
            style = TextStyle(
                color = Color(0xff0D0C0C),
                fontSize = 20.ssp,
                fontFamily = manropebold,
                fontWeight = FontWeight.W700
            )
        )

        Image(
            painter = painterResource(R.drawable.iv_addplus),
            contentDescription = "",
        )
    }
}

@Composable
fun CarerItem(
    name: String,
    isMainContact: Boolean = false,
    phoneNumber: String? = null,
    email: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 15.ssp,
                    fontFamily = manropebold,
                    fontWeight = FontWeight.W700
                )

                if (isMainContact) {
                    Spacer(modifier = Modifier.width(6.sdp))

                    Icon(
                        painter = painterResource(id = R.drawable.iv_maincontent),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.width(6.sdp))

                Icon(
                    painter = painterResource(id = R.drawable.iv_chat),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }

            if (isMainContact) {
                Text(
                    text = "Main contact",
                    fontSize = 11.ssp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.sdp)
                )
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.iv_editor),
            contentDescription = "Edit",
            tint = Color.Unspecified
        )
    }
}

