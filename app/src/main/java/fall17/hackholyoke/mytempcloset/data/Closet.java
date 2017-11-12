package fall17.hackholyoke.mytempcloset.data;

/**
 * Created by chaelimseo on 11/12/17.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import fall17.hackholyoke.mytempcloset.MainApplication;
import io.realm.RealmResults;

public class Closet {
    List<Clothing> topList = new ArrayList<Clothing>();
    List<Clothing> bottomList = new ArrayList<Clothing>();
    List<Clothing> leastFavoriteList = new ArrayList<Clothing>();


    // fahrenheit 10 to 100
    // -4 +4

    public Closet(ArrayList<Clothing> clothesResult){

        add(clothesResult);
        sort(clothesResult);

    }

    public void add(List<Clothing> clothesResult){
        for(Clothing c : clothesResult){
            if(c.getClothingType() == 0){
                topList.add(c);
            }
            else{
                bottomList.add(c);
            }
        }
    }

    public void update(List<Clothing> clothesResult){
        topList.clear();
        bottomList.clear();
        add(clothesResult);
    }

    public void sort(ArrayList<Clothing> list){

        Collections.sort(list, new ClothingComparator());
        Collections.reverse(list);
    }

    public Clothing recommendTop(double temp){
        ArrayList<Clothing> list = new ArrayList<Clothing>();
        for(Clothing top : topList){
            if(top.getTemp() <= temp+4 && top.getTemp() >= temp-4){
                list.add(top);
            }
        }

        if(list.isEmpty()){
            return null;
        }
        else if(list.size() == 1){
            return list.get(0);
        }
        else if(list.isEmpty()){
            int next = 1;
            ArrayList<Clothing> list2 = new ArrayList<Clothing>();
            list2.add(list.get(0));
            while(list.get(0).getNumStars() == list.get(next).getNumStars()){
                list2.add(list.get(next));
                next++;
            }
            return list2.get((int)(Math.random()*list2.size()));
        }
        return null;
    }

    public Clothing recommendBottom(double temp){
        ArrayList<Clothing> list = new ArrayList<Clothing>();
        for(Clothing bottom : bottomList){
            if(bottom.getTemp() <= temp+4 && bottom.getTemp() >= temp-4){
                return bottom;
            }
        }
        if(list.isEmpty()){
            return null;
        }
        else if(list.size() == 1){
            return list.get(0);
        }
        else if(list.isEmpty()){
            int next = 1;
            ArrayList<Clothing> list2 = new ArrayList<Clothing>();
            list2.add(list.get(0));
            while(list.get(0).getNumStars() == list.get(next).getNumStars()){
                list2.add(list.get(next));
                next++;
            }
            return list2.get((int)(Math.random()*list2.size()));
        }
        return null;
    }

    public Clothing recommendDonation(double temp){
        int next = 1;
        ArrayList<Clothing> list = new ArrayList<Clothing>();
        list.add(leastFavoriteList.get(0));
        while(leastFavoriteList.get(0).getNumStars() == leastFavoriteList.get(next).getNumStars()){
            list.add(leastFavoriteList.get(next));
            next++;
        }
        if(list.size() == 1){
            return list.get(0);
        }
        else if(!list.isEmpty())
            return list.get((int)(Math.random()*list.size()));

        return null;
    }
}

