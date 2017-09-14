package serverTEST;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Sserver {
	private boolean startPD = false;				//�ж��Ƿ���տͻ���
	ServerSocket ServerS = null;					//�����ָ���ʼ��
	int LinkNumber = 0;								//������
	String name = "";
	
	MainServer MS;									//��������
	int PORT;										//�˿ں�
	
	List<Sclient> clients = new ArrayList<Sclient>();	//�ͻ��� �б�
	
	public Sserver(MainServer ms,int port){
		this.MS = ms;
		this.PORT = port;
	}
	
	/**
	 * �Ͽ�����
	 */
	public void cut(){
		this.startPD = false;
		try {
			if (ServerS != null) {
				ServerS.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ʼ����
	 */
	public void strat(){
		this.startPD = true;
		if(startPD){		
			try {
				ServerS = new ServerSocket(PORT);								//���������
				while(startPD){													//ѭ������
					Socket s = ServerS.accept();								//��������һ���ͻ���
	                name = GetName(s);											//��ȡ����
	                Sclient nc = new Sclient(s, MS, LinkNumber,name,clients);	
					new Thread(nc).start();										//�����ͻ����߳�
					clients.add(nc);											//����������� �ͻ�����Ŀ				
					LinkNumber++;												//������ +1
				}
			} catch (Exception e) {
				MS.textArea.setText("�����жϣ�");
				try {
					if(ServerS != null)
						ServerS.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				e.printStackTrace();				
			}finally{
				if (startPD) {
					MS.textArea.append("\r\n������ʱ�������������ӣ�");
					strat();
				}else{
					MS.textArea.append("\r\n�û������жϽ����ļ�����ˣ�");
				}
			}
		}//if startPD
	}//void start
	
	/**
	 * ��ȡ����
	 */
	public String GetName(Socket socket){
		String readname = "";
		InputStream in = null;
		byte[] JSwzbyte = null;
		int length = 0;
		int FHwz = 0;
		
		try {
			
			in = socket.getInputStream();
			JSwzbyte = new byte[1000];               
	        length = in.read(JSwzbyte);
	        readname = new String(JSwzbyte, 0, length);
	        FHwz = readname.indexOf("#");
	        readname = readname.substring(FHwz+1);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

		return readname;
	}
}
