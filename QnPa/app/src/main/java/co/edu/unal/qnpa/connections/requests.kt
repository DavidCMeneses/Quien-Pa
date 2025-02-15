package co.edu.unal.qnpa.connections

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// Base URL for the API
object ApiClient {
    private const val BASE_URL = "https://quien-pa.onrender.com/"
    //private const val BASE_URL = "http://localhost:3000/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

// Data models
data class User(val id: String?, val name: String, val email: String)
data class Activity(val id: String?, val title: String, val description: String)
data class Category(val id: String?, val name: String)
data class Review(val id: String?, val rating: Int, val comment: String)

// API service interface
interface ApiService {

    // Users
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @POST("users")
    suspend fun createUser(@Body user: User): User

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): User

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
    suspend fun getAverageRatingForUser(@Path("userId") userId: String): Double

    @PUT("reviews/{id}")
    suspend fun updateReview(@Path("id") id: String, @Body review: Review): Review

    @DELETE("reviews/{id}")
    suspend fun deleteReview(@Path("id") id: String)
}

// Singleton for accessing the API service
object ApiServiceInstance {
    val api: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }
}