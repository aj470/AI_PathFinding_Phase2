package Model;

import java.util.Comparator;

public class UCSComparator implements Comparator<Node>
{
    public int compare(Node m, Node n)
    {
        if(m.getgCost() < n.getgCost())
            return -1;
        else if(m.getgCost() > n.getgCost())
            return 1;
        return 0;
    }
}
