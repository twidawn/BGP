package client;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
/*
 * ������ ���� ���� â
 */
public class ChooseGame extends JFrame{
	JButton othello, omok;
		
	public ChooseGame(){
		setLayout(new FlowLayout());
		
		othello = new JButton("������");
		omok = new JButton("����");
		
		add(omok);
		add(othello);
		
		setVisible(false);
	}
}
