package View;


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Model.*;
import App.*;

@SuppressWarnings("serial")
public class Map extends JFrame implements ActionListener, ItemListener
{
    static JLabel[][] cell;  // The Grid JLabel
    private JPanel panel1;  // Top panel containing labels and a smile button
    private JPanel panel2;  // Bottom panel containing the grid of buttons
    String heur[] = {"Select Heuristic...","Manhattan Distance","Bird Eye", "Diagonal", "Chebyshev Distance", "TieBreaker"};
    String algorithm[] = {"Select Algorithm...","A*", "Weighted A*", "UCS", "Sequential A*"};
    Search mySearch;
    public static int vert;

    JComboBox<String> Algo = new JComboBox<>(algorithm);
    JComboBox<String> mapbox = new JComboBox<>();
    JComboBox<String> heu = new JComboBox<>(heur);
    public static JLabel  l1, timer, timerlabel,fcost, gcost, hcost;
    JButton run, save, generate, export, open, weight;

    public String applyalgo, applymap, applyheu;


	/*color reference
	background GREEN = unblocked cell
	foreground CYAN = 8 points
	background RED = hard to traverse
	foreground RED = hard to traverse
	background BLUE = highway
	background BLACK = blocked cell
	*/

    //no of Highways
    int numHW = 0;

    //Map Size
    int s1 = 120;
    int s2 = 160;

    int temp = 0;

