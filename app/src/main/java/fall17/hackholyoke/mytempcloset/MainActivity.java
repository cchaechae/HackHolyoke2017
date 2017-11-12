package fall17.hackholyoke.mytempcloset;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fall17.hackholyoke.mytempcloset.data.Closet;
import fall17.hackholyoke.mytempcloset.data.Clothing;
import fall17.hackholyoke.mytempcloset.data.WeatherResult;
import fall17.hackholyoke.mytempcloset.network.WeatherAPI;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private String typedName = "South Hadley";
    private GoogleApiClient client;
    ArrayList<Clothing> clothesResult;
    Closet closet;
    Double currTemp = Double.valueOf(0.0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

    //make final text view variables
    final ImageView ivIcon = (ImageView) findViewById(R.id.ivIcon);
    final TextView tvTemp = (TextView) findViewById(R.id.tvDegree);
    final Button btnViewCloset = (Button) findViewById(R.id.btnViewCloset);
    final Button btnDonate = (Button) findViewById(R.id.btnDonate);
    final ImageView ivTop = (ImageView) findViewById(R.id.ivTop);
    final ImageView ivBottom = (ImageView) findViewById(R.id.ivBottom);



    btnViewCloset.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, MyClosetActivity.class);
            startActivity(i);
        }
    });

    btnDonate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(i);
        }
    });

        ((MainApplication)getApplication()).openRealm();

        RealmResults<Clothing> allItems = getRealm().where(Clothing.class).findAll();
        Clothing itemArray[] = new Clothing[allItems.size()];
        clothesResult = new ArrayList<Clothing>(Arrays.asList(allItems.toArray(itemArray)));

        closet = new Closet(clothesResult);


    Call<WeatherResult> callCity = weatherAPI.getCity(typedName, "imperial", "5383244a95768c470e7ed26d17ba9677");
    callCity.enqueue(new Callback<WeatherResult>(){

        @Override
        public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {

            Resources res = getResources();
            DecimalFormat df = new DecimalFormat("###.##");
            //String description = response.body().getWeather().get(0).getDescription().toUpperCase();
            //tvDescribe.setText(description);

            currTemp = response.body().getMain().getTemp();
            tvTemp.setText(res.getString(R.string.degrees, df.format(currTemp)));

            Glide.with(MainActivity.this).load("http://api.openweathermap.org/img/w/" + response.body().getWeather().get(0).getIcon() + ".png").into(ivIcon);
        }

        @Override
        public void onFailure(Call<WeatherResult> call, Throwable t) {

            tvTemp.setText(t.getLocalizedMessage());

        }
    });


    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    String path = closet.recommendTop(currTemp).getImgPath();
    Context c = getApplicationContext();
    int id = c.getResources().getIdentifier("drawable/"+path, null, c.getPackageName());
    ivTop.setImageResource(id);


    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Result Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}
