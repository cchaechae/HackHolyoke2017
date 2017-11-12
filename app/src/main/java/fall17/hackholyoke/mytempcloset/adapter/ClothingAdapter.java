package fall17.hackholyoke.mytempcloset.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import fall17.hackholyoke.mytempcloset.MyClosetActivity;
import fall17.hackholyoke.mytempcloset.R;
import fall17.hackholyoke.mytempcloset.data.Clothing;

/**
 * Created by Moe on 11/12/17.
 */

public class ClothingAdapter extends RecyclerView.Adapter<ClothingAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivClothing;
        public Button btnDonate;

        public ViewHolder(View ClothingView) {
            super(ClothingView);
            ivClothing = (ImageView) ClothingView.findViewById(R.id.clothingImage);
            btnDonate= (Button) ClothingView.findViewById(R.id.btnDonate);
        }
    }

    private List<Clothing> ClothingList;
    private Context context;
    private int lastPosition = -1;

    public ClothingAdapter(List<Clothing> ClothingList, Context context) {
        this.ClothingList = ClothingList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_details, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

//        byte[] imageData = ClothingList.get(position).getImageByte();
////        Log.d("IS IT NULL", imageData.toString());
//        Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//        Bitmap back = Bitmap.createBitmap(bmp.getWidth(),
//                bmp.getHeight(), Bitmap.Config.ARGB_8888);
//        viewHolder.ivClothing.setImageBitmap(back);
        String path = ClothingList.get(position).getImgPath();
        try {
            File f=new File(path, ClothingList.get(position).getclothingID()+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            viewHolder.ivClothing.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        viewHolder.btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClothing(viewHolder.getAdapterPosition());
            }
        });


    }



    @Override
    public int getItemCount() {
        return ClothingList.size();
    }

    public void addClothing(Clothing Clothing) {
        ClothingList.add(Clothing);
        notifyDataSetChanged();
    }

    public Clothing getPlace(int i) {
        return ClothingList.get(i);
    }

    public void removeClothing(int index) {
        ((MyClosetActivity)context).deleteClothing(ClothingList.get(index));
        ClothingList.remove(index);
        notifyItemRemoved(index);
    }


}
