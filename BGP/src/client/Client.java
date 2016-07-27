package client;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import othello.*;

public class Client extends JFrame implements ActionListener, Runnable {
	private boolean running = false;
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private String userName = null;
	private int selectGame = 0;
	public int playerColor = 0;
	public int myDice = 0;
	public int rvDice = 0;

	GameControl gc;

	CardLayout card = new CardLayout();

	LoginPanel lp;
	Matching ma;
	SelectRival sr;
	ChooseGame cg;
	MainGamePanel mgp;
	DiceGame dg;
	MakeRoom mr;
	ReGame rg;

	public Client() {
		setLayout(card);

		lp = new LoginPanel();
		ma = new Matching();
		sr = new SelectRival();
		cg = new ChooseGame();
		dg = new DiceGame();
		mr = new MakeRoom();
		gc = new GameControl();
		mgp = new MainGamePanel();
		mgp.setOthello(gc);
		rg = new ReGame();

		add("LOGIN", lp);
		add("MATCHI", ma);
		add("SERIV", sr);
		add("DICE", dg);
		add("OTG", mgp);

		lp.twitter.addActionListener(this);
		lp.guest.addActionListener(this);
		ma.start.addActionListener(this);
		cg.othello.addActionListener(this);
		cg.omok.addActionListener(this);
		// @@
		sr.createRoom.addActionListener(this);
		sr.enterRoom.addActionListener(this);
		dg.b1.addActionListener(this);
		dg.gc.addActionListener(this);
		mr.makeRoom.addActionListener(this);

		mgp.send.addActionListener(this);
		
		//rg.regame.addActionListener(this);
		//rg.out.addActionListener(this);

		setTitle("League of Board Game");
		sizeChange(600, 800);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String msg;
		try {
			while ((msg = reader.readLine()) != null) {
				// System.out.println(msg);
				if (msg.startsWith("[MKROOM]")) { // �����
					String temp = msg.substring(8);
					String roomName = temp.substring(0, temp.indexOf(" "));
					String numOfPlayer = temp.substring(temp.indexOf(" ") + 1);
					String[] a = { roomName, numOfPlayer };
					sr.model.addRow(a);
				} else if (msg.startsWith("[RMROOM]")) { // ������
					String temp = msg.substring(8);
					System.out.println(temp);
					for (int i = 0; i < sr.model.getRowCount(); i++) {
						if (sr.model.getValueAt(i, 0).equals(temp))
							sr.model.removeRow(i);
					}
				} else if (msg.startsWith("[JOINR]")) { // ������
					String temp = msg.substring(7);
					writer.println("[CHECKF]" + sr.table.getSelectedRow());
					card.show(getContentPane(), "DICE");
					sizeChange(1000, 850);
				} else if (msg.equals("[FULLR]")) {
					JOptionPane.showMessageDialog(this, "���� �� á���ϴ�.");
				} else if (msg.startsWith("[CHECKF]")) {
					sr.model.setValueAt("2", Integer.parseInt(msg.substring(8)), 1);
				} else if (msg.equals("[LEAVE]")) {
					card.show(getContentPane(), "SERIV");
					sizeChange(415, 420);
					writer.println("[LEAVE]");
				} else if (msg.equals("[GODICE]")) {
					dg.b1.setVisible(true);
				} else if (msg.startsWith("[SETDC]")) { // ������ �ֻ��� ����
					dg.la5.setIcon(dg.icon[Integer.parseInt(msg.substring(7)) - 1]);
				} else if (msg.startsWith("[UDICE]")) { // �� �ֻ��� ����
					dg.la4.setIcon(dg.icon[Integer.parseInt(msg.substring(7)) - 1]);
				} else if (msg.startsWith("[DICEW]")) {
					playerColor = 1;
					mgp.tile.turn = 1;
					mgp.tile.setenable(true);
					dg.gc.setVisible(true);
				} else if (msg.startsWith("[DICEL]")) {
					playerColor = 2;
					mgp.tile.turn = 2;
				} else if (msg.equals("[GOTHE]")) {
					gc.gameStatus(mgp.tile);
					card.show(getContentPane(), "OTG");
					mgp.tile.setPw(writer);
					sizeChange(1300, 900);
				} else if (msg.startsWith("[STONE]")) {
					String temp = msg.substring(7);
					int x = Integer.parseInt(temp.substring(0, temp.indexOf(" ")));
					int y = Integer.parseInt(temp.substring(temp.indexOf(" ") + 1));
					if (playerColor == 1)
						mgp.tile.changeStone(x, y, 2);
					else
						mgp.tile.changeStone(x, y, 1);
				} else if (msg.startsWith("[YOURT]")) {
					mgp.tile.gameWLD();
					if (playerColor == 1)
						writer.println("[BLACK]");
					else
						writer.println("[WHITE]");
					mgp.tile.setenable(true);
				} else if (msg.startsWith("[BLACK]")) {
					mgp.turnImage.setIcon(new ImageIcon("image\\black.png"));
				} else if (msg.startsWith("[WHITE]")) {
					mgp.turnImage.setIcon(new ImageIcon("image\\white.png"));
				} else if (msg.equals("[MYWIN]")) {
					rg.lose.setText("��?");
					rg.sizeChange(300, 300);
					rg.setVisible(true);
				} else if (msg.equals("[MYLOS]")) {
					rg.lose.setText("�й�!");
					rg.sizeChange(300, 300);
					rg.setVisible(true);
				} else if (msg.equals("[GDRAW]")) {
					rg.lose.setText("���");
					rg.sizeChange(300, 300);
					rg.setVisible(true);
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("����ο��� ����" + e.getMessage());
		}

	}

	public void start() {

	}

	private void connect() {
		try {
			System.out.println("����õ�");
			socket = new Socket("127.0.0.1", 9999);
			System.out.println("���Ἲ��");
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			new Thread(this).start();
		} catch (Exception e) {
			System.out.println("Ŭ�� ���غκ� �ͼ���" + e.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (socket == null) {
			System.out.println("�������ῡ �����Ͽ����ϴ�.");
			return;
		}

		if (e.getSource() == lp.twitter) {
			writer.println("[TWITT]");
		} else if (e.getSource() == lp.guest) {
			System.out.println("�Խ�Ʈ ��ư ����");

			// @@���ǵ���@@
			card.show(getContentPane(), "SERIV");
			sizeChange(415, 420);
			// setSize(415, 420);

			writer.println("[GUEST]");
			// card.show(getContentPane(), "MATCHI");
			// sizeChange(400, 400);
		} else if (e.getSource() == sr.createRoom) {
			mr.sizeChange(250, 150);
			mr.rn.setText("");
			mr.setVisible(true);
		} else if (e.getSource() == mr.makeRoom) {
			String temp = "";
			String rname = mr.rn.getText().trim();
			if (rname.length() < 1) {
				JOptionPane.showMessageDialog(this, "���̸��� �Է��ϼ���");
				mr.rn.requestFocus();
				return;
			}
			// �ߺ��� üũ
			for (int i = 0; i < sr.model.getRowCount(); i++) {
				temp = sr.model.getValueAt(i, 0).toString();
				if (rname.equals(temp)) {
					JOptionPane.showMessageDialog(this, "�ߺ��� ���̸��Դϴ�");
					mr.rn.setText("");
					return;
				}
			} // ���⼭ for�� �� and �游��µ� ����������
				// ���� �� ���
			String numOfPlayer = "1";
			writer.println("[MKROOM]" + rname + " " + numOfPlayer);

			// String[] a = { rname, numOfPlayer};

			// sr.model.addRow(a);
			// ���̽�ȭ�����γѱ��
			card.show(getContentPane(), "DICE");
			dg.b1.setVisible(false);
			sizeChange(1000, 850);
			mr.setVisible(false);

		} else if (e.getSource() == sr.enterRoom) {// ������
			if (sr.table.getSelectedRow() != -1)
				writer.println("[JOINR]" + sr.model.getValueAt(sr.table.getSelectedRow(), 0));
		}
		// 07.26
		// }else if(e.getSource()==ma.start){
		else if (e.getSource() == dg.gc) {
			cg.setVisible(true);
			cg.setResizable(false);
			cg.setBounds(300, 300, 170, 70);
			System.out.println("���۹�ư ����");
		} else if (e.getSource() == cg.othello) {
			System.out.println("���� ��ư�� ������");
			// mgp.tile.setenable(true);
			writer.println("[GOTHE]");

			cg.setVisible(false);
		} else if (e.getSource() == cg.omok) {
			cg.setVisible(false);
			System.out.println("�����ư ����");
		} else if (e.getSource() == dg.b1) {
			dg.b1.setVisible(false);
			writer.println("[CASTD]");
		} else if (e.getSource() == mgp.send) {
			System.out.println("�޽��� ���� ��ư");
		} else if (e.getSource() == rg.regame){
			System.out.println("�÷�");
		}else if (e.getSource() == rg.out){
			System.out.println("���� �ȵ���");
		}

	}

	public void sizeChange(int x, int y) {
		setSize(x, y);
		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}

	public static void main(String[] args) {
		Client cl = new Client();
		cl.connect();
		cl.start();
	}

}
