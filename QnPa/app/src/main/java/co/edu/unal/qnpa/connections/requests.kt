package co.edu.unal.qnpa.connections

import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


// Base URL for the API
object ApiClient {
    private const val BASE_URL = "https://quien-pa.onrender.com/"

    // Interceptor para logs detallados de las peticiones
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client) // Agregar el cliente con logging
        .build()
}

data class User(
    val id: String? = null,
    val name: String,
    val email: String,
    val age: Int,
    val description: String? = null,
    val password: String,
    val imageUrl: String? = null
)

data class Activity(
    val id: String?,
    val name: String,
    val description: String,
    val latitude: String,
    val longitude: String,
    val userId: Int, // Cambiar a Int
    val date: String,
    val place: String,
    val imageUrl: String?,
    val createdOn: String? // Añadir createdOn
)

data class Category(val id: String?, val name: String)

data class Review(
    val id: String?,
    val activityId: String,
    val reviewerId: String,
    val reviewedUserId: String,
    val rating: Int,
    val comment: String
)

// Modelo para la solicitud de login
data class LoginRequest(
    val email: String,
    val password: String
)

// Modelo para la respuesta de login
data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val userId: String?
)

data class RatingResponse(
    val averageRating: Double
)

data class UserUpdateRequest(
    val name: String,
    val email: String,
    val age: Int,
    val description: String? = null,
    val imageUrl: String? = null
)

data class AssignCategoryToActivityRequest(
    val activityId: String
)


data class ActivityScreenRoute(val activityId: String) // Cambiar de "Activity" a "ActivityScreenRoute"


// API service interface
interface ApiService {

    // Endpoint para el login
    @POST("users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse

    // Users
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): List<User>

    @POST("users")
    suspend fun createUser(@Body user: User): User

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: UserUpdateRequest): User

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String)

    // Activities
    @GET("activities")
    suspend fun getActivities(): List<Activity>

    @GET("activities/{id}")
    suspend fun getActivityById(@Path("id") id: String): Activity

    @POST("activities")
    suspend fun createActivity(@Body activity: Activity): Activity

    @POST("activities/{activityId}/users/{userId}")
    suspend fun assignUserToActivity(@Path("activityId") activityId: String, @Path("userId") userId: String)

    @GET("activities/{id}/users")
    suspend fun getUsersByActivity(@Path("id") id: String): List<User>

    @PUT("activities/{id}")
    suspend fun updateActivity(@Path("id") id: String, @Body activity: Activity): Activity

    @DELETE("activities/{id}")
    suspend fun deleteActivity(@Path("id") id: String)

    // Categories
    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("categories/{id}")
    suspend fun getCategoryById(@Path("id") id: String): Category

    @GET("categories/activities/{id}")
    suspend fun getCategoriesByActivity(@Path("id") activityId: String): List<Category>

    @GET("categories/users/{id}")
    suspend fun getCategoriesByUser(@Path("id") userId: String): List<Category>

    @POST("categories/{categoryId}/activities")
    suspend fun assignCategoryToActivity(
        @Path("categoryId") categoryId: String,
        @Body request: AssignCategoryToActivityRequest // Enviar el objeto en el cuerpo
    )

    @POST("categories")
    suspend fun createCategory(@Body category: Category): Category

    @POST("categories/{categoryId}/users/{userId}")
    suspend fun assignUserToCategory(@Path("categoryId") categoryId: String, @Path("userId") userId: String)

    @PUT("categories/{id}")
    suspend fun updateCategory(@Path("id") id: String, @Body category: Category): Category

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: String)

    // Reviews
    @POST("reviews")
    suspend fun addReview(@Body review: Review): Review

    @GET("reviews/users/{userId}")
    suspend fun getReviewsForUser(@Path("userId") userId: String): List<Review>

    @GET("reviews/activities/{activityId}")
    suspend fun getReviewsForActivity(@Path("activityId") activityId: String): List<Review>

    @GET("reviews/activities/{activityId}/average-rating")
    suspend fun getAverageRatingForActivity(@Path("activityId") activityId: String): Double

    @GET("reviews/users/{userId}/average-rating")
    suspend fun getAverageRatingForUser(@Path("userId") userId: String): RatingResponse

    @PUT("reviews/{id}")
    suspend fun updateReview(@Path("id") id: String, @Body review: Review): Review

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String)
}

// Singleton para acceder a la API
object ApiServiceInstance {
    val api: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }
}

// Función para loggear el JSON antes de enviarlo
fun logJson(tag: String, data: Any) {
    val gson = Gson()
    val json = gson.toJson(data)
    Log.d(tag, "JSON enviado: $json")
}