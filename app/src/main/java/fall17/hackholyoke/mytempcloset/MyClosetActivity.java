package fall17.hackholyoke.mytempcloset;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fall17.hackholyoke.mytempcloset.data.Clothing;
import io.realm.Realm;
import io.realm.RealmResults;
import fall17.hackholyoke.mytempcloset.adapter.ClothingAdapter;

public class MyClosetActivity extends AppCompatActivity {

    private ClothingAdapter clothingAdapter;
    private int REQUEST_NEW_Clothing = 101;
    public static final String KEY_EDIT = "KEY_EDIT";

    public static final int REQUEST_EDIT_Clothing = 102;

    @BindView(R.id.fabbtnAdd)
    FloatingActionButton fabbtnAdd;

    @OnClick(R.id.fabbtnAdd)
    public void addClothing() {
        Intent intentTakePhoto = new Intent(MyClosetActivity.this,PhotoReadyActivity.class);
        startActivity(intentTakePhoto);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_closet);

        ButterKnife.bind(this);
        ((MainApplication)getApplication()).openRealm();

        RealmResults<Clothing> allClothings = getRealm().where(Clothing.class).findAll();
        //deleteAll();
        Clothing ClothingsArray[] = new Clothing[allClothings.size()];
        List<Clothing> ClothingsResult = new ArrayList<Clothing>(Arrays.asList(allClothings.toArray(ClothingsArray)));

        clothingAdapter = new ClothingAdapter(ClothingsResult,this);

        RecyclerView recyclerViewClothings = (RecyclerView) findViewById(
                R.id.recyclerViewClothings);
        recyclerViewClothings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClothings.setAdapter(clothingAdapter);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String ClothingID  = data.getStringExtra(
                        PhotoReadyActivity.KEY_ITEM);

                Clothing Clothing = getRealm().where(Clothing.class)
                        .equalTo("clothingID", ClothingID)
                        .findFirst();

                if (requestCode == REQUEST_NEW_Clothing) {
                    clothingAdapter.addClothing(Clothing);
                }


                break;

            case RESULT_CANCELED:
                break;
        }
    }


    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }

    public void deleteClothing(Clothing Clothing) {
        getRealm().beginTransaction();
        Clothing.deleteFromRealm();
        getRealm().commitTransaction();
    }

    public void deleteAll(){
        RealmResults<Clothing> allClothings = getRealm().where(Clothing.class).findAll();
        for(int i = allClothings.size()-1;i>=0; i--){
            clothingAdapter.removeClothing(i);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }

}
