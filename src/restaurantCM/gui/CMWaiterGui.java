package restaurantCM.gui;

import restaurantCM.*;

import java.awt.*;

import Gui.Gui;

public class CMWaiterGui implements Gui{
	//	boolean sentMsg = true;
	private CMWaiterRole agent = null;
	public static final int xCook = 200;
	public static final int yCook = 150;
	private int xLobby = 60 , yLobby = 60; //Lobby position
	private int xHome;
	private int yHome;
	public int xPos = xHome;

	public int yPos = yHome;
	public int xDestination = xHome, yDestination = yHome;//default start position
	public int xTable, yTable;
	public boolean carryOrder = false;
	public static final int xTable1 = CMHostGui.xTable1;
	public static final int yTable1 = CMHostGui.yTable1;
	public static final int xTable2 = CMHostGui.xTable2;
	public static final int yTable2 = CMHostGui.yTable2;
	public static final int xTable3 = CMHostGui.xTable3;
	public static final int yTable3 = CMHostGui.yTable3;
	public static final int xTable4 = CMHostGui.xTable4;
	public static final int yTable4 = CMHostGui.yTable4;
	public static final int xTable5 = CMHostGui.xTable5;
	public static final int yTable5 = CMHostGui.yTable5;
	public static final int xTable6 = CMHostGui.xTable6;
	public static final int yTable6 = CMHostGui.yTable6;
	public static final int xTable7 = CMHostGui.xTable7;
	public static final int yTable7 = CMHostGui.yTable7;
	public static final int xTable8 = CMHostGui.xTable8;
	public static final int yTable8 = 160;
	public static final int xTable9 = 160;
	public static final int yTable9 = 220;
	public static final int xTable10 = 160;
	public static final int yTable10 = 280;
	public static final int xTable11 = 160;
	public static final int yTable11 = 340;
	public static final int xTable12 = 160;
	public static final int yTable12 = 400;
	public boolean atDest = false;
	public CMWaiterGui(CMWaiterRole agent, int x, int y) {
		this.agent = agent;
		this.xPos = x;
		this.yPos = y;
		this.xHome = x;
		this.yHome = y;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		//		if(xPos == xTable1+20 && yPos == yTable1-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable2+20 && yPos == yTable2-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable3+20 && yPos == yTable3-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable4+20 && yPos == yTable4-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable5+20 && yPos == yTable5-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable6+20 && yPos == yTable6-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable7+20 && yPos == yTable7-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable8+20 && yPos == yTable8-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable9+20 && yPos == yTable9-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable10+20 && yPos == yTable10-20){
		//			agent.msgDoneAtTable();
		//		}
		//		if(xPos == xTable11+20 && yPos == yTable11-20){
		//			agent.msgDoneAtTable();
		//		}	
		//		if(xPos == xTable12+20 && yPos == yTable12-20){
		//			agent.msgDoneAtTable();
		//		}	        
		//		if(xPos == xLobby && yPos == yLobby){
		//			agent.msgAtLobby();
		//		}
		//		if(xPos == xHome && yPos == yHome){


		if(xPos == xCook && yPos == yCook){
			if(!atDest){
				atDest = true;
				agent.msgAtCook();
			}
		}
		else if(xPos == xLobby && yPos == yLobby){
			if(!atDest){
				atDest = true;
				agent.msgAtLobby();
			}
		}
		else if(xPos == xHome && yPos == yHome){
			if(!atDest){
				atDest = true;
				agent.msgAtHome();
			}
		}
		else if(xPos == xDestination && yPos == yDestination){
			if(!atDest){
				atDest = true;
				agent.msgDoneAtTable();
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, 20, 20);

	}

	public boolean isPresent() {
		return true;
	}

	public void DoGoToTable(int place) {

		//sentMsg = false;
		switch(place){
		/*case -1:
	    			xDestination = xLobby;
	    			yDestination = yLobby;
	    			break;
	    		case -2:
	    			xDestination = xCook;
	    			yDestination = yCook;
		 */
		case 1:
			xDestination = xTable1 + 20;
			yDestination = yTable1 - 20;
			yTable = yTable1;
			xTable = xTable1;
			atDest = false;
			break;
		case 2:
			xDestination = xTable2 + 20;
			yDestination = yTable2 - 20;
			yTable = yTable2;
			xTable = xTable2;
			atDest = false;
			break;
		case 3:
			xDestination = xTable3 + 20;
			yDestination = yTable3 - 20;
			yTable = yTable3;
			xTable = xTable3;
			atDest = false;
			break;
		case 4:
			xDestination = xTable4 + 20;
			yDestination = yTable4 - 20;
			yTable = yTable4;
			xTable = xTable4;
			atDest = false;
			break;
		case 5:
			xDestination = xTable5 + 20;
			yDestination = yTable5 - 20;
			yTable = yTable5;
			xTable = xTable5;
			atDest = false;
			break;
		case 6:
			xDestination = xTable6 + 20;
			yDestination = yTable6 - 20;
			yTable = yTable6;
			xTable = xTable6;
			atDest = false;
			break;
		case 7:
			xDestination = xTable7 + 20;
			yDestination = yTable7 - 20;
			yTable = yTable7;
			xTable = xTable7;
			atDest = false;
			break;
		case 8:
			xDestination = xTable8 + 20;
			yDestination = yTable8 - 20;
			yTable = yTable8;
			xTable = xTable8;
			atDest = false;
			break;
		case 9:
			xDestination = xTable9 + 20;
			yDestination = yTable9 - 20;
			yTable = yTable9;
			xTable = xTable9;
			atDest = false;
			break;
		case 10:
			xDestination = xTable10 + 20;
			yDestination = yTable10 - 20;
			yTable = yTable10;
			xTable = xTable10;
			atDest = false;
			break;
		case 11:
			xDestination = xTable11 + 20;
			yDestination = yTable11 - 20;
			yTable = yTable11;
			xTable = xTable11;
			atDest = false;
			break;
		case 12:
			xDestination = xTable12 + 20;
			yDestination = yTable12 - 20;
			yTable = yTable12;
			xTable = xTable12;
			atDest = false;
			break;
		}
	}

	public void DoGoToLobby() {
		xDestination = xLobby;
		yDestination = yLobby;
		atDest = false;
		//  sentMsg = false;
	}
	public void DoGoToHome(){
		xDestination = xHome;
		yDestination = yHome;
		atDest = false;
	}
	public void DoGoToCook() {
		xDestination = xCook;
		yDestination = yCook;
		atDest = false;

	}
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}


}


