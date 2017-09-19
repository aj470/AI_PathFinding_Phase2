package Model;

import java.util.Comparator;

public class AstarComparator implements Comparator<Node>
{
    public int compare(Node m, Node n)
    {
        if(m.getfCost(false) > n.getfCost(false))
        {
            return 1;
        }
        else if (m.getfCost(false) < n.getfCost(false))
        {
            return -1;
        }
        else
        {
            if(m.getgCost() > n.getgCost())
                return 1;
            else
                return -1;
        }
    }
}
