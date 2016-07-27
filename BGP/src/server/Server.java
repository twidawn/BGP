package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static int playerCount = 0;
	public static int mat=0;
	private ResourceManager rm;
	private ControlServer cs;
	private ServerSocket server; // ��������
	private Socket soc; // �������
	private int Port = 9999;
	
	
	public Server(){
		
		rm = new ResourceManager();
	}
	
	public void startServer(){
		try {
			server = new ServerSocket(Port);
			System.out.println("���� ���ϻ���");
			while(true){
				Socket socket = server.accept();
				cs = new ControlServer(socket, server, rm);
				System.out.println("��Ʈ�� ���� ����");	
				cs.start();
				System.out.println("���ҽ� �Ŵ��� ����");
				rm.add(cs);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
	}

	public static void main(String[] args) {

		Server server = new Server();
		server.startServer();
	}

}
