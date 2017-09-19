package Model;



import App.Heuristic;

import java.util.ArrayList;

public class Node
{
    private double gCost; //distance from the start vertex to vertex s
    private double hCost; //heuristic value
    private double fCost;
    private Vertex coord; //coordinates of this node on the grid
    private Node parent; //parent node of this node
    private ArrayList<Node> neighbors;
    private double cost; //cost from transitioning between two vertices
    private String type;
    public double totalCost;

    public Node(Vertex c)
    {
        this.coord = c;
        neighbors = new ArrayList<>();
        type = Heuristic.g.getGrid()[coord.getRow()][coord.getCol()];
    }

    public void calcCost(boolean HVorD, double currCost) //true for horizontal/vertical or false for diagonal
    {
        if(HVorD)
        {
            if(((parent.getType().equals("1") || parent.getType().equals("a")) && type.equals("1")) || (parent.getType().equals("1") && (type.equals("a") || type.equals("1"))))
                cost = 1;
            else if(((parent.getType().equals("1") || parent.getType().equals("a")) && type.equals("2") || (parent.getType().equals("2") && (type.equals("a") || type.equals("1")))))
                cost = 1.5;
            else if(parent.type.equals("2") && type.equals("2"))
                cost = 2;
            else if(parent.getType().equals("a") && type.equals("a"))
                cost = 0.25;
            else if((parent.getType().equals("b") && type.equals("a")) || (parent.getType().equals("b") && type.equals("a")))
                cost = 0.375;
            else if(parent.getType().equals("b") && type.equals("b"))
                cost = 0.5;
        }
        else
        {
            if(((parent.getType().equals("1") || parent.getType().equals("a")) && type.equals("1")) || (parent.getType().equals("1") && (type.equals("a") || type.equals("1"))))
                cost = Math.sqrt(2);
            else if(parent.type.equals("2") && type.equals("2"))
                cost = Math.sqrt(8);
            else if(((parent.getType().equals("1") || parent.getType().equals("a")) && type.equals("2") || (parent.getType().equals("2") && (type.equals("a") || type.equals("1")))))
                cost = ((Math.sqrt(2) + Math.sqrt(8))/2);
        }
    }

    public void findNeighbors()
    {
        if((coord.getRow() + 1) < 120 && coord.getCol() == this.coord.getCol())
        {
            Node temp = new Node(new Vertex(coord.getRow() + 1, coord.getCol()));
            if(!temp.getType().equals("0"))
            {
                temp.setParent(this);
                temp.calcCost(true, cost);
                neighbors.add(temp);
            }
        }
        if(coord.getRow() - 1 >= 0 && coord.getCol() == this.coord.getCol())
        {
            Node temp = new Node(new Vertex(coord.getRow() - 1, coord.getCol()));
            if(!temp.getType().equals("0"))
            {
                temp.setParent(this);
                temp.calcCost(true, cost);
                neighbors.add(temp);
            }
        }
        if(coord.getCol() + 1 < 160 && coord.getRow() == this.coord.getRow())
        {
            Node temp = new Node(new Vertex(coord.getRow(), coord.getCol() + 1));
            if(!temp.getType().equals("0"))
            {
                temp.setParent(this);
                temp.calcCost(true, cost);
                neighbors.add(temp);
            }
        }
        if(coord.getCol() - 1 >= 0 && coord.getRow() == this.coord.getRow())
        {
            Node temp = new Node(new Vertex(coord.getRow(), coord.getCol() - 1));
            if(!temp.getType().equals("0"))
            {
                temp.setParent(this);
                temp.calcCost(true, cost);
                neighbors.add(temp);
            }
        }
        if(coord.getRow() + 1 < 120 && coord.getCol() + 1 < 160)
        {
            Node temp = new Node(new Vertex(coord.getRow() + 1, coord.getCol() + 1));
            if(!temp.getType().equals("0"))
            {
                temp.setParent(this);
                temp.calcCost(false, cost);
                neighbors.add(temp);
            }
        }
        if(coord.getRow() + 1 < 120 && coord.getCol() - 1 >= 0)
        {
            Node temp = new Node(new Vertex(coord.getRow() + 1, coord.getCol() - 1));
            if(!temp.getType().equals("0"))
            {
                temp.setParent(this);
                temp.calcCost(false,cost);
                neighbors.add(temp);
            }
        }
        if(coord.getRow() - 1 >= 0 && coord.getCol() + 1 < 160)
        {
            Node temp = new Node(new Vertex(coord.getRow() - 1, coord.getCol() + 1));
            if(!temp.getType().equals("0"))
            {
                temp.setParent(this);
                temp.calcCost(false,cost);
                neighbors.add(temp);
            }
        }
        if(coord.getRow() - 1 >= 0 && coord.getCol() - 1 >= 0)
        {
            Node temp = new Node(new Vertex(coord.getRow() - 1, coord.getCol() - 1));
            if(!temp.getType().equals("0"))
            {
                temp.setParent(this);
                temp.calcCost(false, cost);
                neighbors.add(temp);
            }
        }
    }

    public double getfCost(boolean flag)
    {
        if(flag)
        {
            return this.fCost;
        }
        else
        {
            return (gCost + hCost);
        }
    }

    public double getgCost()
    {
        return gCost;
    }

    public double gethCost()
    {
        return hCost;
    }

    public Vertex getCoord()
    {
        return coord;
    }

    public Node getParent()
    {
        return parent;
    }

    public double getCost()
    {
        return cost;
    }

    public ArrayList<Node> getNeighbors()
    {
        return  neighbors;
    }

    public String getType()
    {
        return type;
    }

    public void setgCost(double g)
    {
        this.gCost = g;
    }

    public void sethCost(double h)
    {
        this.hCost = h;
    }

    public void setfCost(double f)
    {
        this.fCost = fCost;
    }

    public void setCoord(Vertex c)
    {
        this.coord = c;
    }

    public void setParent(Node p)
    {
        this.parent = p;
    }

    public void setCost(double c)
    {
        this.cost = c;
    }
}
