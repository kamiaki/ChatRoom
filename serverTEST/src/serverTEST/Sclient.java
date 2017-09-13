package serverTEST;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
		Cconnect = true;
		
		try {
			os = s.getOutputStream();
			is = s.getInputStream();
			
			
			
			String fanhui = "��������" + clients.size() + "����";	
			os.write(fanhui.getBytes());							//���������ǵڼ���                
			
			se.textArea.append("\r\n�ͻ���"+name+"����");			
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
                length = is.read(JSwzbyte);				//������Ϣ
                str = new String(JSwzbyte,0,length);                
                String pd = str.substring(0,3);
                
                if(pd.equals("�ǳ�#")){                       //���������ϢΪ-1 �����Ѿ��뿪��
                	clients.remove(this);
                	this.Cconnect = false;
                	this.s.close(); 
                	str = this.getName() + "�뿪�ˣ�";
                }
                
				se.textArea.append("\r\n" + str);
				for(int k = 0;k<clients.size();k++){
					Sclient nc = clients.get(k);
					nc.send(str);
				}
			}
		} catch (EOFException e) {
			// TODO: handle exception
			se.textArea.append("\r\n�û�"+ i +"�뿪");
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
	 * �������ֺ���
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
		String str = "�ͻ���" + this.getName()  + "���뷿��";
		for(Iterator<Sclient> it = clients.iterator();it.hasNext();){
			Sclient nc = it.next();
			nc.send(str);												//�ͻ�����ʾ�ͻ�������
		}
	}
}
