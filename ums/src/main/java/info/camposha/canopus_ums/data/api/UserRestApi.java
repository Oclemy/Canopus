package info.camposha.canopus_ums.data.api;

/**
 * Let's define our imports
 */

import info.camposha.canopus_ums.data.model.entity.UserResponseModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Let's Create an interface
 */
public interface UserRestApi {
    @GET("index.php")
    Call<UserResponseModel> getAll();

    @FormUrlEncoded
    @POST("index.php")
    Call<UserResponseModel> search(@Field("action") String action,
                                   @Field("query") String query,
                                   @Field("start") String start,
                                   @Field("limit") String limit);

    @FormUrlEncoded
    @POST("index.php")
    Call<UserResponseModel> createAccount(@Field("action") String action,
                                          @Field("name") String name,
                                          @Field("email") String email,
                                          @Field("password") String password);

    @FormUrlEncoded
    @POST("index.php")
    Call<UserResponseModel> update(@Field("action") String action,
                                   @Field("id") String id,
                                   @Field("name") String name,
                                   @Field("password") String password
    );

    @FormUrlEncoded
    @POST("index.php")
    Call<UserResponseModel> deleteAccount(@Field("action") String action,
                                          @Field("id") String id);

    @FormUrlEncoded
    @POST("index.php")
    Call<UserResponseModel> login(@Field("action") String action,
                                  @Field("email") String email, @Field("password") String password);
}
//end
