package Model;

import View.Map;

public class UCS extends Search
{
    public UCS(boolean flag)
    {
        super(flag);
    }

    @Override
    public void checkCurrCosts(Node n, int index)
    {
        if(!inClosed(n, 0) && !doesContain(n, 0))
        {
            n.setgCost(n.getCost() + curr.getgCost());
            //n.totalCost += n.getCost() + curr.totalCost;
            n.setParent(curr);
            Map.colorOpen(n.getCoord());
            fringe[0].add(n);
        }
        double newCost = curr.getgCost() + n.getCost();
        if(doesContain(n, 0) && newCost < n.totalCost)
        {
            fringe[0].remove(n);
            Map.colorOpen(n.getCoord());
            n.setgCost(newCost);
            n.setParent(curr);
            fringe[0].add(n);
        }
    }

}
