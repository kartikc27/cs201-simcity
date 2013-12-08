package restaurantSM.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 650;
    private final int WINDOWY = 450;
    private Image bufferImage;
    private Dimension bufferSize;
    private int xTable = 150;
    private int yTable[] = { 200, 300, 400};
    private int tableSize = 50;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY);

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(xTable, yTable[0], tableSize, tableSize);
        g2.fillRect(xTable,  yTable[1], tableSize, tableSize);
        g2.fillRect(xTable,  yTable[2],  tableSize, tableSize);
        

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui){
    	guis.add(gui);
    }

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
}
