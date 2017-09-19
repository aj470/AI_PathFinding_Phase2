package Model;

import java.awt.Color;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Backend
{
    /**
     * list of users
     */
    public static ArrayList<Grid> grids = new ArrayList<Grid>();
    /**
     * directory of data file
     */
    private static final String dir = "data";
    /**
     * name of data file
     */
    private static final String filename = "data.dat";


    /**
     * get directory of data file
     * @return directory name
     */
    public static String getDir()
    {
        return dir;
    }

    /**
     * get data file name
     * @return file name
     */
    public static String getFilename()
    {
        return filename;
    }

    public static ArrayList<Grid> getGrids()
    {
        return grids;
    }

    public static Grid Searchedgrid(String s)
    {
        for(int i = 0; i < grids.size();i++ )
        {
            if(grids.get(i).getName().equals(s))
            {
                return grids.get(i);
            }
        }
        return null;
    }

    public static void save()
    {

        File file = new File(getDir());
        boolean isDirectoryCreated = file.exists();
        if (!isDirectoryCreated)
        {
            isDirectoryCreated = file.mkdir();
        }
        try
        {
            if(isDirectoryCreated)
            {
                FileOutputStream fo = new FileOutputStream(getDir() + File.separator + getFilename());
                ObjectOutputStream out = new ObjectOutputStream(fo);
                out.writeObject(grids);
                out.close();
                fo.close();
                //System.out.println("User list saved.");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadProject()
    {
        try
        {
            FileInputStream fis = new FileInputStream(getDir() + File.separator + getFilename());
            ObjectInputStream ois = new ObjectInputStream(fis);
            grids = (ArrayList<Grid>) ois.readObject();
            if (grids == null)
            {
                grids = new ArrayList<Grid>();
            }
            ois.close();
            fis.close();
            //System.out.println("User list loaded");
        }
        catch(FileNotFoundException e)
        {
            grids = new ArrayList<Grid>();
            //System.out.println("User list created");
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void export(Grid g)
    {
        boolean found = false;

        //search for map in list
        for(Grid x : grids)
        {
            //if found, set boolean value and write to file mapname.txt
            if(x.getName().equals(g.getName()))
            {
                found = true;
                File fileName = new File(g.getName() + ".txt");
                String[][] grid = g.getGrid();
                ArrayList<Vertex> s = g.getsVertex();
                ArrayList<Vertex> goal = g.getgVertex();
                ArrayList<Vertex>  h = g.gethVertices();

                try
                {
                    FileWriter fw = new FileWriter(fileName);
                    Writer output = new BufferedWriter(fw);

                    //write start and end vertices to file
                    for(int i = 0; i < s.size(); i++)
                    {
                        output.write(s.get(i).getRow() + "," + s.get(i).getCol() + ",");
                    }
                    output.write("\n");

                    for(int i = 0; i < goal.size(); i++)
                    {
                        output.write(goal.get(i).getRow() + "," + goal.get(i).getCol() + ",");
                    }
                    output.write("\n");

                    //write center points of hard to traverse areas to file
                    for(Vertex v : h)
                    {
                        output.write(v.getRow() + "," + v.getCol() + ",");
                        output.write("\n");
                    }

                    //write grid to file
                    for(int i = 0; i < grid.length; i++)
                    {
                        for(int j = 0; j < grid[i].length; j++)
                        {
                            //System.out.println(i+ " " + j);
                            output.write(grid[i][j]);
                        }
                        output.write("\n");
                    }
                    output.close();
                } catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        if(!found)
        {
            //throw error
        }
    }

    public static void importFile(String fileName)
    {
        String[][] grid = new String[120][160];
        ArrayList<Vertex> temp = new ArrayList<>();
        ArrayList<Vertex> temp2 = new ArrayList<>();
        ArrayList<Vertex> temp3 = new ArrayList<>();

        String line;
        int i = 0;

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            if(!input.ready())
            {

            }

            //read start vertices from file
            if((line = input.readLine()) != null)
            {
                String[] info = line.split("\\,");
                for(int x = 0; x < info.length; x+=2)
                {
                    temp.add(new Vertex(Integer.parseInt(info[x]),Integer.parseInt(info[x+1])));
                }
                if(temp.isEmpty())
                {
                    //throw error
                }
            }

            //read goal vertices from file
            if((line = input.readLine()) != null)
            {
                String[] info = line.split("\\,");
                for(int x = 0; x < info.length; x+=2)
                {
                    temp2.add(new Vertex(Integer.parseInt(info[x]),Integer.parseInt(info[x+1])));
                }
                if(temp2.isEmpty())
                {
                    //throw error
                }
            }

            //read center of hard to traverse cells from file
            int count = 0;
            while(count < 8)
            {
                line = input.readLine();
                if(line == null)
                {
                    break;
                }
                String[] info = line.split("\\,");
                temp3.add(new Vertex(Integer.parseInt(info[0]),Integer.parseInt(info[1])));
                count++;
            }


            // int lineCount = 0;
            outerloop:
            while ((line = input.readLine()) != null && i < 120)
            {
                // lineCount++;

                for(int j = 0; j < 160; j++)
                {
                    String[] info = line.split("");
                    if(info[j] != null)
                    {
                        //System.out.println(i + " " + j);
                        grid[i][j] = info[j];
                    }
                    else
                    {
                        //invalid file format
                        break outerloop;
                    }
                }
                i++;
            }

            Grid g = new Grid("Imported", grid);
            g.setsVertex(temp);
            g.setgVertex(temp2);
            g.sethVertices(temp3);
            grids.add(g);
        }
        catch(FileNotFoundException e)
        {

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getNames()
    {
        ArrayList<String> temp = new ArrayList<>();

        for(Grid g : grids)
        {
            temp.add(g.getName());
        }
        return temp;
    }

    public static Grid getGrid(String s)
    {
        for(Grid g : grids)
        {
            if(g.getName().equals(s))
            {
                return g;
            }
        }

        //not found
        return null;
    }

}
