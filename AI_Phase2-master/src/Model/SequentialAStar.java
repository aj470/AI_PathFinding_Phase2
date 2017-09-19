package Model;

import View.Map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class SequentialAStar extends Astar
{
    protected String[] heuristics = {"Manhattan Distance", "Bird Eye", "Diagonal", "Chebyshev", "TieBreaker"};
    protected HashMap<Node, Double> [] gValues;
    protected HashMap<Node, Double> [] fValues;
    protected HashMap<Node, Node> [] parentPointers;
    protected double w1 = 1.5, w2 = 3;
    int lastHeur;

    public SequentialAStar(boolean flag)
    {
        super(flag);

        gValues = (HashMap<Node, Double>[]) new HashMap[5];
        fValues = (HashMap<Node, Double>[]) new HashMap[5];
        parentPointers = (HashMap<Node, Node>[]) new HashMap[5];
    }

    public void seqAStar()
    {
        for (int currHeur = 0; currHeur < 5; currHeur++)
        {
            fringe[currHeur] = new PriorityQueue(new AstarComparator());
            closedSet[currHeur] = new HashSet();
            gValues[currHeur] = new HashMap<Node, Double>();
            fValues[currHeur] = new HashMap<Node, Double>();
            parentPointers[currHeur] = new HashMap<Node, Node>();

            gValues[currHeur].put(start, 0.0);
            gValues[currHeur].put(goal, Double.POSITIVE_INFINITY);

            parentPointers[currHeur].put(start, null);
            parentPointers[currHeur].put(goal, null);

            start.setfCost(key(start, currHeur));
            fValues[currHeur].put(start, start.getfCost(true));
            Map.colorOpen(start.getCoord());
            fringe[currHeur].add(start);
        }
OUTER:  while (fValues[0].get(fringe[0].peek()) < 1000)
        {
            for (int currHeur = 1; currHeur < 5; currHeur++)
            {
                double inadMinKey = fValues[currHeur].get(fringe[currHeur].peek());

                if (inadMinKey <= w2 * fValues[0].get(fringe[0].peek()))
                {
                    System.out.println("I am here always " + currHeur);
                    if (gValues[currHeur].get(goal) <= inadMinKey)
                    {
                        System.out.println("I am here always2" + currHeur);
                        if (gValues[currHeur].get(goal) < Double.POSITIVE_INFINITY)
                        {
                            System.out.println("Path found!");
                            lastHeur = currHeur;
                            found = true;
                            timeE = System.currentTimeMillis();
                            Map.timerlabel.setText("Time: " + getRunTime() + "ms");
                            System.out.println(path.size() - 1);
                            break OUTER;
                            //System.out.println("Path found!");
                            //nodesExpanded = closedSet[currHeur].size();
                            //return returnPath(goal, currHeur);
                        }
                    }
                    else
                    {
                        Node currentNode = fringe[currHeur].poll();
                        flag = heuristics[currHeur];
                        checkCurrCosts(currentNode, currHeur);
                        Map.colorClosed(currentNode.getCoord());
                        closedSet[currHeur].add(currentNode);
                    }
                }
                else
                { // if inadMinKey < w2 * anchorMinKey
                    if (gValues[0].get(goal) <= fValues[0].get(fringe[0].peek()))
                    {
                        if (gValues[0].get(goal) == goal.getgCost())//< Double.POSITIVE_INFINITY)
                        {
                            System.out.println("Path found!");
                            lastHeur = currHeur;
                            found = true;
                            timeE = System.currentTimeMillis();
                            Map.timerlabel.setText("Time: " + getRunTime() + "ms");
                            System.out.println(path.size() - 1);
                            break OUTER;

                            //nodesExpanded = closedSet[currHeur].size();
                            //return returnPath(goal, 0);
                        }
                    }
                    else
                    {
                        Node currentNode = fringe[0].poll();
                        flag = heuristics[currHeur];
                        checkCurrCosts(currentNode, 0);
                        Map.colorClosed(currentNode.getCoord());
                        closedSet[0].add(currentNode);
                    }
                }
            }
        }
        if(found)
        {
            curr = goal;
            while (curr != start)
            {
                Map.colorFinal(curr.getCoord());
                curr = parentPointers[lastHeur].get(curr);
                path.add(curr);
            }
            System.out.println(path.size());
        }
        //System.out.println("NO PATH!");
        //return null;
    }

    protected double key (Node currentNode, int currHeur)
    {
        heuristicSwitch(heuristics[currHeur], currentNode);
        return gValues[currHeur].get(currentNode) + (w1 * currentNode.gethCost());
    }

    @Override
    public void checkCurrCosts(Node n, int currHeur)
    {
        fringe[currHeur].remove(n);
        if(n.getNeighbors().isEmpty())
        {
            n.findNeighbors();
        }
        for(Node m : n.getNeighbors())
        {
            if(!doesContain(m, currHeur) && !inClosed(m, currHeur))
            {
                gValues[currHeur].put(m, Double.POSITIVE_INFINITY);
                parentPointers[currHeur].put(m, null);
            }
            if(gValues[currHeur].get(m) > (gValues[currHeur].get(n) + n.getCost()))
            {
                gValues[currHeur].put(m, gValues[currHeur].get(n) + n.getCost());
                parentPointers[currHeur].put(m, n);
                if(!inClosed(m, currHeur))
                {
                    if(!doesContain(m, currHeur))
                    {
                        fringe[currHeur].remove(m);
                    }
                    m.setfCost(key(m, currHeur));
                    fValues[currHeur].put(m, m.getfCost(true));
                    fringe[currHeur].add(m);
                    Map.colorOpen(m.getCoord());
                }
            }
        }

        /*if(n.getNeighbors().isEmpty())
        {
            n.findNeighbors();
        }
        for (Node m : n.getNeighbors())
        {
            if (!fringe[currHeur].contains(m) && !closedSet[currHeur].contains(m)) {
                gValues[currHeur].put(m, Double.POSITIVE_INFINITY);
                parentPointers[currHeur].put(m, null);
            }

            double tentativeGScore = gValues[currHeur].get(n) + m.getgCost();

            if (gValues[currHeur].get(m) > tentativeGScore ) {

                //System.out.println(neighbor.getCoords() + ": " + gValues[currHeur].get(neighbor));
                gValues[currHeur].replace(m, tentativeGScore);
                parentPointers[currHeur].replace(m, n);

                if (!closedSet[currHeur].contains(m)) {
                    m.setfCost(key(m, currHeur));
                    fValues[currHeur].put(m, m.getfCost());
                    if (!fringe[currHeur].contains(m))
                    {

                        fringe[currHeur].add(m);
                        Map.colorOpen(m.getCoord());
                    }
                    else if(fringe[currHeur].peek().equals(m))
                    {
                        fringe[currHeur].remove(m);
                        fringe[currHeur].add(m);
                    }
                }
            }
        }*/
    }

    public void heuristicSwitch(String heuristic, Node startNode) {
        switch(heuristic) {
            case "Manhattan Distance":
                startNode.sethCost(Man_distance(startNode, goal));
                break;
            case "Bird Eye":
                startNode.sethCost(bird_eye(startNode, goal));
                break;
            case "Diagonal":
                startNode.sethCost(diagonal(startNode, goal));
                break;
            case "Chebyshev":
                startNode.sethCost(Chebyshev(startNode, goal));
                break;
            case "TieBreaker":
                startNode.sethCost(Tie_Cross(startNode, goal));
                break;
        }
    }
}
