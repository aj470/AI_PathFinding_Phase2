package Model;

import View.Map;

public class Astarweighted extends Search
{
    public Astarweighted(boolean flag)
    {
        super(flag);
    }

    double weight = 1.5;
    public void checkCurrCosts(Node n, int index)
    {
        if(!inClosed(n, 0) && !doesContain(n, 0))
        {
            if(flag.equals("Manhattan Distance"))
            {
                n.sethCost(Man_distance(n, goal)*weight);
            }
            else if(flag.equals("Bird Eye"))
            {
                n.sethCost(bird_eye(n, goal)*weight);
            }
            else if(flag.equals("Diagonal"))
            {
                n.sethCost(diagonal(n, goal)*weight);
            }
            else if(flag.equals("Chebyshev"))
            {
                n.sethCost(Chebyshev(n, goal)*weight);
            }
            else if(flag.equals("TieBreaker"))
            {
                n.sethCost(Tie_Cross(n, goal)*weight);
            }

            n.setgCost(n.getCost()+curr.getgCost());
            n.setParent(curr);
            n.setfCost(n.getfCost(false));
            Map.colorOpen(n.getCoord());
            fringe[0].add(n);
        }

        double newCost = curr.getgCost() + n.getCost();
        if(doesContain(n, 0) && (newCost < curr.getCost() + n.getCost()))
        {
            fringe[0].remove(n);
            Map.colorOpen(n.getCoord());
            n.setgCost(newCost);
            n.setfCost(n.getfCost(false));
            n.setParent(curr);
            fringe[0].add(n);
        }
    }


    public double getweight()
    {
        return this.weight;
    }
    public void setweight(double w)
    {
        this.weight = w;
    }

}
