package restaurantMQ.gui;
import Gui.*;

import javax.swing.*;

import city.gui.PersonGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public class AnimationPanel extends JPanel implements ActionListener {

    private static final int WINDOWX = 550;
    private static final int WINDOWY = 350;
    private static final int TABLEX = 150;
    private static final int TABLEY = 200;
    private static final int TABLEWIDTH = 50;
    private static final int TIMEINTERVAL = 5;
    private static final int TABLEDIST = 100;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
    
    private Vector<TableGui> tables = new Vector<TableGui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(TIMEINTERVAL, this );
    	timer.start();
    	
    	tables.add(new TableGui(TABLEX, TABLEY));
    	tables.add(new TableGui(TABLEX + TABLEDIST, TABLEY));
    	tables.add(new TableGui(TABLEX, TABLEY + TABLEDIST));
    	tables.add(new TableGui(TABLEX + TABLEDIST, TABLEY + TABLEDIST));
    }

	public void actionPerformed(ActionEvent e) {
		synchronized(guis)
		{
			for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		}
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX+300, WINDOWY+300 );

        //Here are the tables
        g2.setColor(Color.ORANGE);
        for(TableGui t : tables)
        {
        	g2.fillRect(t.tableX, t.tableY, TABLEWIDTH, TABLEWIDTH);//200 and 250 need to be table params
        }
        
        synchronized(guis)
        {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui) {
    	guis.add(gui);
    }

	public void addGui(PersonGui gui) {
		guis.add(gui);
		
	}
}
