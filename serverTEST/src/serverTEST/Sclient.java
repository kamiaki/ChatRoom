package serverTEST;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		Cconnect = true;
		
		try {
			os = s.getOutputStream();
			is = s.getInputStream();
			
			
			
			String fanhui = "房间内有" + clients.size() + "个人";	
			os.write(fanhui.getBytes());							//告诉他你是第几号                
			
			se.textArea.append("\r\n客户端"+name+"进入");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	@Override
	public void run() {	
		try {
			while(Cconnect){
				String str;			
				byte[] JSwzbyte = new byte[1024];
                int length = 0;           
                length = is.read(JSwzbyte);				//接受信息
                str = new String(JSwzbyte,0,length);                
                String pd = str.substring(0,3);
                
                if(pd.equals("登出#")){                       //如果接受信息为-1 代表已经离开了
                	clients.remove(this);
                	this.Cconnect = false;
                	this.s.close(); 
                	str = this.getName() + "离开了！";
                }
                
				se.textArea.append("\r\n" + str);
				for(int k = 0;k<clients.size();k++){
					Sclient nc = clients.get(k);
					nc.send(str);
				}
			}
		} catch (EOFException e) {
			// TODO: handle exception
			se.textArea.append("\r\n用户"+ i +"离开");
			clients.remove(this);
			this.Cconnect = false;		
		}catch (Exception e){
			e.printStackTrace();
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
			os.write(str.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	public String getName() {
		return name;
	}
	public void setNameTOall(String name) {
		this.name = name;
		String str = "客户端" + this.getName()  + "进入房间";
		for(Iterator<Sclient> it = clients.iterator();it.hasNext();){
			Sclient nc = it.next();
			nc.send(str);												//客户端显示客户端数据
		}
	}
}
