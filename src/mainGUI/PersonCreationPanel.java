package mainGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import city.CityData;
import city.PersonAgent;
import city.gui.PersonGui;

/**
 * Panel to create PersonAgents
 */
public class PersonCreationPanel extends JPanel implements ActionListener, KeyListener{

	public static final int PANEDIM = 150;

	public JScrollPane pane =
			new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	public JPanel personPane = new JPanel();
	public JPanel jobsPane = new JPanel();

	private JPanel view = new JPanel();
	public JPanel namePane = new JPanel();
	private JButton addPerson = new JButton("Add");
	private JButton robBank = new JButton("Rob the bank!");
	private JButton carCrash = new JButton("Cause car crash!");
	private JButton runOver = new JButton("Run someone over!");
	private JTextField nameField = new JTextField(10);
	private ButtonGroup jobs = new ButtonGroup();
	
	private JButton fire = new JButton("Fire!!!");
	public JPanel timePane = new JPanel();
	private JButton timeSetting = new JButton("Set Time Interval");
	private JTextField timeField = new JTextField(10);
	private JLabel timeIntervalLabel = new JLabel();
	
	private int timeInterval = -1;

	private List<JButton> list = new ArrayList<JButton>(); 
	private String type = "Person";
	private String name;
	private String job;
	private MainGui mainGui;
	private CityData cityData;
	private boolean robberAdded = false;

	JRadioButton unemployed;
	JRadioButton landlord;
	//restaurant jobs
	JRadioButton restMQwaiter;
	JRadioButton restMQhost;
	JRadioButton restMQcook;
	JRadioButton restMQcashier;
	JRadioButton restMQcustomer;
	//market jobs
	JRadioButton marketManager;
	JRadioButton marketEmployee;
	//bank jobs
	JRadioButton bankManager;
	JRadioButton bankTeller;
	JRadioButton bankCustomer;
	
	private JButton trace = new JButton("Trace Panel");

	public PersonCreationPanel(MainGui mainGui, CityData cd) {
		this.mainGui = mainGui;
		this.type = "person";
		cityData = cd;
		setLayout(null);//new BoxLayout(this, BoxLayout.Y_AXIS));
		
		timePane.setLayout(new FlowLayout());
		timePane.add(timeField);
		timePane.add(timeSetting);
		timePane.add(timeIntervalLabel);
		timeIntervalLabel.setText("\t\t\tTime Interval = "+mainGui.mainAnimationPanel.GLOBALINTERVAL);

		JPanel nonNorms = new JPanel();
		nonNorms.setVisible(true);
		nonNorms.setLayout(new GridLayout(1, 3));
		nonNorms.add(robBank);
		nonNorms.add(carCrash);
		nonNorms.add(runOver);
		
		nameField.setHorizontalAlignment(JTextField.CENTER);
		namePane.setLayout(new FlowLayout());
		
		namePane.add(nameField);
		namePane.add(addPerson);

		personPane.setLayout(null);
		
		unemployed = new JRadioButton("Unemployed", true);
		landlord = new JRadioButton("Landlord", false);
		restMQwaiter = new JRadioButton("Waiter", false);
		restMQhost = new JRadioButton("Host", false);
		restMQcook = new JRadioButton("Cook", false);
		restMQcashier = new JRadioButton("Cashier", false);
		marketManager = new JRadioButton("MarketManager", false);
		marketEmployee = new JRadioButton("MarketEmployee", false);
		bankManager = new JRadioButton("BankManager", false);
		bankTeller = new JRadioButton("BankTeller", false);
		
		jobs.add(unemployed);
		jobs.add(landlord);
		jobs.add(restMQwaiter);
		jobs.add(restMQhost);
		jobs.add(restMQcook);
		jobs.add(restMQcashier);
		jobs.add(marketManager);
		jobs.add(marketEmployee);
		jobs.add(bankManager);
		jobs.add(bankTeller);
		
		jobsPane.setLayout(null);
		unemployed.setBounds(0, 0, 160, 30);
		landlord.setBounds(145, 0, 150, 30);
		restMQhost.setBounds(0, 30, 160, 30);
		restMQwaiter.setBounds(145, 30, 155, 30);
		restMQcook.setBounds(300, 30, 150, 30);
		restMQcashier.setBounds(450, 30, 180, 30);
		marketManager.setBounds(0, 60, 150, 30);
		marketEmployee.setBounds(145, 60, 150, 30);
		bankManager.setBounds(0, 90, 150, 30);
		bankTeller.setBounds(145, 90, 150, 30);
		jobsPane.add(unemployed);
		jobsPane.add(landlord);
		jobsPane.add(restMQwaiter);
		jobsPane.add(restMQhost);
		jobsPane.add(restMQcook);
		jobsPane.add(restMQcashier);
		jobsPane.add(marketManager);
		jobsPane.add(marketEmployee);
		jobsPane.add(bankManager);
		jobsPane.add(bankTeller);

		unemployed.addActionListener(this);
		addPerson.addActionListener(this);
		nameField.addKeyListener(this);
		fire.addActionListener(this);
		robBank.addActionListener(this);
		carCrash.addActionListener(this);
		runOver.addActionListener(this);
		timeSetting.addActionListener(this);
		timeField.addKeyListener(this);

		view.setLayout(new GridLayout(0,1));
		pane.setViewportView(view);
		pane.setMinimumSize(new Dimension(PANEDIM, PANEDIM));
		pane.setPreferredSize(new Dimension(PANEDIM, PANEDIM));  

		fire.setBounds(5, 5, 150, 50);
		robBank.setBounds(160, 5, 150, 50);
		carCrash.setBounds(315, 5, 150, 50);
		runOver.setBounds(470, 5, 150, 50);
		timePane.setBounds(0, 65, 625, 50);
		jobsPane.setBounds(0, 0, 625, 125);
		nameField.setBounds(0, 125, 625, 90);
		addPerson.setBounds(0, 215, 625, 90);
		personPane.add(jobsPane);	
		personPane.add(nameField);
		personPane.add(addPerson);
		personPane.setBounds(0, 120, 625, 305);
		pane.setBounds(0, 430, 625, 250);

		trace.setBounds(0, 685, 625, 90);
		trace.addActionListener(this);
		
		add(fire);
		add(robBank);
		add(carCrash);
		add(runOver);
		add(timePane);
		add(personPane);
		add(pane);
		add(trace);

		addPerson.setEnabled(false);
	}


