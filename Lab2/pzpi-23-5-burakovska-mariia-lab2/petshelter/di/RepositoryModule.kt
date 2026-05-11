package ua.nure.petshelter.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ua.nure.petshelter.db.DbRepository
import ua.nure.petshelter.db.DbRepositoryImpl
import ua.nure.petshelter.repository.animal.AnimalRepository
import ua.nure.petshelter.repository.animal.AnimalRepositoryImpl
import ua.nure.petshelter.repository.auth.AuthRepository
import ua.nure.petshelter.repository.auth.AuthRepositoryImpl
import ua.nure.petshelter.repository.task.TaskRepository
import ua.nure.petshelter.repository.task.TaskRepositoryImpl
import ua.nure.petshelter.repository.token.TokenRepository
import ua.nure.petshelter.repository.token.TokenRepositoryImpl
import ua.nure.petshelter.repository.donation.DonationRepository
import ua.nure.petshelter.repository.donation.DonationRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDbRepository(
        @ApplicationContext context: Context,
        tokenRepository: TokenRepository
    ): DbRepository = DbRepositoryImpl(
        context = context,
        tokenRepository = tokenRepository
    )

    @Provides
    @Singleton
    fun provideTokenRepository(
        dataStore: DataStore<Preferences>
    ): TokenRepository = TokenRepositoryImpl(
        dataStore = dataStore
    )

    @Provides
    @Singleton
    fun provideAuthRepository(
        httpClient: HttpClient,
        tokenRepository: TokenRepository,
        dbRepository: DbRepository,
    ): AuthRepository = AuthRepositoryImpl(
        httpClient = httpClient,
        tokenRepository = tokenRepository,
        dbRepository = dbRepository,
    )

    @Singleton
    @Provides
    fun provideAnimalRepository(
        httpClient: HttpClient,
    ): AnimalRepository = AnimalRepositoryImpl(
        httpClient = httpClient
    )

    @Singleton
    @Provides
    fun provideTaskRepository(
        httpClient: HttpClient,
    ): TaskRepository = TaskRepositoryImpl(
        httpClient = httpClient
    )

    @Singleton
    @Provides
    fun provideDonationRepository(
        httpClient: HttpClient,
        tokenRepository: TokenRepository
    ): DonationRepository = DonationRepositoryImpl(
        httpClient = httpClient,
        tokenRepository = tokenRepository
    )
}