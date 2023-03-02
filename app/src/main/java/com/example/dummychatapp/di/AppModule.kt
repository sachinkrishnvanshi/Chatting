package com.example.dummychatapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dummychatapp.db.ChatDatabase
import com.example.dummychatapp.db.dao.ChatDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun getDatabase(context: Application) : ChatDatabase {
        return Room.databaseBuilder(context,ChatDatabase::class.java,"chatDB").build()
    }

    @Singleton
    @Provides
    fun chatDao(chatDatabase: ChatDatabase) : ChatDao {
        return chatDatabase.chatDao()
    }
}