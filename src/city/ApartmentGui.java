package city;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ApartmentGui extends JFrame implements ActionListener {

	ApartmentPanel aptPanel;
	AptInfoPanel aptInfoPanel;
	List<Room> rooms;
	
	public ApartmentGui(List<Room> r) {
		setTitle("Apartment");
    	setVisible(false);
    	setResizable(false);
    	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0, 600, 400);
		setLocation((screenSize.width/2-this.getSize().width/2), (screenSize.height/2-this.getSize().height/2));
		setLayout(new BorderLayout());
		rooms = r;
		
		aptInfoPanel = new AptInfoPanel();
		aptInfoPanel.setPreferredSize(new Dimension(200, 400));
		this.add(aptInfoPanel, BorderLayout.WEST);
		
		aptPanel = new ApartmentPanel();
		aptPanel.setPreferredSize(new Dimension(400, 400));
		this.add(aptPanel, BorderLayout.CENTER);
	}
	
	public void addName(PersonAgent p) {
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		
	}

	public ApartmentPanel getAptPanel() {
		return aptPanel;
	}
	
	public AptInfoPanel getAptInfoPanel() {
		return aptInfoPanel;
	}

}
