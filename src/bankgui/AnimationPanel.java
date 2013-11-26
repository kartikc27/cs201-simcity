package bankgui;

import java.awt.Color;

import bank.CustomerRole;
import bank.utilities.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import city.PersonAgent;
import market.gui.CustomerGui;
import Gui.*;

public class AnimationPanel extends JPanel implements GuiPositions, ActionListener{
    private static final int WINDOWX = 400;
    private static final int WINDOWY = 500;
    private static final int TIMEINTERVAL = 5;
    
	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	private Image bufferImage;
	private Dimension bufferSize;

	public AnimationPanel() {
		setBackground(Color.lightGray);
		setVisible(true);
		setSize(WINDOWX, WINDOWY);
		bufferSize = this.getSize();
		Timer timer = new Timer(TIMEINTERVAL, this );
		timer.start();

		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		Color DeskColor = new Color(139, 0, 0);

		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, WINDOWX, WINDOWY );
		g2.setColor(DeskColor);
		g2.fill3DRect(250 ,0, 10, WINDOWY-30, true);
		g2.setColor(Color.BLACK);
		g2.draw3DRect(0, 200, 200, 1, true);
		g2.draw3DRect(0, 230, 200, 1, true);
		g2.draw3DRect(doorx, doory, 20, 20, false);
		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		synchronized(guis){
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
	}

	public void addGui(BankCustomerGui gui) {
		guis.add(gui);
		System.out.print("added gui type "+ gui.getClass().toString());
	}

	
	public void addGui(BankManagerGui gui) {
		System.out.print("added gui type "+ gui.getClass().toString());
		guis.add(gui); 
	}
	public void addGui(TellerGui gui) {
		System.out.print("added gui type "+ gui.getClass().toString());
		guis.add(gui);
		
	}
	public void removeGui(TellerGui gui) {
		
		guis.remove(gui);
	}
	public void removeGui(CustomerGui gui) {
		guis.remove(gui);
	}
	public void removeGui(BankManagerGui gui) {
		guis.remove(gui);
	}
}
