package bank.gui;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bank.Bank;
import bank.utilities.CustInfo;

public class BankPanel extends JPanel{
	Bank bank;
	public List<JLabel> labels = Collections.synchronizedList(new ArrayList<JLabel>());
	BankPanel(Bank bank){
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.bank = bank;
		add(new JLabel("Name \t Acct# \t MoneyinBank"));
//		if(bank == null){
//			System.out.println("bank is null inside bankpanel");
//		}if(bank.getCustAccounts() == null){
//			System.out.println("bank accounts are null");
//		}else{
//			for(CustInfo info : bank.getCustAccounts().values()){
//				add(new JLabel(info.custName + " " + info.accountNumber + " " +info.moneyInAccount+" "+info.accountHolder.cash));	
//
//			}
//		}
//	}
//
//	public void updateLabels(){
//		if(bank.getCustAccounts() == null){
//			System.out.println("bank accounts are null");
//
//			for(CustInfo info : bank.getCustAccounts().values()){
//				add(new JLabel(info.custName + " " + info.accountNumber + " " +info.moneyInAccount+" "+info.accountHolder.cash));	
//
//			}
//		}
		
	}

}
