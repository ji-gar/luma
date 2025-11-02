package com.io.luma.uiscreen.someoneelsesignup

import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.skyblue
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaRegular
import com.io.luma.uiscreen.loginscreen.CountryOutlinedDropdown
import com.io.luma.utils.TokenManager
import com.io.luma.viewmodel.CarerRegisterViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SignupStep4(navController: NavController, carerViewModel: CarerRegisterViewModel) {

    var firstName = rememberSaveable { mutableStateOf("") }
    var lasttName = rememberSaveable { mutableStateOf("") }
    var email = rememberSaveable { mutableStateOf("") }
    var phone = rememberSaveable { mutableStateOf("") }
    var language = rememberSaveable { mutableStateOf("") }
    var countrycodes by remember { mutableStateOf("") }


    var context= LocalContext.current

//    var contactPick= rememberLauncherForActivityResult(contract = ActivityResultContracts.PickContact(), onResult = {
//       if (it!=null)
//       {
//           var cursor=context.contentResolver.query(it,null,null,null,null)
//
//           if (cursor?.moveToNext() ?: false)
//           {
//               val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
//               val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
//               val id = cursor.getString(idIndex)
//               val name = cursor.getString(nameIndex)
//               firstName.value=name
//
//               val phoneCursor = context.contentResolver.query(
//                   ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                   null,
//                   "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
//                   arrayOf(id),
//                   null
//               )
//
//               phoneCursor?.use { pCur ->
//                   if (pCur.moveToFirst()) {
//                       val phoneIndex =
//                           pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
//                       phone.value = pCur.getString(phoneIndex)
//                   }
//               }
//
//
//           }
//       }
//
//
//    })
    val contactPick = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact(),
        onResult = { uri ->
            if (uri != null) {
                val cursor = context.contentResolver.query(uri, null, null, null, null)

                if (cursor?.moveToNext() == true) {
                    val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                    val id = cursor.getString(idIndex)
                    val name = cursor.getString(nameIndex)
                    firstName.value = name

                    val phoneCursor = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                        arrayOf(id),
                        null
                    )

                    phoneCursor?.use { pCur ->
                        if (pCur.moveToFirst()) {
                            val phoneIndex =
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val rawNumber = pCur.getString(phoneIndex)

                            // Clean spaces or dashes
                            val cleanedNumber = rawNumber.replace("\\s|-".toRegex(), "")

                            // Parse number with libphonenumber
                            val phoneUtil = com.google.i18n.phonenumbers.PhoneNumberUtil.getInstance()
                            try {
                                val parsedNumber = phoneUtil.parse(cleanedNumber, null)
                                val countryCode = parsedNumber.countryCode
                                val nationalNumber = parsedNumber.nationalNumber

                                // ✅ Set values in your state
                                countrycodes = "+$countryCode"
                                phone.value = nationalNumber.toString()

                                Log.d("ContactPick", "Country: +$countryCode | Number: $nationalNumber")
                            } catch (e: Exception) {
                                e.printStackTrace()
                                // fallback if parsing fails
                                phone.value = cleanedNumber
                            }
                        }
                    }
                }
                cursor?.close()
            }
        }
    )


    var rememberPermissions= rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {


        if (it)
        {
            contactPick.launch(null)
        }
        else {

        }

    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                listOf(
                    goldenYellow,
                    Color.White,
                    Color.White,
                    Color.White,
                    skyblue
                )
            )
        ))
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)) {


            com.io.luma.customcompose.height(30)
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.sdp))
            {

                Icon(imageVector = Icons.Filled.KeyboardArrowLeft,
                    modifier = Modifier.clickable{
                        navController.popBackStack()
                    },
                    contentDescription = "Back")


                Image(painter = painterResource(R.drawable.lumalifewide),
                    contentDescription = "",
                    modifier = Modifier.height(33.sdp),

                    )

                Image(painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .height(16.sdp)
                        .clip(CircleShape))
            }

            com.io.luma.customcompose.height(30)


            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp).verticalScroll(
                    rememberScrollState()
                ).imePadding().windowInsetsPadding(WindowInsets.navigationBars)
            )
            {

                Text("Set up the profile for the\nperson you care for",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.ssp,
                        fontWeight = FontWeight.W700
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    textAlign = TextAlign.Center
                )

                com.io.luma.customcompose.height(20)

//                CustomOutlineButton(modifier = Modifier.fillMaxWidth(),
//                    text = "Fill from Contacts"){
//
//                    rememberPermissions.launch(android.Manifest.permission.READ_CONTACTS)
//
//                }
                Box(modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color(0xff93969B),shape = RoundedCornerShape(10.sdp)).clip(RoundedCornerShape(10.sdp)).clickable{
                    rememberPermissions.launch(android.Manifest.permission.READ_CONTACTS)
                }
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.sdp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Icon(painter = painterResource(R.drawable.contact),
                            contentDescription = "",)
                        Text("Fill from Contacts",style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontFamily = manropebold,
                            fontWeight = FontWeight.W700,
                            fontSize = 13.ssp
                        ))

                    }
                }


                com.io.luma.customcompose.height(20)
                rowHeader("Please Enter Name")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =firstName.value,
                    onValueChange = {
                        firstName.value=it
                    },
                    placeholder = {
                        Text("Enter your First Name",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp,
                                fontFamily = monospaceRegular
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )
                com.io.luma.customcompose.height(13)
                rowHeader("Your Email")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =email.value,
                    onValueChange = {
                        email.value=it
                    },
                    keyboardActions = KeyboardActions(onNext = {

                    }),
                    singleLine = true,
                    placeholder = {
                        Text("Enter Your Email",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp,
                                fontFamily = monospaceRegular
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )
                com.io.luma.customcompose.height(13)
                rowHeader("User Phone Number")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =phone.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onNext = {

                    }),
                    onValueChange = {
                        phone.value=it
                    },
                    singleLine = true,
                    leadingIcon = {
                        CountryOutlinedDropdown(
                            modifier = Modifier.wrapContentHeight(),
                            defaultCountryCode = "${TokenManager.getInstance(context).getCountryCode()}"
                        ){
                            countrycodes=it.code
                        }
                    },

                    placeholder = {
                        Text("Your Phone Number",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp,
                                fontFamily = monospaceRegular
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )
                com.io.luma.customcompose.height(13)


                rowHeader("User’s Language")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = language.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        language.value=it
                    },
                    trailingIcon = {
                        Icon(
                            imageVector =  Icons.Filled.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },

                    placeholder = {
                        Text("German",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp,
                                fontFamily = monospaceRegular
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )

                com.io.luma.customcompose.height(20)

                CustomButton(modifier = Modifier.fillMaxWidth(),
                    "Save") {
                  if (phone.value.length<10)
                  {
                      Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
                  }
                    else if (email.value.isEmpty())
                  {
                      Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                  }
                    else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches())
                  {
                      Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                  }
                    else if (firstName.value.isEmpty())
                  {
                      Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show()
                  }
                    else {
                      carerViewModel.updatePatientName(firstName.value+""+lasttName.value)
                      carerViewModel.updatePatientLanguage("en")
                      carerViewModel.updatePatientPhone(patientPhone = phone.value)
                      carerViewModel.countrycode("${countrycodes}")
                      carerViewModel.updatePatientEmail(patientEmail = email.value)
                      navController.navigate(NavRoute.SignupOptionStep5)
                    }



                }

                com.io.luma.customcompose.height(20)

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {

                    Text("By creating an account or signing you agree to our", style = TextStyle(
                        color = Color(0xff3F3F3F),
                        fontWeight = FontWeight.W500,
                        fontFamily = manropebold,
                        fontSize = 14.sp
                    ))


                }
                Text("Terms and Conditions",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = Color(0xff2951E0),
                        fontWeight = FontWeight.W500,
                        fontFamily = verandaRegular,
                        fontSize = 14.sp,
                    ))




            }


        }


    }


}