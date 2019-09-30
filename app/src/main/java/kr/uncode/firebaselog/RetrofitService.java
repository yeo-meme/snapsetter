package kr.uncode.firebaselog;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitService {
    @Headers("Authorization: KakaoAK a6b4ae2dd5484a7d83305a2eb851c5a6")
    @GET("/v2/search/image")
    Call<RetrofitService> search (
      @Query("query") String query,
      @Query("sort") String sort,
      @Query("page") String page,
      @Query("size") String size
    );
}
