package othello;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameControl {
	public static final int N = 8;
	public static final int SIZE = 64;
	public static int x = -1;
	public static int y = -1;
	public GameControl gc;	
	private Tile tile;
	private int[][] gameMap = new int[N][N];
	private int gap = 0;
	private int player1 = 0;
	private int player2 = 0;
	private int gameSet = 0;
	private int end = 0;

	public void gameStatus(Tile tile) {  //////////////////����â�� �ʱ�ȭ ���·� ����
		for(int i = 0; i < N ; i++){
			for(int j = 0; j < N ; j++){
				tile.changeStone(i, j, 0);
			}
		}
		
		tile.changeStone(3, 3, 1);
		tile.changeStone(4, 4, 1);
		tile.changeStone(3, 4, 1);
		tile.changeStone(4, 3, 1);

	}

	public int checkEnd() { // ���� ��Ȳ�� ������ �������� �ȳ������� �ܼ� �˻��Ѵ�.
		gap = 0;
		player1 = 0;
		player2 = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (gameMap[i][j] == 0)
					gap++;
				if (gameMap[i][j] == 1)
					player1++;
				if (gameMap[i][j] == 2)
					player2++;
			}
		}
		if (gap == 0) { // �Ѱ��� ������
			if (player1 > player2) // P1�� ������ P1�� �¸�
				return 1;
			else if (player1 < player2) // p2�� ������ P2�� �¸�
				return 2;
			else
				return 3; // ������ ���
		}
		if (gap != 0 && player1 == 0) // �Ѱ��� ������ �÷��̾� 1�� ���� �ϳ��� ������ �÷��̾�1�� �й�
			return 2;
		else if (gap != 0 && player2 == 0) // �Ѱ��� ������ �÷��̾� 2�� ���� �ϳ��� ������ �÷��̾�2��
											// �й�
			return 1;

		return 0; // ��� ��쿡 �ش��� �ȵɰ�� ���� ����
	}

	public boolean placeCheck(int player, int x, int y) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (((x + i >= 0 && x + i <= 7) && (y + j >= 0 && y + j <= 7))) {
					if (gameMap[i + x][j + y] != 0 && gameMap[x][y] == 0) {
						Point a = new Point(x + i, y + j);
						if (takeStoneCheck(player, a, false, i, j)) {
							return true;
						}

					}

				}
			}
		}
		return false;
	}

	// ����ִ� �ڸ��߿� �÷��̾ �� �� �ִ� ���� �ִ��� üũ
	public boolean allPlaceCheck(int player) {
		boolean check = false;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (gameMap[i][j] == 0 && placeCheck(player, i, j)) {
					check = true;
				}
			}
		}
		return check;
	}

	public boolean takeStoneCheck(int player, Point a, boolean check, int x, int y) {
		//////// �˻��� ��ġ�� ���� ���̸� �˻����� ����
		if ((a.x) < 0 || (a.x) > 7 || (a.y) < 0 || (a.y) > 7)
			return false;

		// �˻� ��ġ�� ��ĭ�̰ų� ���� �÷��̾��� ���� ������ �˻����� ����
		if (gameMap[a.x][a.y] == 0)
			return false;
		else if ((gameMap[a.x][a.y] != 0) && (gameMap[a.x][a.y] != player)) {
			a.x += x;
			a.y += y;
			if (!takeStoneCheck(player, a, true, x, y))
				return false;
		} else if (gameMap[a.x][a.y] == player) {
			if (check == false)
				return false;
		}

		return true;
	}

	public GameControl() {
		gameMap = new int[N][N];
		//tile = new Tile(this);
		//add(tile);		
	}

	public int getgameMap(int i, int j) {
		return gameMap[i][j];
	}

	public void setgameMap(int i, int j, int trun) {
		gameMap[i][j] = trun;
	}

	public void printMap() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++)
				System.out.print(gameMap[i][j] + " ");
			System.out.println();
		}
		if (x != -1 && y != -1)
			System.out.println("�÷��̾�" + gameMap[x][y] + "�� ���� �Ϸ�");
			
	}
}
