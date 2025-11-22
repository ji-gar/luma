package com.io.luma

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.io.luma.customcompose.NotificationDialog
import com.io.luma.navroute.NavRoute
import com.io.luma.uiscreen.CareerCamping
import com.io.luma.uiscreen.CarerSignupOption
import com.io.luma.uiscreen.SignupCarer
import com.io.luma.uiscreen.SplaceScreen
import com.io.luma.uiscreen.carrercontanct.CarersContactList
import com.io.luma.uiscreen.dashboard.DashBoard
import com.io.luma.uiscreen.dashboard.PatientScreen
import com.io.luma.uiscreen.event.EventListCompose
import com.io.luma.uiscreen.event.EventListForm
import com.io.luma.uiscreen.loginscreen.LoginOption
import com.io.luma.uiscreen.loginscreen.LoginScreen
import com.io.luma.uiscreen.loginscreen.MobileNumberScreen
import com.io.luma.uiscreen.myselfflow.MySelfFlow
import com.io.luma.uiscreen.myselfflow.stepscreen.MySelfStep1
import com.io.luma.uiscreen.myselfflow.stepscreen.MySelfStep2
import com.io.luma.uiscreen.myselfflow.stepscreen.MySelfStep3
import com.io.luma.uiscreen.onbordingscreen.OnBordingScreen
import com.io.luma.uiscreen.onbordingscreen.OnBordingScreen2
import com.io.luma.uiscreen.onbordingscreen.OnBordingScreen3
import com.io.luma.uiscreen.onbordingscreen.OnBordingScreen4
import com.io.luma.uiscreen.onbordingscreen.OnBordingScreen5
import com.io.luma.uiscreen.onbordingscreen.OnBordingScreen6
import com.io.luma.uiscreen.onbordingscreen.OnBordingScreen7
import com.io.luma.uiscreen.onbordingscreen.OnBordingScreen8
import com.io.luma.uiscreen.recurringtasks.RecurringTasksList
import com.io.luma.uiscreen.recurringtasks.RecurringtaskFormScreen
import com.io.luma.uiscreen.schdual.DailyRouting
import com.io.luma.uiscreen.schdual.SchdualScreen
import com.io.luma.uiscreen.setPassword
import com.io.luma.uiscreen.someoneelsesignup.SignupStep1
import com.io.luma.uiscreen.someoneelsesignup.SignupStep2
import com.io.luma.uiscreen.someoneelsesignup.SignupStep3
import com.io.luma.uiscreen.someoneelsesignup.SignupStep4
import com.io.luma.uiscreen.someoneelsesignup.SignupStep5
import com.io.luma.uiscreen.someoneelsesignup.SignupStep6
import com.io.luma.uiscreen.talktoluma.talkToLuma
import com.io.luma.uiscreen.weeaklySchdual.WeekalyRouting
import com.io.luma.uiscreen.weeaklySchdual.WeekalyRoutingForm
import com.io.luma.viewmodel.CarerRegisterViewModel
import com.io.luma.viewmodel.RegisterViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppContainer(
    container: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    val registermyself: RegisterViewModel = viewModel()

    val carerViewModel: CarerRegisterViewModel = viewModel()


    NavHost(navController, startDestination = NavRoute.OnBordingScreen) {

        composable<NavRoute.SignupСarer> {

            SignupCarer(navController)
        }

        composable<NavRoute.DashBoard> {

            DashBoard(navController)
        }
        composable<NavRoute.DailyRouting> {
            DailyRouting(navController)

        }

        composable<NavRoute.PatientScreen> {
            PatientScreen(navController)
        }
        composable<NavRoute.talkToLuma> {
            talkToLuma(navController)
        }

        composable<NavRoute.RecurringTasksList> {
            RecurringTasksList(navController)

        }

        composable<NavRoute.EventListForm> {
            EventListForm(navController)

        }
        composable<NavRoute.RecurringtaskFormScreen> {
            RecurringtaskFormScreen(navController)

        }

        composable<NavRoute.WeekalyRoutingForm> {
            WeekalyRoutingForm(navController)

        }
        composable<NavRoute.EventListCompose> {
            EventListCompose(navController)

        }

        composable<NavRoute.MobileScreen> {
            MobileNumberScreen(navController)
        }

        composable<NavRoute.SplaceScreen> {

            SplaceScreen(navController)
        }
        composable<NavRoute.SignupOption> {
            CarerSignupOption(navController)

        }
        composable<NavRoute.LoginOption> {
            LoginOption(navController)

        }
        composable<NavRoute.LoginScreen> {
            LoginScreen(navController)

        }

        composable<NavRoute.CarrersContactList> {
            CarersContactList()

        }
        composable<NavRoute.SignupOptionStep1> {
            SignupStep1(navController)

        }
        composable<NavRoute.WeekalyRouting> {
            WeekalyRouting(navController)

        }

        composable<NavRoute.SchdualScreen> {
            SchdualScreen(navController)

        }

        composable<NavRoute.SignupOptionStep2> {
            SignupStep2(navController, carerViewModel)

        }
        composable<NavRoute.SignupOptionStep3> {
            SignupStep3(navController, carerViewModel)

        }

        composable<NavRoute.CareerCampingScreen> {
            CareerCamping(navController)

        }
        composable<NavRoute.SignupOptionStep4> {
            SignupStep4(navController, carerViewModel)

        }

        composable<NavRoute.SignupOptionStep5> {
            SignupStep5(navController, carerViewModel)

        }
        composable<NavRoute.SignupOptionStep6> {
            SignupStep6(navController, carerViewModel)

        }
        composable<NavRoute.Myself> {
            MySelfFlow(navController)

        }

        composable<NavRoute.MyselfStep1> {
            MySelfStep1(navController, registermyself)

        }
        composable<NavRoute.MyselfStep2> {
            MySelfStep2(navController, registermyself)

        }
        composable<NavRoute.MyselfStep3> {
            MySelfStep3(navController, registermyself)

        }
        composable<NavRoute.SetPassword> {
            setPassword(navController)

        }
        composable<NavRoute.OnBordingScreen> {
            OnBordingScreen(navController) {
//                showAlarmDialog = true
            }

        }
        composable<NavRoute.OnBordingScreen2> {
            OnBordingScreen2(navController)

        }
        composable<NavRoute.OnBordingScreen3> {
            OnBordingScreen3(navController)

        }
        composable<NavRoute.OnBordingScreen4> {
            OnBordingScreen4(navController)

        }

        composable<NavRoute.OnBordingScreen5> {
            OnBordingScreen5(navController)

        }

        composable<NavRoute.OnBordingScreen6> {
            OnBordingScreen6(navController)

        }
        composable<NavRoute.OnBordingScreen7> {
            OnBordingScreen7(navController)

        }
        composable<NavRoute.OnBordingScreen8> {
            OnBordingScreen8(navController)

        }
    }

    if (container.showAlarmDialog) {
        NotificationDialog(
            headerTitle = "Emergency Call",
            alertTitle = "Serverly disoriented",
            alertDescription = "Emergency situation with Amy Bishop!",
            btnPositiveText = "Talk to Amy",
            btnNegativeText = "No",
            onDismissRequest = {
                container.dismissAlarmDialog()
            },
            onPositiveClick = {
                container.dismissAlarmDialog()
            }
        )
    }
}

class AppContainer {

    var showAlarmDialog by mutableStateOf(false)
        private set

    var alarmTitle: String = ""
    var alarmDescription: String = ""

    fun triggerAlarmDialog(title: String, description: String) {
        alarmTitle = title
        alarmDescription = description
        showAlarmDialog = true
    }

    fun dismissAlarmDialog() {
        showAlarmDialog = false
    }
}