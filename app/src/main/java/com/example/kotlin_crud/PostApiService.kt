import retrofit2.http.*

interface PostApiService {
    // CREATE
    @POST("posts")
    suspend fun createPost(@Body post: Post): Post

    // READ (All items)
    @GET("posts")
    suspend fun getPosts(): List<Post>

    // UPDATE
    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): Post

    // DELETE
    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): retrofit2.Response<Unit>
}
