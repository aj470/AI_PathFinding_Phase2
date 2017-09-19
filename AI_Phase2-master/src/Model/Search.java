package Model;

import App.Heuristic;
import View.*;
import View.Map;

import java.util.*;

public abstract class Search
{
    public PriorityQueue<Node>[] fringe;
    public HashSet<Node>[] closedSet;
    public ArrayList<Node> path = new ArrayList<>();
    String flag;
    Node start;
    Node goal;
    Node curr = null;
    Grid grid = Heuristic.g;
    boolean found = false;
    boolean isMultiHeuristic = false;
    long timeS, timeE;

    public Search(boolean flag)
    {
        this.isMultiHeuristic = flag;
        if(!isMultiHeuristic)
        {
            fringe = new PriorityQueue[1];
            closedSet = new HashSet[1];
        }
        else
        {
            fringe = new PriorityQueue[5];
            closedSet = new HashSet[5];
        }

    }
    //method to be implemented in subclasses
    public abstract void checkCurrCosts(Node n, int index);

    public void init(Comparator comp)
    {
        path.add(start);
        start = new Node(grid.getsVertex().get(Map.vert));
        start.setParent(start);
        goal = new Node(grid.getgVertex().get(Map.vert));

        if(!isMultiHeuristic)
        {
            closedSet[0] = new HashSet<>();
            fringe[0] = new PriorityQueue<Node>(comp);
            fringe[0].add(start);
        }
        curr = null;
        //start timer
        timeS = System.currentTimeMillis();
    }

    public boolean doesContain(Node n, int index)
    {
        for (Node m : fringe[index])
        {
            if ((n.getCoord().getRow() == m.getCoord().getRow()) && (n.getCoord().getCol()) == m.getCoord().getCol())
            {
                return true;
            }
        }
        return false;
    }

    public boolean inClosed(Node n, int index)
    {
        for (Node m : closedSet[index])
        {
            if ((n.getCoord().getRow() == m.getCoord().getRow()) && (n.getCoord().getCol()) == m.getCoord().getCol())
            {
                //System.out.println(n.getCoord().getRow() + " " + n.getCoord().getCol());
                return true;
            }
        }
        return false;
    }

    public long getRunTime() {
        return timeE - timeS;
    }

    public ArrayList<Node> getPath() {
        return path;
    }

    public PriorityQueue<Node> getFringe(int index)
    {
        return fringe[index];
    }

    public HashSet<Node> getClosedSet(int index) {
        return closedSet[index];
    }

    public void findPath(String flag, int index)
    {
        this.flag = flag;
        while (!fringe[index].isEmpty())
        {
            curr = fringe[index].poll(); // get next node in the priority queue
            Map.colorClosed(curr.getCoord());
            closedSet[index].add(curr);

            if (curr.getCoord().getRow() == goal.getCoord().getRow() && curr.getCoord().getCol() == goal.getCoord().getCol())
            {
                //stop timer
                timeE = System.currentTimeMillis();
                Map.timerlabel.setText("Time: " + getRunTime() + "ms");
                System.out.println(path.size() - 1);
                found = true;
                break;
            }

            curr.findNeighbors();
            for(Node n: curr.getNeighbors())
            {
                checkCurrCosts(n, index);
            }
        }

        if (found)
        {
            while (curr != start)
            {
                Map.colorFinal(curr.getCoord());
                curr = curr.getParent();
                path.add(curr);
            }
            System.out.println(path.size());
        }
    }

    public double Man_distance(Node current, Node goal)
    {
        int x1 = current.getCoord().getRow();
        int y1 = current.getCoord().getCol();
        int x2 = goal.getCoord().getRow();
        int y2 = goal.getCoord().getCol();

        return ((Math.abs(x2-x1)+Math.abs(y2-y1)));
    }

    public double bird_eye(Node current, Node goal)
    {
        int x1 = current.getCoord().getRow();
        int y1 = current.getCoord().getCol();
        int x2 = goal.getCoord().getRow();
        int y2 = goal.getCoord().getCol();

        return Math.sqrt(Math.pow(2,(x2-x1))+Math.pow(2, (y2-y1)));
    }

    public double diagonal(Node current, Node goal)
    {
        double x = Math.abs(current.getCoord().getRow() - goal.getCoord().getRow());
        double y = Math.abs(current.getCoord().getCol() - goal.getCoord().getCol());
        return  2 * (x + y) + (Math.sqrt(2)  - 2 ) * Math.min(x,y );
    }
    public double Chebyshev(Node current, Node goal)
    {
        return Math.max(Math.abs(current.getCoord().getRow() - goal.getCoord().getRow()), Math.abs(current.getCoord().getCol() - goal.getCoord().getCol()));

    }

    public double Tie_Cross(Node current, Node goal)
    {
        double x1 = current.getCoord().getRow() - goal.getCoord().getRow();
        double y1 = current.getCoord().getCol() - goal.getCoord().getCol();
        double x2 = current.getCoord().getRow() - goal.getCoord().getRow();
        double y2 =  current.getCoord().getCol() - goal.getCoord().getCol();
        double evaluate = Math.abs(x1*y2 - x2*y1);
        evaluate += (evaluate * 0.02);
        return evaluate;

        //double heuristic = Math.sqrt(2) * Math.min((Math.abs(start.getCoord().getRow() - goal.getCoord().getRow())), (Math.abs(current.getCoord().getCol()- goal.getCoord().getCol())));// - min(abs(startx - goalx), abs(starty - goaly))
        //return heuristic *(1.0 + (0.25/160));//return evaluate;
    }

}
