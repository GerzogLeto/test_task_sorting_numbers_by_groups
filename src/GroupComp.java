import java.util.Comparator;

public class GroupComp implements Comparator<Group> {
    @Override
    public int compare(Group o1, Group o2) {
        if(o1.size > o2.size) return -1;
        if(o1.size < o2.size) return 1;
        return 0;
    }
}
