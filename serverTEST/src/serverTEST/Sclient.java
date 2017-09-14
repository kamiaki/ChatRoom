package serverTEST;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;


public class Sclient implements Runnable{
	Socket s = null;				//�ͻ���
	MainServer se = null;			//�����		
	Boolean Cconnect = false;		//�˿ͻ�������״̬
	String name = null;				//�ͻ�������
	int i = 0;						//�ڼ����ο�			
	InputStream  is = null;			//������
    OutputStream os = null;  		//�����     
    List<Sclient> clients;			//�ͻ�������
    
	String message = "";			//��ʼ��ʱ���͵�����
	String names = "";				//��ʼ��ʱ���߶���˭����
	String ReadStr;					//���յ�����Ϣ
	String SendStr;					//���ص���Ϣ
    /**
     * ���캯��
     * @param s ����ͻ�������
     * @param se ��������
     * @param i �������
     */
	public Sclient(Socket s,MainServer se,int i,String NAME,List<Sclient> CLIENTS){
		this.s = s;
		this.se = se;
		this.i =i;
		this.name = NAME;
		this.clients = CLIENTS;
		this.Cconnect = true;	
		
		se.textArea.append("\r\n�ͻ���"+name+"����");					//֪ͨ�����
	
		try {														//֪ͨ�����ͻ���
			os = s.getOutputStream();
			is = s.getInputStream();						
			
			message = "��������" + clients.size() + "����  " + " ���ǵ�" + i + "������";	
			os.write(message.getBytes());							//���������ǵڼ���                		
			try {													//�ȴ�����
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			for(int nameID = 0; nameID < clients.size(); nameID++){
				names = clients.get(nameID).getName();
				message = names + "������";
				os.write(message.getBytes());		
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setNameTOall();
	}	
	
	/**
	 * ���߳̽�����Ϣ
	 */
	@Override
	public void run() {	
		try {
			while(Cconnect){
//				byte[] JSwzbyte = new byte[1024];
//				int length = 0;           
//				length = is.read(JSwzbyte);				
//				ReadStr = new String(JSwzbyte,0,length);   		//������Ϣ             
               
                InputStreamReader isr = new InputStreamReader(s.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                ReadStr = br.readLine();
                
                String pd = ReadStr.substring(0,3);   
               
                switch (pd) {									//�����û�����������Ϣ�жϲ���
				case "�ǳ�#":
					clients.remove(this);
                	this.Cconnect = false;
                	this.s.close(); 
                	SendStr = this.getName() + "�뿪�ˣ�";     	
                	se.textArea.append("\r\n" + SendStr);
    				for(int k = 0;k<clients.size();k++){
    					Sclient nc = clients.get(k);
    					nc.send(SendStr);
    				}
					break;
				default:
					SendStr = name + "˵:" + ReadStr;
		           	se.textArea.append("\r\n" + SendStr);
    				for(int k = 0;k<clients.size();k++){
    					Sclient nc = clients.get(k);
    					nc.send(SendStr);
    				}
					break;
				}			
			}
		} catch (Exception e) {									//�쳣�жϴ���
			clients.remove(this);
			this.Cconnect = false;	
			SendStr = this.getName() + "�쳣�жϣ�";     	
        	se.textArea.append("\r\n" + SendStr);
			for(int k = 0;k<clients.size();k++){
				Sclient nc = clients.get(k);
				nc.send(SendStr);
			}
		}finally{
			try {
				if(is != null) is.close();
				if(os != null) os.close();					
				if(s!=null) s.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * �������ֺ���
	 * @param str
	 */
	public void send(String str){
		try {
//			os.write(str.getBytes());
			
			PrintStream ps = new PrintStream(s.getOutputStream());
			ps.println(str);
			ps.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * ��ȡ�ͻ�������
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ����������������
	 * @param name
	 */
	public void setNameTOall() {
		message = this.getName()  + " ���뷿��";
		for(Iterator<Sclient> it = clients.iterator();it.hasNext();){
			Sclient nc = it.next();
			nc.send(message);												//�ͻ�����ʾ�ͻ�������
		}
	}
}
