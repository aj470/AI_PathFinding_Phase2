package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Grid implements Serializable
{
    private int NUM_VERTICES = 10;
    private String name;
    private String[][] grid;
    private int numHW = 0; //no of Highways
    //View.Map Size
    private int ROWS = 120;
    private int COLS = 160;
    //start and end pairs of vertices
    private ArrayList<Vertex> sVertex= new ArrayList<>();
    private ArrayList<Vertex> gVertex = new ArrayList<>();
    //center points of hard to traverse cells
    private ArrayList<Vertex> hVertices = new ArrayList<Vertex>();

    public Grid(String n)
    {
        name = n;
        grid = new String[120][160];
        for (int i=0; i < ROWS; i++)
        {
            for (int j = 0; j < COLS; j++)
            {
                //System.out.println(i + " " + j);
                grid[i][j] = "1";
            }
        }

        // Algorithms
        setGrid();
        sethighways();
        setBlocked();

        Vertex temp;
        for(int i = 0; i < NUM_VERTICES; i++)
        {
            temp = setVertex();
            if(!sVertex.contains(temp))
            {
                sVertex.add(temp);
            }
        }

        int dist;
        for(int i = 0; i < NUM_VERTICES; i++)
        {
            temp = setVertex();
            dist = (Math.abs(temp.getRow() - sVertex.get(i).getRow())) + (Math.abs(temp.getCol() - sVertex.get(i).getCol()));
            if(dist > 100 && !sVertex.contains(temp))
            {
                gVertex.add(temp);
            }
            else
            {
                i--;
            }
        }
    }

    public Grid(String n, String[][] g)
    {
        name = n;
        grid = g;
    }

    private void setGrid()
    {
        Random rand = new Random();

        int count = 0;
        int r;
        int c;
        while(count < 8)
        {
            r = rand.nextInt(ROWS);
            c = rand.nextInt(COLS);
            if (grid[r][c].equals("1"))
            {
                grid[r][c] = "temp"; //set point and flag with Cyan color
                hVertices.add(new Vertex(r, c));
                count++;
            }
        }

        // Fill the 31*31 area with hard to traverse grid and flag with Red color.
        for (int i = 0; i < ROWS; i++)
        {
            for (int j = 0; j < COLS; j++)
            {
                if (grid[i][j].equals("temp"))
                {
                    for (int k = -15; k <= 15 ; k++)
                    {
                        for (int l = -15; l <= 15; l++)
                        {
                            // In boundary cases
                            try
                            {
                                if (!(grid[i+k][j+l].equals("temp")))
                                {
                                    int x = rand.nextInt(2);
                                    if(x == 1)
                                    {
                                        grid[i+k][j+l] = "2";
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                //
                            }
                        }
                    }
                    grid[i][j] = "1";
                }
            }
        }
    }
    public void sethighways() // call to randomly find starting point of 4 highways
    {
        Random rand = new Random();
        while(numHW < 4) // loop for the 4 unique highway which doesn't collide
        {
            int choice = rand.nextInt(4);
            if (choice == 0)
            {
                int col = rand.nextInt(159);
                //System.out.println(col);
                highway(0, col);
            }
            if (choice == 1)
            {
                int col = rand.nextInt(159);
                //System.out.println(col);
                highway(119, col);
            }
            if (choice == 2)
            {
                int row = rand.nextInt(119);
                //System.out.println(row);
                highway(row, 0);
            }
            if (choice == 3)
            {
                int row = rand.nextInt(119);
                //System.out.println(row);
                highway(row, 159);
            }
        }
    }

    private void highway(int row, int col) // set randomly one highway at a time
    {
        ArrayList<String> k = new ArrayList<String>();

        Random r = new Random();
        int next;
        int hwLen = 0;

        if(row == 0)
        {
            for(int i = 0 ; i < 20 ;i++)
            {
                if(grid[row+i][col].equals("a") || grid[row + i][col].equals("b"))
                {
                    undo(k);
                    return;
                }
                else if(grid[row+i][col].equals("2"))
                {
                    grid[row+i][col] = "b";
                }
                else if(grid[row + i][col].equals("1"))
                {
                    grid[row+i][col] = "a";
                }
                k.add(hwLen,((row+i)+ " " +col));
                hwLen++;
            }
            row += hwLen;
            while(row > 0 && row < 119 && col > 0 && col < 159)
            {
                next = r.nextInt(10);
                if (next < 6)
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if(row + i > 119)
                            break;
                        if(grid[row+i][col].equals("a") || grid[row + i][col].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row+i][col].equals("2"))
                        {
                            grid[row+i][col] = "b";
                        }
                        else if(grid[row + i][col].equals("1"))
                        {
                            grid[row+i][col] = "a";
                        }
                        k.add(hwLen,((row+i)+ " " +col));
                        hwLen++;
                    }
                    row += i;
                }
                else if (next >= 6 || next <= 7)
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if (col + i > 159)
                            break;
                        if(grid[row][col+i].equals("a") || grid[row][col + i].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row][col + i].equals("2"))
                        {
                            grid[row][col + i] = "b";
                        }
                        else if(grid[row][col + i].equals("1"))
                        {
                            grid[row][col + i] = "a";
                        }
                        k.add(hwLen,(row + " " + (col+i)));
                        hwLen++;
                    }
                    col += i;
                }
                else
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if (col - i< 0)
                            break;
                        if(grid[row][col - i].equals("a") || grid[row][col - i].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row][col - i].equals("2"))
                        {
                            grid[row][col - i] = "b";
                        }
                        else if(grid[row][col - i].equals("1"))
                        {
                            grid[row][col - i] = "a";
                        }
                        k.add(hwLen,(row + " " + (col - i)));
                        hwLen++;
                    }
                    col -= i;
                }
            }
            //System.out.println(hwLen);
        }
        if(row == 119)
        {
            for(int i = 0 ; i<20 ;i++)
            {
                if(grid[row - i][col].equals("a") || grid[row - i][col].equals("b"))
                {
                    undo(k);
                    return;
                }
                else if(grid[row - i][col].equals("2"))
                {
                    grid[row - i][col] = "b";
                }
                else if(grid[row - i][col].equals("1"))
                {
                    grid[row - i][col] = "a";
                }
                k.add(hwLen,((row-i) + " " + col));
                hwLen++;
            }
            row -= hwLen;
            while(row > 0 && row < 119 && col > 0 && col < 159)
            {
                next = r.nextInt(10);
                if (next < 6)
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if(row - i < 0)
                            break;
                        if(grid[row - i][col].equals("a") || grid[row - i][col].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row - i][col].equals("2"))
                        {
                            grid[row - i][col] = "b";
                        }
                        else if(grid[row - i][col].equals("1"))
                        {
                            grid[row - i][col] = "a";
                        }
                        k.add(hwLen,(row - i + " " + (col)));
                        hwLen++;
                    }
                    row -= i;
                }
                else if (next >= 6 || next <= 7)
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if ((col + i) > 159)
                            break;
                        if(grid[row][col + i].equals("a")|| grid[row][col + i].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row][col + i].equals("2"))
                        {
                            grid[row][col + i] = "b";
                        }
                        else if(grid[row][col + i].equals("1"))
                        {
                            grid[row][col + i] = "a";
                        }
                        k.add(hwLen,(row + " " + (col+i)));
                        hwLen++;
                    }
                    col += i;
                }
                else
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if (col - i< 0)
                            break;
                        if(grid[row][col - i].equals("a")|| grid[row][col - i].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row][col - i].equals("2"))
                        {
                            grid[row][col - i] = "b";
                        }
                        else if(grid[row][col - i].equals("1"))
                        {
                            grid[row][col - i] = "a";
                        }
                        k.add(hwLen,((row)+ " " +(col-i)));
                        hwLen++;
                    }
                    col -= i;
                }
            }
            // System.out.println(hwLen);

        }
        if(col==0)
        {
            for(int j = 0; j<20 ; j++)
            {
                if(grid[row][col + j].equals("a") || grid[row][col + j].equals("b"))
                {
                    undo(k);
                    return;
                }
                else if(grid[row][col + j].equals("2"))
                {
                    grid[row][col + j] = "b";
                }
                else if(grid[row][col + j].equals("1"))
                {
                    grid[row][col + j] = "a";
                }
                k.add(hwLen,((row)+ " " +(col +j)));
                hwLen++;
            }
            col += hwLen;
            while(row > 0 && row < 119 && col > 0 && col < 159)
            {
                next = r.nextInt(10);
                if (next < 6)
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if(col + i > 159)
                            break;
                        if(grid[row][col + i].equals("a") || grid[row][col + i].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row][col + i].equals("2"))
                        {
                            grid[row][col + i] = "b";
                        }
                        else if(grid[row][col + i].equals("1"))
                        {
                            grid[row][col + i] = "a";
                        }
                        k.add(hwLen,((row)+ " " +(col+i)));
                        hwLen++;
                    }
                    col += i;
                }
                else if (next >= 6 || next <= 7)
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if(row - i < 0)
                            break;
                        if(grid[row - i][col].equals("a")|| grid[row - i][col].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row - i][col].equals("2"))
                        {
                            grid[row - i][col] = "b";
                        }
                        else if(grid[row - i][col].equals("1"))
                        {
                            grid[row - i][col] = "a";
                        }
                        k.add(hwLen,((row-i)+ " " +col));
                        hwLen++;
                    }
                    row -= i;
                }
                else
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if(row + i > 119)
                            break;
                        if(grid[row + i][col].equals("a") || grid[row + i][col].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row + i][col].equals("2"))
                        {
                            grid[row + i][col] = "b";
                        }
                        else if(grid[row + i][col].equals("1"))
                        {
                            grid[row + i][col] = "a";
                        }
                        k.add(hwLen,((row+i)+ " " +col));
                        hwLen++;
                    }
                    row += i;
                }
            }
            // System.out.println(hwLen);
        }
        if(col==159)
        {
            for(int j = 0; j<20 ; j++)
            {
                if(grid[row][col - j].equals("a")|| grid[row][col - j].equals("b"))
                {
                    undo(k);
                    return;
                }
                else if(grid[row][col - j].equals("2"))
                {
                    grid[row][col - j] = "b";
                }
                else if(grid[row][col - j].equals("1"))
                {
                    grid[row][col - j] = "a";
                }
                k.add(hwLen,((row)+ " " +(col-j)));
                hwLen++;
            }
            col -= hwLen;
            while(row > 0 && row < 119 && col > 0 && col < 159)
            {
                next = r.nextInt(10);
                if (next < 6)
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if(col - i < 0)
                            break;
                        if(grid[row][col - i].equals("a") || grid[row][col - i].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row][col - i].equals("2"))
                        {
                            grid[row][col - i] = "b";
                        }
                        else if(grid[row][col - i].equals("1"))
                        {
                            grid[row][col - i] = "a";
                        }
                        k.add(hwLen,((row)+ " " +(col-i)));
                        hwLen++;
                    }
                    col -= i;
                }
                else if (next >= 6 || next <= 7)
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if(row - i < 0)
                            break;
                        if(grid[row - i][col].equals("a") || grid[row - i][col].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row - i][col].equals("2"))
                        {
                            grid[row - i][col] = "b";
                        }
                        else if(grid[row - i][col].equals("1"))
                        {
                            grid[row - i][col] = "a";
                        }
                        k.add(hwLen,((row-i)+ " " +col));
                        hwLen++;
                    }
                    row -= i;
                }
                else
                {
                    int i;
                    for (i = 0; i < 20; i++)
                    {
                        if(row + i > 119)
                            break;
                        if(grid[row + i][col].equals("a") || grid[row + i][col].equals("b"))
                        {
                            undo(k);
                            return;
                        }
                        else if(grid[row + i][col].equals("2"))
                        {
                            grid[row + i][col] = "b";
                        }
                        else if(grid[row + i][col].equals("1"))
                        {
                            grid[row + i][col] = "a";
                        }
                        k.add(hwLen,((row + i)+ " " +col));
                        hwLen++;
                    }
                    row += i;
                }
            }
        }
        if(hwLen > 100)
        {
            numHW++;
        }
        else
        {
            undo(k);
        }
    }

    public void undo(ArrayList<String> p)
    {
        for(int z = 0; z < p.size(); z++)
        {
            String cord = p.get(z);
            String[] arr = cord.split(" ", 2);
            int x = Integer.parseInt(arr[0]);
            int y = Integer.parseInt(arr[1]);

            if(grid[x][y].equals("b"))
            {
                grid[x][y] = "2";
            }
            else if(grid[x][y].equals("a"))
            {
                grid[x][y] = "1";
            }
            else
            {
                grid[x][y] = "1";
            }
        }
    }

    public void setBlocked() // set the random blocked grids.
    {
        Random rand = new Random();
        int row, col, count = 0;

        while(count < 3840)
        {
            row = rand.nextInt(120);
            col = rand.nextInt(160);

            if(grid[row][col].equals("1") || grid[row][col].equals("2") )
            {
                grid[row][col] = "0";
                count++;
            }
        }
    }

    public Vertex setVertex()
    {
        Random rand = new Random();
        int r, c;
        boolean found = false;

        while(true)
        {
            r = rand.nextInt(120);
            c = rand.nextInt(160);
            if(r < 20 || r >= 100 && !grid[r][c].equals("0"))
            {
                break;
            }
            else if(c < 20 || c >= 140 && !grid[r][c].equals("0"))
            {
                break;
            }

        }
        return new Vertex(r,c);
    }

    public String getName()
    {
        return name;
    }

    public String[][] getGrid()
    {
        return  grid;
    }

    public ArrayList<Vertex> getsVertex()
    {
        return sVertex;
    }

    public ArrayList<Vertex> getgVertex()
    {
        return gVertex;
    }

    public ArrayList<Vertex> gethVertices()
    {
        return hVertices;
    }

    public void setsVertex(ArrayList<Vertex> s)
    {
        this.sVertex = s;
    }

    public void setgVertex(ArrayList<Vertex> g)
    {
        this.gVertex = g;
    }

    public void sethVertices(ArrayList<Vertex> h)
    {
        this.hVertices = h;
    }

    public int getNumVertices()
    {
        return NUM_VERTICES;
    }
}

