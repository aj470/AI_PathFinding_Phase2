package Model;

import java.util.Comparator;

public class MultiHeurComparator implements Comparator<Node>
{
    public int compare(Node m, Node n)
    {
        if(m.getfCost(true) > n.getfCost(true))
        {
            return 1;
        }
        else if (m.getfCost(true) < n.getfCost(true))
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
