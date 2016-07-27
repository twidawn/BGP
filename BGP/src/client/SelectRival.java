package client;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * �游���
 * �̱���
 * ���ӽ� ������� �ִ� ���� ����� �����ְ�
 * ������ ���ΰ�, �ڽ��� ���� ���� ���� ���ΰ��� ��Ÿ���ִ� �г�
 */
public class SelectRival extends JPanel {
	JButton createRoom, enterRoom;
	JTable table;
	DefaultTableModel model;

	public SelectRival() {
		setLayout(null);

		String[][] row = new String[0][2];
		String[] col = { "���̸�", "�ο�" };

		createRoom = new JButton("�游���");
		enterRoom = new JButton("�����ϱ�");

		model = new DefaultTableModel(row, col);
		table = new JTable(model) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JScrollPane js = new JScrollPane(table);

		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		add(createRoom);
		add(enterRoom);
		add(js);

		js.setBounds(0, 0, 400, 330);
		createRoom.setBounds(50, 340, 100, 35);
		enterRoom.setBounds(250, 340, 100, 35);
	}

}
