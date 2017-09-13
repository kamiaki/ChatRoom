package clientTEST;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.FileOutputStream;  
import java.io.InputStream; 
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileRelevant {
	final static int OPEN_File = 0;
	final static int SAVE_File = 1;
	final static int FILE = 0;
	final static int FILES = 1;
	final static int FILEandFILES = 2;
	
	//***********************************************************************�ļ�·�������ȡtxt
	//���ļ�·��������Ŀ¼��
	//1 �ļ������ַ 2 ����
	public static void filepathsave(String path,String NeiRong){
			try {
	            File writename = new File(path); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
	            writename.createNewFile(); // �������ļ�  
	            BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
	            out.write(NeiRong); // \r\n��Ϊ����  
	            out.flush(); // �ѻ���������ѹ���ļ�  
	            out.close(); // ���ǵùر��ļ�  
			} catch (Exception e) {
				e.printStackTrace();
			}		 
	}
	//���ļ�·����ȡ����
	//1 �ļ������ַ
	public static String filepathread(String path){
			String filepath = null;
			File file = new File(path);
			try {		 
				if(file.isFile() && file.exists()){
					InputStreamReader read = new InputStreamReader(new FileInputStream(file),"GBK");
					BufferedReader bfreader = new BufferedReader(read);
					filepath = bfreader.readLine();
					bfreader.close();
				}else{
					filepath = "";
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return filepath;
	}
	
	//***********************************************************************�ļ�·�� ���� �� �Ի���
	//�ļ������Ի���Ŀ���
	//1 ѡ���Ǳ��滹�Ǵ� (0�� 1����)
	public static String fileoperate(int so,int filefiles){
		int filenumber = 0;
		switch (filefiles) {
		case FILE:
			filenumber = JFileChooser.FILES_ONLY;
			break;
		case FILES:
			filenumber = JFileChooser.DIRECTORIES_ONLY;
			break;
		case FILEandFILES:
			filenumber = JFileChooser.FILES_AND_DIRECTORIES;
			break;
		default:
			break;
		}
		String txtpath = "";					//��ȡ�ļ�·��
		String filePath = "";					//���ش�·�����ͱ���·��
		JFileChooser fileChooser = null;		//�ļ��Ի���
		int returnValue = 0; 					//�ļ�·���Ƿ���ж�
		if(so == OPEN_File){
			txtpath = filepathread("openPath.txt");
			if(txtpath.equals(""))
				txtpath = ".\\";
			fileChooser = new JFileChooser(txtpath);
			fileChooser.setFileSelectionMode(filenumber);		//����ѡ���ļ���
			returnValue = fileChooser.showOpenDialog(fileChooser);					//�򿪴򿪶Ի���
			if(returnValue == JFileChooser.APPROVE_OPTION){ 	
				filePath= fileChooser.getSelectedFile().getAbsolutePath();			//getSelectedFile ����ѡ�е��ļ� getAbsolutePath ����ѡ�е��ļ��������ַ		
				filepathsave("openPath.txt", filePath);								//��·��д��ȥ
			}
			if(filePath.equals("")){
				JOptionPane.showMessageDialog(null,"û��ѡ��·����");
			}
		}else{
			txtpath = filepathread("savePath.txt");
			if(txtpath.equals(""))
				txtpath = ".\\";
			fileChooser = new JFileChooser(txtpath);
			fileChooser.setFileSelectionMode(filenumber); 							//����ѡ���ļ����ļ���
			returnValue = fileChooser.showSaveDialog(fileChooser);					//�򿪱���Ի���
			if(returnValue == JFileChooser.APPROVE_OPTION){
				filePath = fileChooser.getSelectedFile().getAbsolutePath();			//getSelectedFile ����ѡ�е��ļ� getAbsolutePath ����ѡ�е��ļ��������ַ
				filepathsave("savePath.txt", filePath);								//��·��д��ȥ
			}
			if(filePath.equals("")){
				JOptionPane.showMessageDialog(null,"û��ѡ��·����");
			}
		}
		return filePath;
	}
	
	//***********************************************************************�ļ����� ���� ɾ�� �ƶ�
	//�ļ�����
	//1 ԭʼ�ļ�λ�� 2 ���Ƶ����ļ�λ��
	public static void filecopy(String oldPath, String newPath){
		int bytesum = 0;												//�ļ���С �����ֽ���
        int byteread = 0;												//�ļ���ȡ �ֽ���
		try{
          File oldfile = new File(oldPath);
          if(oldfile.exists()){											//����ļ�����
              InputStream inStream = new FileInputStream(oldPath);		//����ԭ�ļ�   
              FileOutputStream  fs  =  new  FileOutputStream(newPath);	//���Ƶ��ļ�λ��
              byte[] buffer = new byte[1024];							//һ�ζ������ֽ�
              while((byteread = inStream.read(buffer)) != -1){    
            	  bytesum += byteread;									//�ļ����ֽ���
                  fs.write(buffer, 0, byteread);    
              }    
              inStream.close();   
              fs.close();
          }    
      }    
      catch  (Exception  e)  {    
          e.printStackTrace();    
      }    
	}
	//�ļ�ɾ��
	//1 Ҫɾ�����ļ���ַ
	public static void filedelete(String Path){
		try  {       
	           File myDelFile = new File(Path);    
	           myDelFile.delete();    
	       }    
	       catch  (Exception  e)  {    
	           e.printStackTrace();       
	       }    
	}
	//�ļ��ƶ�
	public static void filemove(String oldPath, String newPath){
		filecopy(oldPath, newPath);    
		filedelete(oldPath);    
	}
	
	//***********************************************************************�ļ������ļ��жϺͲ���
	//��ȡ���ļ������� ������
	//1 �ļ����ԶԵ�ַ 2 �ļ������ļ�����ɵ� �ַ������� 3 �ļ���ȫ·��
	public static int getAllFileName(String path, List<String> fileName, List<String> fileAllPath){
		int numCount = 0;     
        String [] names;
		File file = new File(path);
        if(file.exists()){
	        names = file.list();
	        if(names != null){											//����������鲻Ϊ��
		        fileName.addAll(Arrays.asList(names));      			//��һ������ת��Ϊ���� 
		        numCount = names.length;    							//��ȡ���ļ���        
		        for(int i = 0; i < fileName.size(); i++){				//ѭ�������ļ�����·����ַ
		        	fileAllPath.add(path + "\\" + fileName.get(i));
		        }
	        }
        }
        return numCount;
	}
}
