package uk.ac.tees.mad.carcare.di

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.carcare.core.Constants
import uk.ac.tees.mad.carcare.ui.screens.splash.SplashViewModel
import kotlin.jvm.java


val appModule = module {

//    single {
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//
//        OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .build()
//    }

//    single {
//        Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(get())
//            .build()
//            .create(AuthApi::class.java)
//    }

//    single {
//        Room.databaseBuilder(
//            androidContext(),
//            AppDatabase::class.java,
//            Constants.DATABASE_NAME
//        ).build()
//    }

}

val splashModule = module {
//    singleOf(::RemoteSplashDataSource)
//    singleOf(::SplashRepository)
//    factoryOf(::SplashUseCase)
//
    viewModelOf(::SplashViewModel)
}

val authModule = module {
//    singleOf(::RemoteAuthDataSource)
//
//    singleOf(::AuthRepository)
//
//    factoryOf(::AuthUseCase)
//
//    viewModel { LoginViewModel(get()) }
//    viewModel { SignupViewModel(get()) }
}

val appModules = listOf(appModule, splashModule, authModule)