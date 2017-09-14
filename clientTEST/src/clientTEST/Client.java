package clientTEST;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
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
    JTextArea TextArea;
    /**
     * ���캯��
     * @param ip
     * @param port
     */
	public Client(String ip,int port,JTextArea textArea) {
		this.IP = ip;
		this.PORT = port;
		this.TextArea = textArea;
	}
	
	/**
	 * ���������
	 */
	public boolean Link() {
		try {
			PDLink = true;
			ClientSocket = new Socket(IP, PORT);
			readMessage();
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
//			byte[] FSwzbyte = str.getBytes();
//			OutputStream out = ClientSocket.getOutputStream();
//			out.write(FSwzbyte);
			
			PrintStream ps = new PrintStream(ClientSocket.getOutputStream());
			ps.println(str);
			ps.flush();
			
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
	public void readMessage() {
		new Thread(new Runnable() {		
			public void run() {
				try {			
					while (PDLink) {	
						String JSWz = "";
						byte[] JSwzbyte = new byte[1000];
		                int length = 0;    
		                
//						InputStream in = ClientSocket.getInputStream();
//		                length = in.read(JSwzbyte);//1��������Ϣ�ĳ���
//		                JSWz = new String(JSwzbyte, 0, length);
		                
		                InputStreamReader isr = new InputStreamReader(ClientSocket.getInputStream());
		                BufferedReader br = new BufferedReader(isr);
		                JSWz = br.readLine();
		                
		                if(TextArea.getText().trim().equals("")){
		                	TextArea.append(JSWz); 
		                }else{
		                	TextArea.append("\r\n" + JSWz); 
		                }  
		                TextArea.setCaretPosition(TextArea.getDocument().getLength());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
