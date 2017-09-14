package serverTEST;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Sserver {
	private boolean startPD = false;				//判断是否接收客户端
	ServerSocket ServerS = null;					//服务端指针初始化
	int LinkNumber = 0;								//连接数
	String name = "";
	
	MainServer MS;									//主窗口类
	int PORT;										//端口号
	
	List<Sclient> clients = new ArrayList<Sclient>();	//客户端 列表
	
	public Sserver(MainServer ms,int port){
		this.MS = ms;
		this.PORT = port;
	}
	
	/**
	 * 断开监听
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
	 * 开始监听
	 */
	public void strat(){
		this.startPD = true;
		if(startPD){		
			try {
				ServerS = new ServerSocket(PORT);								//服务端启动
				while(startPD){													//循环连接
					Socket s = ServerS.accept();								//阻塞连接一个客户端
	                name = GetName(s);											//读取名字
	                Sclient nc = new Sclient(s, MS, LinkNumber,name,clients);	
					new Thread(nc).start();										//启动客户端线程
					clients.add(nc);											//在链表中添加 客户端项目				
					LinkNumber++;												//连接数 +1
				}
			} catch (Exception e) {
				MS.textArea.setText("连接中断！");
				try {
					if(ServerS != null)
						ServerS.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				e.printStackTrace();				
			}finally{
				if (startPD) {
					MS.textArea.append("\r\n阻塞超时，重新启动连接！");
					strat();
				}else{
					MS.textArea.append("\r\n用户主动中断接收文件服务端！");
				}
			}
		}//if startPD
	}//void start
	
	/**
	 * 获取名字
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