    public Map(String name)
    {
        this.setSize(1550, 900);
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - getWidth()) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2);

        this.setResizable(true);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setTitle("Assignment 1 : Heuristic Search Algorithms - " + "Map : "+ name);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    public void main(Map frame, String algo, String h) throws InterruptedException {
        applyalgo = algo;
        applyheu = h;
        //panel creation and installing listeners
        MyMouseListener myMouseListener = new MyMouseListener(frame);
        JPanel mainPanel = new JPanel();
        mainPanel.getMaximumSize();

        panel1 = new JPanel();
        panel2 = new JPanel();

        //Controller setup

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        BoxLayout g1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
        panel1.setLayout(g1);


        Font f1=new Font("Times New Roman",Font.BOLD,20);
        Font f2=new Font("Times New Roman",Font.BOLD,13);
        Font f3=new Font("Times New Roman",Font.BOLD,16);


        JLabel l1=new JLabel("Control Panel : ");
        panel1.add(Box.createRigidArea(new Dimension(0,0)));
        l1.setFont(f1);
        panel1.add(l1, BorderLayout.PAGE_START);

        Dimension d1 = l1.getMaximumSize();

        ArrayList<String> Maps = new ArrayList<>();
        if(!Backend.grids.isEmpty())
        {
            Maps.addAll(Backend.getNames());
        }
        //JComboBox<String> mapbox = new JComboBox<String>();
        mapbox.setModel(new DefaultComboBoxModel(Maps.toArray()));
        mapbox.setFont(f2);
        mapbox.addActionListener(this);
        mapbox.addItemListener(this);
        mapbox.setMaximumSize(new Dimension(d1));
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(mapbox, BorderLayout.PAGE_START);
        mapbox.setBounds(50,50,150,25);

        Dimension d2 = mapbox.getMaximumSize();


        //JComboBox<?> Algo = new JComboBox<Object>(algorithm);
        Algo.addActionListener(this);
        Algo.addItemListener(this);
        Algo.setFont(f2);
        Algo.setMaximumSize(new Dimension(d2));
        //Algo.setBounds(50,50,150,25);
        Algo.setSize(100,5);
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(Algo, BorderLayout.PAGE_START);


        Dimension dh = Algo.getMaximumSize();



        heu.addActionListener(this);
        heu.addItemListener(this);
        heu.setFont(f2);
        heu.setMaximumSize(new Dimension(dh));
        heu.setSize(100,5);
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(heu, BorderLayout.PAGE_START);




        run=new JButton("Run");
        run.addMouseListener(myMouseListener);
        run.addActionListener(this);
        run.setBounds(50,100,100,25);
        run.setMaximumSize(new Dimension(d2));
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(run, BorderLayout.PAGE_START);
        //p2.add(load);

        Dimension d3 = run.getMaximumSize();

        save=new JButton("Save");
        save.addMouseListener(myMouseListener);
        save.addActionListener(this);
        save.setBounds(50,100,100,25);
        save.setMaximumSize(new Dimension(d3));
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(save, BorderLayout.PAGE_START);


        Dimension d4 = run.getMaximumSize();
        export=new JButton("Export");
        export.addMouseListener(myMouseListener);
        export.addActionListener(this);
        export.setBounds(50,100,100,25);
        export.setMaximumSize(new Dimension(d4));
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(export, BorderLayout.PAGE_START);


        Dimension d5 = run.getMaximumSize();
        open=new JButton("Open");
        open.addMouseListener(myMouseListener);
        open.addActionListener(this);
        open.setBounds(50,100,100,25);
        open.setMaximumSize(new Dimension(d5));
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(open, BorderLayout.PAGE_START);


        generate=new JButton("Generate");
        generate.addMouseListener(myMouseListener);
        generate.addActionListener(this);
        generate.setBounds(50,100,100,25);
        generate.setMaximumSize(new Dimension(d3));
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(generate, BorderLayout.PAGE_START);

        weight=new JButton("Set Weight");
        weight.addMouseListener(myMouseListener);
        weight.addActionListener(this);
        weight.setBounds(50,100,100,25);
        weight.setMaximumSize(new Dimension(d3));
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(weight, BorderLayout.PAGE_START);

        timerlabel = new JLabel("Time: Value");
        timerlabel.setMinimumSize(new Dimension(d3));
        timerlabel.setFont(f3);
        panel1.add(Box.createRigidArea(new Dimension(10,5)));
        panel1.add(timerlabel, BorderLayout.PAGE_START);

        Dimension d6 = run.getMaximumSize();
        fcost = new JLabel("F: Value ");
        fcost.setFont(f3);
        fcost.setMinimumSize(new Dimension(d6));
        panel1.add(Box.createRigidArea(new Dimension(5,5)));
        panel1.add(fcost, BorderLayout.PAGE_START);

        Dimension d7 = run.getMaximumSize();
        gcost = new JLabel("G:  Value");
        gcost.setFont(f3);
        gcost.setMinimumSize(new Dimension(d7));
        panel1.add(Box.createRigidArea(new Dimension(5,5)));
        panel1.add(gcost, BorderLayout.PAGE_START);

        Dimension d8 = run.getMaximumSize();
        hcost = new JLabel("H:  Value");
        hcost.setFont(f3);
        hcost.setMinimumSize(new Dimension(d8));
        panel1.add(Box.createRigidArea(new Dimension(5,5)));
        panel1.add(hcost, BorderLayout.PAGE_START);

        addWindowListener (new WindowAdapter () {
                               public void windowClosing (WindowEvent we) {
                                   quitApp();
                               }
                           }
        );



        GridLayout g2 = new GridLayout(s1, s2);
        panel2.setLayout(g2);

        cell = new JLabel[s1][s2];

        for (int i=0; i<s1; i++)
        {
            for (int j=0; j<s2 ; j++ )
            {
                cell[i][j] = new JLabel();
                cell[i][j].setPreferredSize(new Dimension(0, 0));
                cell[i][j].setBorder(new LineBorder(Color.BLACK));
                cell[i][j].setName(i + " " + j);
                cell[i][j].setOpaque(true);
                cell[i][j].setBackground(Color.GREEN);
                cell[i][j].addMouseListener(myMouseListener);
                panel2.add(cell[i][j]);
            }
        }

        // add to mainpanel and setvisible
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        frame.setContentPane(mainPanel);
        this.setVisible(true);

        setup();
        runPath();
    }

    public void runPath() throws InterruptedException
    {
        if(applyalgo.equals("A*"))
        {
            mySearch = new Astar(false);
            mySearch.init(new AstarComparator());
            mySearch.findPath(applyheu, 0);
        }
        else if (applyalgo.equals("Weighted A*"))
        {
            mySearch = new Astarweighted(false);
            mySearch.init(new AstarComparator());
            mySearch.findPath(applyheu, 0);
        }
        else if(applyalgo.equals("UCS"))
        {
            mySearch = new UCS(false);
            mySearch.init(new UCSComparator());
            mySearch.findPath("", 0);
        }
        else
        {
            SequentialAStar aSearch = new SequentialAStar(true);
            aSearch.init(new MultiHeurComparator());
            aSearch.seqAStar();
        }
    }

    public void setup()
    {
        String[][] grid = Heuristic.g.getGrid();
        ArrayList<Vertex> s = Heuristic.g.getsVertex();
        ArrayList<Vertex> g = Heuristic.g.getgVertex();

        for(int i = 0; i < s1; i++)
        {
            for(int j = 0; j < s2; j++)
            {
                if(grid[i][j].equals("0"))
                {
                    cell[i][j].setBackground(Color.BLACK);
                }
                else if(grid[i][j].equals("1"))
                {
                    cell[i][j].setBackground(Color.GREEN);
                }
                else if(grid[i][j].equals("2"))
                {
                    cell[i][j].setBackground(Color.RED);
                }
                else if(grid[i][j].equals("a"))
                {
                    cell[i][j].setBackground(Color.BLUE);
                }
                else if(grid[i][j].equals("b"))
                {
                    cell[i][j].setBackground(Color.BLUE);
                    cell[i][j].setForeground(Color.RED);
                }
            }
        }

        Random rand = new Random();
        vert = rand.nextInt(10);
        cell[s.get(vert).getRow()][s.get(vert).getCol()].setBackground(Color.WHITE);
        cell[g.get(vert).getRow()][g.get(vert).getCol()].setBackground(Color.WHITE);

    }

    class MyMouseListener implements MouseListener {
        Map parent;
        int x;
        int y;

        MyMouseListener(Map parent) {
            this.parent = parent;
        }

        public void mouseExited(MouseEvent arg0){
        }
        public void mouseEntered(MouseEvent arg0, int x, int y){

            cell[x][y].setBackground(Color.RED);

        }
        public void mousePressed(MouseEvent arg0, int x, int y){
            cell[x][y].setBackground(Color.RED);
        }
        public void mouseClicked(MouseEvent arg0, int x, int y){
            cell[x][y].setBackground(Color.RED);
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
            if(SwingUtilities.isRightMouseButton(arg0)){
                Object eventSource = arg0.getSource();
                JLabel clickedButton = (JLabel) eventSource;
                String[] xy = clickedButton.getName().split(" ", 2);
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);

                // parent.buttonRightClicked(x, y);
            }
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            boolean flag = false;
            if(applyalgo.equals("Sequential A*"))
            {
                flag = true;
            }

            Object source = e.getSource();
            JLabel clickedButton = (JLabel) source;

            String[] xy = clickedButton.getName().split(" ", 2);
            x = Integer.parseInt(xy[0]);
            y = Integer.parseInt(xy[1]);

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            if(cell[x][y].getBackground() == Color.MAGENTA)
            {
                for(Node n : mySearch.getFringe(0))
                {
                    if(n.getCoord().getRow() == x  && n.getCoord().getCol() == y)
                    {
                        hcost.setText("h(s): " + df.format(n.gethCost()) + " ");
                        fcost.setText("f(s): " + df.format(n.getfCost(flag)) + " ");
                        gcost.setText("g(s): " + df.format(n.getgCost()) + " ");
                    }
                }
            }
            else if(cell[x][y].getBackground() == Color.CYAN)
            {
                for(Node n : mySearch.getClosedSet(0))
                    {
                        if(n.getCoord().getRow() == x  && n.getCoord().getCol() == y)
                        {
                            hcost.setText("h(s): " + df.format(n.gethCost()) + " ");
                            fcost.setText("f(s): " + df.format(n.getfCost(flag)) + " ");
                            gcost.setText("g(s): " + df.format(n.getgCost()) + " ");
                        }
                    }
            }
            else if(cell[x][y].getBackground() == Color.WHITE)
            {
                for(int i = 0; i < mySearch.getPath().size(); i++)
                {
                    if(mySearch.getPath().get(i) != null && (mySearch.getPath().get(i).getCoord().getRow() == x  && mySearch.getPath().get(i).getCoord().getCol() == y))
                    {
                        hcost.setText("h(s): " + df.format(mySearch.getPath().get(i).gethCost()) + " ");
                        fcost.setText("f(s): " + df.format(mySearch.getPath().get(i).getfCost(flag)) + " ");
                        gcost.setText("g(s): " + df.format(mySearch.getPath().get(i).getgCost()) + " ");
                    }
                }
            }
        }
    }

    public void actionPerformed(ActionEvent ae)
    {

        if(ae.getSource()== save)
        {
            //save function
            Backend.save();
            JOptionPane.showMessageDialog (this, "Saved!", "Pop-up", JOptionPane.PLAIN_MESSAGE);
        }
        if(ae.getSource() == run)
        {
            //load function
            applymap = String.valueOf(mapbox.getSelectedItem());
            applyalgo = String.valueOf(Algo.getSelectedItem());
            applyheu = String.valueOf(heu.getSelectedItem());

            if(applymap.equals(""))
            {
                JOptionPane.showMessageDialog (this, "Select Map!", "Alert", JOptionPane.PLAIN_MESSAGE);
            }
            else if(applyalgo.equals(""))
            {
                JOptionPane.showMessageDialog (this, "Select Algorithm!", "Alert", JOptionPane.PLAIN_MESSAGE);
            }
            else if(applyheu.equals("Select Heuristic...") && (!applyalgo.equals("UCS") && !applyalgo.equals("Sequential A*")))
            {
                JOptionPane.showMessageDialog (this, "Select Heuristic!", "Alert", JOptionPane.PLAIN_MESSAGE);
            }
            else
            {
                Grid g = Backend.getGrid(applymap);
                if(g == null)
                {
                    g = new Grid(applymap);
                }
                this.setVisible(false);
                Map m = new Map(applymap);
                Heuristic.g = g;
                try
                {
                    m.main(m, applyalgo, applyheu);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                JOptionPane.showMessageDialog (this, "Opened!", "Pop-up", JOptionPane.PLAIN_MESSAGE);
                this.dispose();
            }

            //	JOptionPane.showMessageDialog (this, "Processing!", "Pop-up", JOptionPane.PLAIN_MESSAGE);
        }
        if(ae.getSource()== generate)
        {
            String file = JOptionPane.showInputDialog("File Name");

            Grid g = new Grid(file);
            Backend.grids.add(g);
            Map m = new Map(file);
            Heuristic.g = g;
            try
            {
                m.main(m, applyalgo, applyheu);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog (this, "Generated!", "Pop-up", JOptionPane.PLAIN_MESSAGE);
            this.dispose();
        }
        /*if(ae.getSource() == weight)
        {
            String file = JOptionPane.showInputDialog("Weight");
            double w = Double.parseDouble(file);
            Astarweighted x= new Astarweighted();
            x.setweight(w);
        }*/
        else
        {

        }

    }


    public static void colorClosed(Vertex v)
    {
        cell[v.getRow()][v.getCol()].setBackground(Color.CYAN);
    }

    public static void colorOpen(Vertex v)
    {
        cell[v.getRow()][v.getCol()].setBackground(Color.MAGENTA);
    }

    public static void colorFinal(Vertex v)
    {
        cell[v.getRow()][v.getCol()].setBackground(Color.WHITE);
    }

    private void quitApp () {

        try {
            //Show a Confirmation Dialog.
            int reply = JOptionPane.showConfirmDialog (this,
                    "Do you really want to exit?",
                    "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

            //Check the User Selection.
            if (reply == JOptionPane.YES_OPTION) {
                setVisible (false);	//Hide the Frame.
                JOptionPane.showMessageDialog(this, "Thank you!\nAuthor - Ayush Joshi and Gabriel Gutierrez.");
                dispose();            	//Free the System Resources.
                System.exit (0);        //Close the Application.
            }
            else if (reply == JOptionPane.NO_OPTION) {
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        }

        catch (Exception e) {}

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // TODO Auto-generated method stub

    }
}
