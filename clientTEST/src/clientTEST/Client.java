package clientTEST;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JTextArea;

/**
 * �ͻ�����
 * @author Administrator
 *
 */
public class Client {
	Socket ClientSocket = null;
	String IP = "";
	int PORT = 0;
	boolean PDLink = false;
    
    /**
     * ���캯��
     * @param ip
     * @param port
     */
	public Client(String ip,int port) {
		this.IP = ip;
		this.PORT = port;
	}
	
	/**
	 * ���������
	 */
	public boolean Link() {
		try {
			ClientSocket = new Socket(IP, PORT);
			PDLink = true;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * ����˶Ͽ�
	 */
	public boolean cut(){
		try {
			PDLink = false;	
			ClientSocket.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ������Ϣ
	 * @param str
	 */
	public boolean Write(String str) {
		try {
			OutputStream out = ClientSocket.getOutputStream();
              byte[] FSwzbyte = str.getBytes();
			out.write(FSwzbyte);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ������Ϣ
	 * @param text
	 */
	public void read(JTextArea text) {
		new Thread(new Runnable() {		
			public void run() {
			    String JSWz = "";
				try {
					InputStream in = ClientSocket.getInputStream();
					while (PDLink) {			
						 byte[] JSwzbyte = new byte[1000];
		                 int length = 0;                
		                 length = in.read(JSwzbyte);//1��������Ϣ�ĳ���
		                 JSWz = new String(JSwzbyte, 0, length);
		                 if(text.getText().trim().equals("")){
		                	 text.append(JSWz); 
		                 }else{
		                	 text.append("\r\n" + JSWz); 
		                 }  	                                   
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
