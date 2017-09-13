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
	
	public void SetstartPD(boolean pd){
		this.startPD = pd;
	}
	
	public void cut(){
		try {
			if (ServerS != null) {
				ServerS.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void strat(){
		if(startPD){		
			try {
				ServerS = new ServerSocket(PORT);								//���������
				while(startPD){													//ѭ������
					Socket s = ServerS.accept();								//��������һ���ͻ���
					//***************************************��ȡ����
					InputStream in = s.getInputStream();
					byte[] JSwzbyte = new byte[1000];               
	                int length = in.read(JSwzbyte);
	                name = new String(JSwzbyte, 0, length);
	                int FHwz = name.indexOf("#");
	                name = name.substring(FHwz+1);
	                
	                Sclient nc = new Sclient(s, MS, LinkNumber,name,clients);
					nc.setNameTOall(name);										//��ȡ�ͻ�����
					LinkNumber++;												//������ +1
					new Thread(nc).start();										//�����ͻ����߳�
					clients.add(nc);											//����������� �ͻ�����Ŀ
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
}
