package city;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import city.gui.PersonGui;
import market.gui.AnimationPanel;

public class HomeGui extends JFrame implements ActionListener {

	private HomePanel homePanel;
	private HomeInfoPanel homeInfoPanel;
	
	public HomeGui() {
    	setTitle("Home");
    	setVisible(false);
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0, 600, 400);
		setLocation((screenSize.width/2-this.getSize().width/2), (screenSize.height/2-this.getSize().height/2));
		setLayout(new BorderLayout());
		
		homeInfoPanel = new HomeInfoPanel();
		this.add(homeInfoPanel, BorderLayout.WEST);
		
		homePanel = new HomePanel();
		this.add(homePanel, BorderLayout.CENTER);
	}
	
	public HomePanel getHomePanel(){
		return homePanel;
	}
	
	public HomeInfoPanel getHomeInfoPanel() {
		return homeInfoPanel;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
