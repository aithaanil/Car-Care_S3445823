package uk.ac.tees.mad.carcare.di

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.carcare.core.Constants
import uk.ac.tees.mad.carcare.model.repository.AuthRepository
import uk.ac.tees.mad.carcare.ui.screens.home.HomeScreenViewModel
import uk.ac.tees.mad.carcare.ui.screens.login.LogInScreenViewModel
import uk.ac.tees.mad.carcare.ui.screens.signup.SignUpScreenViewModel
import uk.ac.tees.mad.carcare.ui.screens.splash.SplashViewModel
import kotlin.jvm.java


val appModule = module {
    single {FirebaseAuth.getInstance()}

    singleOf(::AuthRepository)

    viewModelOf(::SplashViewModel)

    viewModelOf(::LogInScreenViewModel)
    viewModelOf(::SignUpScreenViewModel)

    viewModelOf(::HomeScreenViewModel)




}

val appModules = listOf(appModule)