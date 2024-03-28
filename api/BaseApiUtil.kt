package com.codeplus.cppwalpapers.mvvm.api

import android.util.Log
import com.codeplus.cppwalpapers.mvvm.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseApiUtil {

    suspend fun <T> safeApiCall(apiToBeCalled: suspend ()-> Response<T>): Resource<T> {
        return withContext(Dispatchers.IO){
            try {
                Log.d("38472940238", "in try")
                val response: Response<T> = apiToBeCalled()
                Log.d("38472940238", "api called")

                if (response.isSuccessful && response.body() != null){
                    Log.d("38472940238", "response ok")
                    Resource.Success(data = response.body()!!)
                }else{
                    Resource.Error(errorMessage = "Something went wrong. Plz try again")
                }

            } catch (e: HttpException) {
                Resource.Error(errorMessage = e.message ?: "Something went wrong")
            } catch (e: IOException) {
                Resource.Error("Please check your network connection")
            } catch (e: Exception) {
                Resource.Error(errorMessage = "Something went wrong")
            }
        }
    }

}