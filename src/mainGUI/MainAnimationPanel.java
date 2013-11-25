package mainGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import city.Building;
import city.Building.BuildingType;
import city.CityData;
import city.Home;
import city.gui.PersonGui;
import Gui.*;

public class MainAnimationPanel extends JPanel implements ActionListener {
    
	private int frameDisplay = 2;
    
	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	//public List<Building> buildings = Collections.synchronizedList(new ArrayList<Building>());
	public CityData cd;
	
	private int WIDTH = 100;
	private int HEIGHT = 100;
	
	private MainGui mainGui;
	
	public MainAnimationPanel(MainGui mainGui) {
		//Add buildings
		this.mainGui = mainGui;
		cd = new CityData();
		
		for (int i = 0; i < 2; i++) {
			Building b = new Home(10, 140+i*130, WIDTH, HEIGHT, "apartment", BuildingType.apartment, mainGui, cd);
			cd.buildings.add(b);
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Home(10, 410+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Home(190+i*130, 680, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Home(500, 410+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Home(500, 140+i*130, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
		}
		for (int i = 1; i >= 0; i--) {
			Building b = new Home(190+i*130, 0, WIDTH, HEIGHT, "home", BuildingType.home, mainGui, cd);
			cd.buildings.add(b);
		}
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 2; i++) {
				Building b = new Building(190+i*130, 140+j*130, WIDTH, HEIGHT, mainGui);
				cd.buildings.add(b);
			}
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Building(190+i*130, 540, WIDTH, HEIGHT, mainGui);
			cd.buildings.add(b);
		}
		for (int i = 0; i < 2; i++) {
			Building b = new Building(190+i*130, 410, WIDTH, HEIGHT, mainGui);
			cd.buildings.add(b);
		}
		
		//setBackground(Color.WHITE);
		
		setVisible(true);
		Timer timer = new Timer(frameDisplay, this );
		timer.start();
        
	}
    
	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
    
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.WHITE);
        
		//Clear the screen by painting a rectangle the size of the frame
		ImageIcon background = new ImageIcon("res/background.png");
		g2.drawImage(background.getImage(), 0, 0, null);
		
		//Draw apartments
		ImageIcon apartment = new ImageIcon("res/apartment.png");
		for (int i = 0; i < 2; i++) {
			cd.buildings.get(i).setBuildingNumber(i);
			g2.drawImage(apartment.getImage(), (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y, null);
			g2.drawString(cd.buildings.get(i).name, (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y+10);
		}
		
		//Draw houses
		ImageIcon house = new ImageIcon("res/house.png");
		for (int i = 2; i < 12; i++) {
			cd.buildings.get(i).setBuildingNumber(i);
			g2.drawImage(house.getImage(), (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y, null);
			g2.drawString(cd.buildings.get(i).name, (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y+10);
		}
		
		//Draw restaurants
		ImageIcon rest = new ImageIcon("res/restaurant.png");
		for (int i = 12; i < 18; i++) {
			g2.drawImage(rest.getImage(), (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y, null);
			cd.buildings.get(i).setType(BuildingType.restaurant);
			cd.buildings.get(i).setName("restaurant"+(i-11));
			cd.buildings.get(i).setBuildingNumber(i);
			g2.drawString(cd.buildings.get(i).name, (int) cd.buildings.get(i).x, (int) cd.buildings.get(i).y+10);
		}
		
		//Draw bank
        ImageIcon bank = new ImageIcon("res/bank.png");
        g2.drawImage(bank.getImage(), (int) cd.buildings.get(18).x, (int) cd.buildings.get(18).y, null);
        cd.buildings.get(18).setType(BuildingType.bank);
		cd.buildings.get(18).setName("bank");
		cd.buildings.get(18).setBuildingNumber(18);
		g2.drawString(cd.buildings.get(18).name, (int) cd.buildings.get(18).x, (int) cd.buildings.get(18).y+10);
		
		//Draw market
        ImageIcon market = new ImageIcon("res/market.png");
        g2.drawImage(market.getImage(), (int) cd.buildings.get(19).x, (int) cd.buildings.get(19).y, null);
        cd.buildings.get(19).setType(BuildingType.market);
		cd.buildings.get(19).setName("market");
		cd.buildings.get(19).setBuildingNumber(19);
		g2.drawString(cd.buildings.get(19).name, (int) cd.buildings.get(19).x, (int) cd.buildings.get(19).y+10);
        
        //Draw road
        ImageIcon road1 = new ImageIcon("res/road1.png");
        for (int i = 0; i < 615; i++) {
        	g2.drawImage(road1.getImage(), i, 365, null);
        }
        for (int i = 130; i < 470; i++) {
        	g2.drawImage(road1.getImage(), i, 95, null);
        }
        for (int i = 130; i < 470; i++) {
        	g2.drawImage(road1.getImage(), i, 635, null);
        }
        ImageIcon road2 = new ImageIcon("res/road2.png");
        for (int i = 95; i < 660; i++) {
        	g2.drawImage(road2.getImage(), 130, i, null);
        }
        for (int i = 95; i < 660; i++) {
        	g2.drawImage(road2.getImage(), 440, i, null);
        }
        ImageIcon road3 = new ImageIcon("res/road3.png");
        g2.drawImage(road3.getImage(), 130, 365, null);
        g2.drawImage(road3.getImage(), 440, 365, null);
        g2.drawImage(road3.getImage(), 130, 95, null);
        g2.drawImage(road3.getImage(), 440, 95, null);
        g2.drawImage(road3.getImage(), 130, 635, null);
        g2.drawImage(road3.getImage(), 440, 635, null);
        
		synchronized(cd.guis){
			for(Gui gui : cd.guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		synchronized(cd.guis){
			for(Gui gui : cd.guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
	}
    
	public void addGui(PersonGui gui) {
		cd.guis.add(gui);
		System.out.println ("added gui!");
	}
	
    
	public List getBuildings() {
		return cd.buildings;
	}
    
}