package App;

import Model.Backend;
import Model.Grid;
import View.Map;

public class Heuristic
{
    private static Map frame;
    public static Grid g;

    public static void main(String[] args) throws InterruptedException {
        Backend.loadProject();
        if (Backend.grids.isEmpty())
        {
            g = new Grid("First");
            Backend.grids.add(g);
            Backend.grids.add(new Grid("Second"));
            Backend.grids.add(new Grid("Third"));
            Backend.grids.add(new Grid("Fourth"));
            Backend.grids.add(new Grid("Fifth"));
            Model.Backend.export(g);
            //Model.Backend.importFile("First.txt");
            g = Backend.grids.get(0);
            Backend.export(g);
            Backend.save();
        }
        else
        {
            g = Backend.grids.get(0);
        }
        frame = new Map("First");
        frame.main(frame, "Sequential A*", "");
    }
}
