import java.util.Comparator;

public class CoordComp implements Comparator<Structure> {
    @Override
    public int compare(Structure o1, Structure o2) {
        if(o1.number < o2.number) return -1;
        if(o1.number > o2.number) return 1;
        return 0;
    }
}
