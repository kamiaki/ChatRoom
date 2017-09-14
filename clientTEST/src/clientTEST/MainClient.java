package clientTEST;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.AutumnSkin;
import org.pushingpixels.substance.api.skin.BusinessBlueSteelSkin;

public class MainClient {

	private JFrame frame;

	private JButton Button_send;
	private JTextField textField_send;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel Label_NameZT;
	private JLabel Label_IpPort;
	
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem MenuItem_setname;
	private JMenuItem MenuItem_login;
	private JMenuItem MenuItem_exit;
	private JMenu mnNewMenu_1;
	private JMenuItem MenuItem_ipport;
	
	private buttonlistener btl;
	private keyListener keyl;
	
	private Client client;
	String ip;
	String Port;
	String name;
	String Login = "����";
	/**
	 * ������
	 */
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SubstanceLookAndFeel.setSkin(new AutumnSkin());  
					MainClient window = new MainClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * ���캯��
	 */
	public MainClient() {
		initialize();
	}

	/**
	 * ��ʼ������
	 */
	private void initialize() {
		btl = new buttonlistener();
		keyl = new keyListener();
		//*********************************************����
		frame = new JFrame("��������" + "    By:Aki");
		
//		ImageIcon ICOa = new ImageIcon("ico.png");   						//����ͼ��
		ImageIcon ICOa = new ImageIcon(getClass().getResource("/ico.png"));   
		Image Imagea = ICOa.getImage();	
		frame.setIconImage(Imagea);
		
		frame.setBounds(100, 100, 333, 283);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);	
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(client != null){
					if(client.PDLink){
						client.Write("�ǳ�#" + name);
						client.cut();
					}
				}
				System.exit(0);
			}		
		});
		//*********************************************�ؼ�
		Label_IpPort = new JLabel("IP�˿ں�");
		Label_IpPort.setHorizontalAlignment(SwingConstants.RIGHT);
		Label_IpPort.setBounds(10, 207, 294, 15);
		
		Label_NameZT = new JLabel("�ǳ�");
		Label_NameZT.setHorizontalAlignment(SwingConstants.LEFT);
		Label_NameZT.setBounds(28, 10, 190, 30);
		
		Button_send = new JButton("����");
		Button_send.setBounds(28, 167, 76, 30);
		Button_send.addActionListener(btl);
		Button_send.setActionCommand("����");
		Button_send.setEnabled(false);
		
		textField_send = new JTextField();
		textField_send.setBounds(119, 168, 185, 30);
		textField_send.addKeyListener(keyl);
		textField_send.setEnabled(false);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(28, 50, 276, 95);
		textArea = new JTextArea();
		textArea.setEnabled(false);
		scrollPane.setViewportView(textArea);
		
		menuBar = new JMenuBar();
		mnNewMenu = new JMenu("��¼");		
		MenuItem_setname = new JMenuItem("�����ǳ�..");	
		MenuItem_setname.addActionListener(btl);
		MenuItem_setname.setActionCommand("�����ǳ�");
		
		MenuItem_login = new JMenuItem("��¼");
		MenuItem_login.addActionListener(btl);
		MenuItem_login.setActionCommand("��¼");	
		
		MenuItem_exit = new JMenuItem("�˳�");	
		MenuItem_exit.addActionListener(btl);
		MenuItem_exit.setActionCommand("�˳�");
		
		mnNewMenu_1 = new JMenu("����");	
		MenuItem_ipport = new JMenuItem("IP,�˿ں�..");
		MenuItem_ipport.addActionListener(btl);
		MenuItem_ipport.setActionCommand("IP�˿ں�");
		
		//*********************************************˳��	
		frame.getContentPane().add(Label_IpPort);
		frame.getContentPane().add(Label_NameZT);
		frame.getContentPane().add(Button_send);
		frame.getContentPane().add(textField_send);	
		frame.getContentPane().add(scrollPane);	

		frame.setJMenuBar(menuBar);
		menuBar.add(mnNewMenu);
		mnNewMenu.add(MenuItem_setname);
		mnNewMenu.add(MenuItem_login);	
		mnNewMenu.add(MenuItem_exit);
		menuBar.add(mnNewMenu_1);
		mnNewMenu_1.add(MenuItem_ipport);
		//*********************************************���¿ؼ�
		updata();
	}
	
	/**
	 * ������ť�¼�
	 * @author Administrator
	 *
	 */
	public class buttonlistener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "��¼":
				if(MenuItem_login.getText().equals("��¼")){					
					client = new Client(ip, Integer.parseInt(Port));
					if(client.Link()){
						if(!client.Write("��¼#" + name)){
							textArea.setText("�������쳣�������µ�¼��");
						}
						client.read(textArea);
						MenuItem_login.setText("�ǳ�");
						textField_send.setEnabled(true);
						Button_send.setEnabled(true);
						MenuItem_ipport.setEnabled(false);
						MenuItem_exit.setEnabled(false);
						MenuItem_setname.setEnabled(false);
						Login = "����";
						updata();
					}else {
						textArea.setText("��½ʧ�ܣ�IP��ַ��˿ںŴ���");
					}
				}else{
					if(!client.Write("�ǳ�#" + name)){
						textArea.setText("�������쳣�������µ�¼��");
					}
					client.cut();
					MenuItem_login.setText("��¼");
					textField_send.setEnabled(false);
					Button_send.setEnabled(false);
					MenuItem_ipport.setEnabled(true);
					MenuItem_exit.setEnabled(true);
					MenuItem_setname.setEnabled(true);
					Login = "����";
					updata();
				}

				break;
			case "����":		
				if(!client.Write(textField_send.getText())){
					textArea.setText("�������쳣�������µ�¼��");
				}
				textField_send.setText("");
				updata();
				break;
			case "�����ǳ�":
				name = JOptionPane.showInputDialog("�������?");
				while (name == null || name.trim().equals("")) {
					name = JOptionPane.showInputDialog("���ָ�ʽ�����δ�������֣����������룡");	
				}
				FileRelevant.filepathsave("ClientName.txt", name);
				updata();
				break;
			case "�˳�":
				if(client != null){
					if(client.PDLink){
						client.Write("�ǳ�#" + name);
						client.cut();
					}
				}
				System.exit(0);
				break;
			case "IP�˿ں�":
				ip = JOptionPane.showInputDialog("IP?");
				Port = JOptionPane.showInputDialog("Port?");
				FileRelevant.filepathsave("ServerIp.txt", ip);
				FileRelevant.filepathsave("ServerPort.txt", Port);
				updata();
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * ���̼���
	 */
	public class keyListener implements KeyListener{
		public void keyPressed(KeyEvent  e)
		{   
			if(e.getSource() == textField_send)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER) //�жϰ��µļ��Ƿ��ǻس���
				{  
					if(!client.Write(textField_send.getText())){
						textArea.setText("�������쳣�������µ�¼��");
					}
					textField_send.setText("");
					updata();
				}
			}  
		} 
		public void keyReleased(KeyEvent e){  
		}
		public void keyTyped(KeyEvent  e){  
		}
	}
	/**
	 * ��������
	 */
	public void updata(){   
		ip = FileRelevant.filepathread("ServerIp.txt");
		Port = FileRelevant.filepathread("ServerPort.txt");
		name = FileRelevant.filepathread("ClientName.txt");
		Label_IpPort.setText("������    IP��ַ��" + ip + "    �˿ںţ�" + Port);
		Label_NameZT.setText("�ǳƣ�" + name + "    ״̬��" + Login);
	}
}
