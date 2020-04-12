package com.androar.fitly;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    String BASE_URL = "http://goforbg.com/";


    @GET("home_activities")
    Call<List<RecyclerItemActivities>> getActivities();
}
