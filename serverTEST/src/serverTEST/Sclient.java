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
	Socket s = null;				//客户端
	MainServer se = null;			//服务端		
	Boolean Cconnect = false;		//此客户端连接状态
	String name = null;				//客户端名字
	int i = 0;						//第几个游客			
	InputStream  is = null;			//输入流
    OutputStream os = null;  		//输出流     
    List<Sclient> clients;			//客户端链表
    
	String message = "";			//初始化时发送的数据
	String names = "";				//初始化时告诉都有谁在线
	String ReadStr;					//接收到的信息
	String SendStr;					//返回的信息
    /**
     * 构造函数
     * @param s 加入客户端连接
     * @param se 主窗口累
     * @param i 加入序号
     */
	public Sclient(Socket s,MainServer se,int i,String NAME,List<Sclient> CLIENTS){
		this.s = s;
		this.se = se;
		this.i =i;
		this.name = NAME;
		this.clients = CLIENTS;
		this.Cconnect = true;	
		
		se.textArea.append("\r\n客户端"+name+"进入");					//通知服务端
	
		try {														//通知其他客户端
			os = s.getOutputStream();
			is = s.getInputStream();						
			
			message = "房间现有" + clients.size() + "个人  " + " 你是第" + i + "个来的";	
			os.write(message.getBytes());							//告诉他你是第几号                		
			try {													//等待半秒
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			for(int nameID = 0; nameID < clients.size(); nameID++){
				names = clients.get(nameID).getName();
				message = names + "—在线";
				os.write(message.getBytes());		
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setNameTOall();
	}	
	
	/**
	 * 多线程接受信息
	 */
	@Override
	public void run() {	
		try {
			while(Cconnect){
//				byte[] JSwzbyte = new byte[1024];
//				int length = 0;           
//				length = is.read(JSwzbyte);				
//				ReadStr = new String(JSwzbyte,0,length);   		//接受信息             
               
                InputStreamReader isr = new InputStreamReader(s.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                ReadStr = br.readLine();
                
                String pd = ReadStr.substring(0,3);   
               
                switch (pd) {									//根据用户发过来的消息判断操作
				case "登出#":
					clients.remove(this);
                	this.Cconnect = false;
                	this.s.close(); 
                	SendStr = this.getName() + "离开了！";     	
                	se.textArea.append("\r\n" + SendStr);
    				for(int k = 0;k<clients.size();k++){
    					Sclient nc = clients.get(k);
    					nc.send(SendStr);
    				}
					break;
				default:
					SendStr = name + "说:" + ReadStr;
		           	se.textArea.append("\r\n" + SendStr);
    				for(int k = 0;k<clients.size();k++){
    					Sclient nc = clients.get(k);
    					nc.send(SendStr);
    				}
					break;
				}			
			}
		} catch (Exception e) {									//异常中断处理
			clients.remove(this);
			this.Cconnect = false;	
			SendStr = this.getName() + "异常中断！";     	
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
	 * 发送文字函数
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
	 * 获取客户端名字
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 告诉所有人你来了
	 * @param name
	 */
	public void setNameTOall() {
		message = this.getName()  + " 进入房间";
		for(Iterator<Sclient> it = clients.iterator();it.hasNext();){
			Sclient nc = it.next();
			nc.send(message);												//客户端显示客户端数据
		}
	}
}