	public void keyTyped(KeyEvent arg0) {

	}



	/**
	 * If the add button is pressed, this function creates
	 * a spot for it in the scroll pane, and tells the restaurant panel
	 * to add a new person.
	 *
	 * @param name name of new person
	 */
	public void addPerson(String name) {
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 20,
					(int) (paneSize.height/7));

			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			list.add(button);
			view.add(button);
			
			String role = "";
			String destination;
			
			if (robberAdded) {
				role = "BankRobber";
				destination = "Bank";
				robberAdded = false;
			}
			
			if(getSelectedButtonText(jobs).contains("Bank")){
				destination = "Bank";
				if(getSelectedButtonText(jobs).contains("BankManager")){
					role = "BankManager";
				//	mainGui.mainAnimationPanel.cd.buildings.get(18).manager
				}
				else if(getSelectedButtonText(jobs).contains("BankTeller")){
					role = "BankTeller";
				}
				/*else if(getSelectedButtonText(jobs).contains("Customer")){
					role = "Customer";
				}*/
			}
			else if(getSelectedButtonText(jobs).contains("Market")){
				destination = "Market";
				if(getSelectedButtonText(jobs).contains("MarketManager")){
					role = "MarketManager";
				}
				else if(getSelectedButtonText(jobs).contains("MarketEmployee")){
					role = "MarketEmployee";
				}
			}
			else if(getSelectedButtonText(jobs).contains("Unemployed")||getSelectedButtonText(jobs).contains("Landlord")){
				destination = "Home";
			}
			else
			{
				destination = "Restaurant";
				/*if(getSelectedButtonText(jobs).contains("Customer"))
				{
					role = "Customer";
				}*/
				if(getSelectedButtonText(jobs).contains("Waiter"))
				{
					role = "Waiter";
				}
				else if(getSelectedButtonText(jobs).contains("Cook"))
				{
					role = "Cook";
				}
				else if(getSelectedButtonText(jobs).contains("Host"))
				{
					role = "Host";
				}
				else if(getSelectedButtonText(jobs).contains("Cashier"))
				{
					role = "Cashier";
				}
			}
			
			mainGui.addPerson(name, role, destination);
			unemployed.setSelected(true);
			validate();
		}
	}
	
	



	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		name = nameField.getText();
		if ((name != null) && !name.isEmpty()){
			addPerson.setEnabled(true);
		} 
		else {
			addPerson.setEnabled(false);
		}
		
		try {
			timeInterval = Integer.parseInt(timeField.getText());
		} catch (NumberFormatException e) {
		     //Not an integer
		}
	}


	/**
	 * Method from the ActionListener interface.
	 * Handles the event of the add button being pressed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addPerson) {
			name = nameField.getText();
			nameField.setText(null);

			if (name != null && !name.isEmpty()){
				addPerson(name);
				addPerson.setEnabled(false);
				addPerson.setEnabled(false);
			}
		}
		
		if (e.getSource() == timeSetting) {
			if(timeInterval < 0) {
	    		JFrame frame = new JFrame();
	    		JOptionPane.showMessageDialog(frame, "Time interval must be a positive number!");
	    		timeField.setText("");
	    		return;
	    	}
			else {
				mainGui.mainAnimationPanel.GLOBALINTERVAL = timeInterval;
			}
			timeField.setText("");
			timeIntervalLabel.setText("\t\t\tTime Interval = " + mainGui.mainAnimationPanel.GLOBALINTERVAL);
			mainGui.mainAnimationPanel.cd.globalTimer.setDelay(mainGui.mainAnimationPanel.GLOBALINTERVAL);
		}
		
		if (e.getSource() == fire) {
			List<PersonAgent> persons = mainGui.mainAnimationPanel.cd.getAllPeople();
			for (PersonAgent p : persons) {
				p.msgFire();
			}
		}
		
		else if (e.getSource() == robBank) {
			if (cityData.banks.get(0).isOpen()) {
				robberAdded = true;
				addPerson("DaBankRobba");
			}
			else {
				JFrame frame = new JFrame();
	    		JOptionPane.showMessageDialog(frame, "You can't rob the bank unless it's open, silly!");
			}
		}
		
		else if (e.getSource() == carCrash) {
			//car accident
		}
		
		else if (e.getSource() == runOver) {
			//run a pedestrian over
		}
		
		if (e.getSource() == trace) {
			mainGui.dl.setVisible(true);
		}
	}


	public String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()) {
				return button.getText();
			}
		}
		return null;
	}
}