package com.io.luma.navroute

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {
    @Serializable
    data object SignupСarer : NavRoute()

    @Serializable
    data object SignupOption : NavRoute()

    @Serializable
    data object SignupOptionStep1 : NavRoute()
    @Serializable
    data object SignupOptionStep2 : NavRoute()

    @Serializable
    data object SignupOptionStep3 : NavRoute()
    @Serializable
    data object SignupOptionStep4 : NavRoute()

    @Serializable
    data object SignupOptionStep5 : NavRoute()

    @Serializable
    data object SignupOptionStep6 : NavRoute()

    @Serializable
    data object Myself : NavRoute()

    @Serializable
    data object MyselfStep1 : NavRoute()

    @Serializable
    data object MyselfStep2 : NavRoute()

    @Serializable
    data object MyselfStep3 : NavRoute()

    @Serializable
    data object OnBordingScreen : NavRoute()

    @Serializable
    data object OnBordingScreen2 : NavRoute()
    @Serializable
    data object OnBordingScreen3 : NavRoute()
    @Serializable
    data object OnBordingScreen4 : NavRoute()

    @Serializable
    data object OnBordingScreen5 : NavRoute()

    @Serializable
    data object OnBordingScreen6 : NavRoute()

    @Serializable
    data object OnBordingScreen7 : NavRoute()

    @Serializable
    data object OnBordingScreen8 : NavRoute()


    //SignupStep4
}