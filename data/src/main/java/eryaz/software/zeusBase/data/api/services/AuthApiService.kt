package eryaz.software.zeusBase.data.api.services

import eryaz.software.zeusBase.data.models.remote.models.PdaVersionModel
import eryaz.software.zeusBase.data.models.remote.models.ResultModel
import eryaz.software.zeusBase.data.models.remote.request.LoginRequest
import eryaz.software.zeusBase.data.models.remote.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/TokenAuth/Authenticate")
    suspend fun login(@Body request: LoginRequest?): ResultModel<UserResponse>

    @GET("api/services/app/Common/GetPdaVersion")
    suspend fun getPdaVersion(): ResultModel<PdaVersionModel>

}