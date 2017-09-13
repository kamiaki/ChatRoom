package clientTEST;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JTextArea;

/**
 * 客户端类
 * @author Administrator
 *
 */
public class Client {
	Socket ClientSocket = null;
	String IP = "";
	int PORT = 0;
	boolean PDLink = false;
    
    /**
     * 构造函数
     * @param ip
     * @param port
     */
	public Client(String ip,int port) {
		this.IP = ip;
		this.PORT = port;
	}
	
	/**
	 * 服务端连接
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
	 * 服务端断开
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
	 * 发送信息
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
	 * 接收信息
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
		                 length = in.read(JSwzbyte);//1，接受信息的长度
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
