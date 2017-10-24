package gesac.com.login

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by GE11522 on 2017/9/18.
 */
data class User(var id: String, var pw: String) {
    interface Login {
        @GET("VerifyServlet")
        fun signUp(@Query("user") id: String,
                   @Query("pw") pw: String)
                : Observable<String>
    }
}