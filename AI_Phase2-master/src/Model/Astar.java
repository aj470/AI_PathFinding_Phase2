package Model;

import View.Map;

public class Astar extends Search
{
    public Astar(boolean flag)
    {
        super(flag);
    }

    public void checkCurrCosts(Node n, int index)
    {
            if (!inClosed(n, 0) && !doesContain(n, 0))
            {
                if (flag.equals("Manhattan Distance"))
                {
                    n.sethCost(Man_distance(n, goal));
                }
                else if (flag.equals("Bird Eye"))
                {
                    n.sethCost(bird_eye(n, goal));
                }
                else if (flag.equals("Diagonal"))
                {
                    n.sethCost(diagonal(n, goal));
                }
                else if (flag.equals("Chebyshev"))
                {
                    n.sethCost(Chebyshev(n, goal));
                }
                else if (flag.equals("TieBreaker"))
                {
                    n.sethCost(Tie_Cross(n, goal));
                }

                n.setgCost(n.getCost() + curr.getgCost());
                n.setParent(curr);
                n.setfCost(n.getfCost(false));
                Map.colorOpen(n.getCoord());
                fringe[0].add(n);
            }

            double newCost = curr.getgCost() + n.getCost();
            if (doesContain(n, 0) && (newCost < curr.getCost() + n.getCost())) {
                fringe[0].remove(n);
                Map.colorOpen(n.getCoord());
                n.setgCost(newCost);
                n.setfCost(n.getfCost(false));
                n.setParent(curr);
                fringe[0].add(n);
            }
    }
}


