package fall17.hackholyoke.mytempcloset.data;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

import fall17.hackholyoke.mytempcloset.R;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Moe on 11/11/17.
 */

public class Clothing extends RealmObject {

    int numStars;
    int lowTemp;
    int highTemp;

    String imgPath;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getClothingType() {
        return clothingType;
    }

    public void setClothingType(int clothingType) {
        this.clothingType = clothingType;
    }

    private int clothingType;
    @PrimaryKey
    private String clothingID;




    public String getclothingID() {
        return clothingID;
    }

    public void setclothingID(String clothingID) {
        this.clothingID = clothingID;
    }

    public String getId() {
        return clothingID;
    }

    public void setId(String id) {
        this.clothingID = id;
    }

    public int getNumStars() {
        return numStars;
    }

    public void setNumStars(int numStars) {
        this.numStars = numStars;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

//    public ImageView getImage() {
//        return image;
//    }
//
//    public void setImage(ImageView image) {
//        this.image = image;
//    }

    public enum ClothingType {
        TOP(0),BOTTOM(1);

        private int value;

        private ClothingType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
        

        public static ClothingType fromInt(int value) {
            for (ClothingType p : ClothingType.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return TOP;
        }
    }

    public Clothing(){}

    public Clothing(int numStars, ImageView image){
        this.numStars = numStars;
//        this.image = image;
    }


}
