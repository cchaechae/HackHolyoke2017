package fall17.hackholyoke.mytempcloset.data;

import java.util.Comparator;

/**
 * Created by chaelimseo on 11/12/17.
 */

public class ClothingComparator implements Comparator<Clothing>{

    @Override
    public int compare(Clothing c1, Clothing c2) {

        return Integer.valueOf(c1.getNumStars()).compareTo(Integer.valueOf(c2.getNumStars()));
    }
}
