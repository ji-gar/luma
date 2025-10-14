package com.io.luma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.LumaTheme
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.skyblue
import com.io.luma.uiscreen.CarerSignupOption
import com.io.luma.uiscreen.SignupCarer
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
import com.io.luma.uiscreen.someoneelsesignup.SignupStep1
import com.io.luma.uiscreen.someoneelsesignup.SignupStep2
import com.io.luma.uiscreen.someoneelsesignup.SignupStep3
import com.io.luma.uiscreen.someoneelsesignup.SignupStep4
import com.io.luma.uiscreen.someoneelsesignup.SignupStep5
import com.io.luma.uiscreen.someoneelsesignup.SignupStep6
import com.io.luma.viewmodel.CarerRegisterViewModel
import com.io.luma.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge(
            // To make the status bar icons color light
            //     statusBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb())
            // If you want to make the status bar icons color dark than you can use it
            statusBarStyle = SystemBarStyle.light(Color.Red.toArgb(), Color.White.toArgb())
        )
        setContent {
            LumaTheme {
                NavHost()
            }
        }
    }
}

@Composable
fun NavHost() {
    var navController= rememberNavController()
    val registermyself: RegisterViewModel = viewModel()

    val carerViewModel: CarerRegisterViewModel = viewModel()

    NavHost(navController, startDestination = NavRoute.OnBordingScreen5) {

        composable<NavRoute.SignupСarer> {

            SignupCarer(navController)
        }
        composable<NavRoute.SignupOption> {
            CarerSignupOption(navController)

        }
        composable<NavRoute.SignupOptionStep1> {
            SignupStep1(navController)

        }
        composable<NavRoute.SignupOptionStep2> {
            SignupStep2(navController,carerViewModel)

        }
        composable<NavRoute.SignupOptionStep3> {
            SignupStep3(navController,carerViewModel)

        }
        composable<NavRoute.SignupOptionStep4> {
            SignupStep4(navController,carerViewModel)

        }

        composable<NavRoute.SignupOptionStep5> {
            SignupStep5(navController,carerViewModel)

        }
        composable<NavRoute.SignupOptionStep6> {
            SignupStep6(navController,carerViewModel)

        }
        composable<NavRoute.Myself> {
            MySelfFlow(navController)

        }

        composable<NavRoute.MyselfStep1> {
            MySelfStep1(navController,registermyself)

        }
        composable<NavRoute.MyselfStep2> {
            MySelfStep2(navController,registermyself)

        }
        composable<NavRoute.MyselfStep3> {
            MySelfStep3(navController,registermyself)

        }
        composable<NavRoute.OnBordingScreen> {
            OnBordingScreen(navController,)

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
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {


    Box(modifier= Modifier
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
        )){


        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            CustomOutlineButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Hello How Are You ?"
            ) { }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LumaTheme {
        Greeting("Android")
    }
}