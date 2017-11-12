package fall17.hackholyoke.mytempcloset;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Rating;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fall17.hackholyoke.mytempcloset.data.Clothing;
import io.realm.Realm;
import io.realm.RealmResults;

public class PhotoReadyActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_PHOTO = 101;
    public static final String KEY_ITEM = "KEY_ITEM";

    private Spinner spinnerItemType;

    private Bitmap img;
    private Clothing clothingToSave = null;

    RatingBar ratingBar;
    @BindView(R.id.imageAttach)
    ImageView imageAttach;
    @BindView(R.id.btnPhoto)
    Button btnPhoto;

    @OnClick(R.id.btnPhoto)
    public void takePicture() {
        Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentTakePhoto, REQUEST_CODE_TAKE_PHOTO);
    }

    public void setupUI(){
        spinnerItemType = (Spinner) findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.clothingtypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(adapter);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        imageAttach = (ImageView) findViewById(R.id.imageAttach);

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveClothing();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            img = (Bitmap) data.getExtras().get("data");
            imageAttach.setImageBitmap(img);
            imageAttach.setVisibility(View.VISIBLE);
        }
    }

    private void initCreate(){
        getRealm().beginTransaction();
        clothingToSave = getRealm().createObject(Clothing.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();
    }
    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_ready);
        ButterKnife.bind(this);
        setupUI();
        ((MainApplication)getApplication()).openRealm();
        initCreate();
    }

    public void testPrint(){
        RealmResults<Clothing> clothingResults = getRealm().where(Clothing.class).findAll();
        for (Clothing c: clothingResults){
            Log.d("TEST", c.getId());
        }
    }
    public void saveClothing(){
        Intent intentResult = new Intent();
        getRealm().beginTransaction();
        clothingToSave.setNumStars(ratingBar.getNumStars());
        // saving the image
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        img.compress(Bitmap.CompressFormat.JPEG, 200, baos);
//        byte[] imageInBytes = baos.toByteArray();
//        clothingToSave.setImageByte(imageInBytes);
        String path = saveToInternalStorage(img);
        clothingToSave.setImgPath(path);
        clothingToSave.setClothingType(spinnerItemType.getSelectedItemPosition());
        getRealm().commitTransaction();

        intentResult.putExtra(KEY_ITEM, clothingToSave.getclothingID());
        setResult(RESULT_OK, intentResult);
        //testPrint();
        finish();

    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,clothingToSave.getclothingID()+".jpeg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


}
