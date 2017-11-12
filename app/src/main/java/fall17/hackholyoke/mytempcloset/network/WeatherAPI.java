package fall17.hackholyoke.mytempcloset.network;

import fall17.hackholyoke.mytempcloset.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by chaelimseo on 11/11/17.
 */

public interface WeatherAPI {

    @GET("data/2.5/weather")
    Call<WeatherResult> getCity(@Query("q") String q, @Query("units") String units, @Query("appid") String appid);

}
