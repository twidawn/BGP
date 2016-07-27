package server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ControlServer extends Thread {
	
	private String userName = null;
	//private int roomNumber=-1;
	private String roomName = null;
	private Socket socket;
	private ServerSocket sc;
	private ResourceManager rm;
	private boolean ready = false;
	private boolean dice = false;
	private int diceNum=0;

	private BufferedReader reader;
	private PrintWriter writer;

	public ControlServer(Socket socket, ServerSocket sc, ResourceManager rm) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.rm = rm;
		this.sc = sc;
	}
	
	Socket getSocket(){
		return socket;
	}
	
	public boolean getDice(){return dice;}
	public void setDice(boolean dice){this.dice = dice;}
	public int getDiceNum(){return diceNum;}
	
	public boolean getReady(){return ready;}
	
	public String getRoomName(){
		return roomName;
	}
	
	/*
	public int getRoomNumber(){
		return roomNumber;
	}
*/
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);

			String msg;

			while ((msg = reader.readLine()) != null) {
				
				if(msg.equals("[GUEST]")){//�Խ�Ʈ �α���
					userName = "GUEST" + Integer.toString(++Server.playerCount);
				} else if(msg.equals("[TWITT]")){//Ʈ���� �α��� ��ư ����
					System.out.println("Ʈ���� ��ư ����");
				} else if(msg.startsWith("[MKROOM]")){//�游��
					String temp = msg.substring(8);
					roomName = temp.substring(0, temp.indexOf(" "));
					rm.allNotice(msg);
				} else if(msg.startsWith("[JOINR]")){
					String temp = msg.substring(7);
					roomName =temp;
					if(rm.roomCheck(temp)){
						writer.println("[JOINR]" + temp);
						rm.roomNotice(this, "[GODICE]");
					}else{
						roomName= null;
						writer.println("[FULLR]");
					}
				}else if(msg.startsWith("[CHECKF]")){
					rm.allNotice(msg);
				}else if(msg.startsWith("[CASTD]")){ //�ֻ��� ����
					dice = true;
					diceNum = (int)(Math.random()*6)+1;
					writer.println("[UDICE]" + diceNum);
					rm.roomMsg(this, "[SETDC]"+diceNum);// ���濡�� �ֻ����� �����Ұ��� ����
					if(rm.checkDice(roomName)){
						if(rm.diceResult(this, diceNum)==1){
							writer.println("[DICEW]");
							rm.roomMsg(this, "[DICEL]");
						}else if(rm.diceResult(this, diceNum)==2){
							rm.roomMsg(this, "[DICEW]");
							writer.println("[DICEL]");
						}else if(rm.diceResult(this, diceNum)==3){
							rm.roomNotice(this, "[GODICE]"); //�ٽ� �ֻ��� ����
						}
					}
				}else if(msg.startsWith("[GOTHE]")){ //�����θ� ������
					rm.roomNotice(this, "[GOTHE]");
				}else if(msg.startsWith("[STONE]")){
					rm.roomMsg(this, msg);
				}else if(msg.startsWith("[ENDTN]")){
					rm.roomMsg(this, "[YOURT]");
				}else if (msg.equals("[WHITE]")){
					rm.roomNotice(this, "[WHITE]");
				}else if( msg.equals("[BLACK]")){
					rm.roomNotice(this, "[BLACK]");
				}else if (msg.equals("[LEAVE]")){
					rm.allNotice("[RMROOM]" + roomName);
					roomName=null;
				}else if (msg.equals("[MYWIN]")){ //�� �¸��ϰ�� ������ ���� ������ �и� �˸���.
					writer.println("[MYWIN]");
					rm.roomMsg(this, "[MYLOS]");
				}else if (msg.equals("[MYLOS]")){//�� �й��ϰ�� ������ �и� ������ ���� �˸���.
					writer.println("[MYLOS]");
					rm.roomMsg(this, "[MYWIN]");
				}else if (msg.equals("[GDRAW]")){ //���� �Ѵ� �����̴�.
					rm.roomNotice(this, "[GDRAW]");
				}
				
				
/*
				if (msg.startsWith("[GUEST]")) {
					userName = "GUEST" + Integer.toString(++Server.playerCount);
					
					roomNumber=1;
					if(rm.roomCheck(1)){
						writer.println("[ROOM]");
						userName = "GUEST" + Integer.toString(++Server.playerCount);
						System.out.println(userName + "�� ����");
						if(rm.gamerCheck(1)){
							//rm.btnEnabled(this, "[BTNEN]"); �θ� �ٿ��� ��ư Ȱ��ȣ
							rm.sendMsg(this, "[BTNEN]"); //üũ��
							System.out.println("�����۵�");
							////////////////////////////�ӽ� �� ���� ����
							rm.sendMsg(this, "[BLACK]");
							writer.println("[WHITE]");														
						}
						else{
							writer.println("[BTNRE]");
							System.out.println("������");
						}
						
					}else{
						writer.println("[FULL]");
						System.out.println("�� �̻� �濡 ���� �� �� �����ϴ�.");
					}
					
				}else if(msg.startsWith("[GOTHE]")){
					rm.btnEnabled(this, "[GMSOS]");
					writer.println("[BTURN]");
					rm.sendMsg(this, "[WTURN]");
				}else if(msg.startsWith("[GOMOK]")){
					
				}else if(msg.startsWith("[STONE]")){
					rm.sendMsg(this, msg);
				}else if(msg.startsWith("[ENDTN]")){
					rm.sendMsg(this, "[YOURT]");
				}
*/
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("�����");
		} finally {
			try {
				//System.out.println("������" + roomName);
				//System.out.println(userName+ "����");
				if(roomName != null && rm.roomCheck(this)){ //���� ����� ���
					rm.allNotice("[RMROOM]" + roomName);
				}else if(roomName != null && !rm.roomCheck(this)){//�濡 ����� �������� ���
					rm.roomMsg(this, "[LEAVE]");
				}
				rm.remove(this);
				if (reader != null)
					reader.close();
				if (writer != null)
					writer.close();
				if (socket != null)
					socket.close();
				reader = null;
				writer = null;
				socket = null;
				System.out.println("�����ڼ� : " + rm.size());
			} catch (Exception e) {
			}
		}

	}

}
