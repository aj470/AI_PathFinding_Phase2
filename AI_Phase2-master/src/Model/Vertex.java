package Model;

import java.io.Serializable;

public class Vertex implements Serializable
{
    private int row;
    private int col;


    public Vertex(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }
}
