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
	String Login = "离线";
	/**
	 * 主函数
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
	 * 构造函数
	 */
	public MainClient() {
		initialize();
	}

	/**
	 * 初始化函数
	 */
	private void initialize() {
		btl = new buttonlistener();
		keyl = new keyListener();
		//*********************************************窗口
		frame = new JFrame("聊起来！" + "    By:Aki");
		
//		ImageIcon ICOa = new ImageIcon("ico.png");   						//窗体图标
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
						client.Write("登出#" + name);
						client.cut();
					}
				}
				System.exit(0);
			}		
		});
		//*********************************************控件
		Label_IpPort = new JLabel("IP端口号");
		Label_IpPort.setHorizontalAlignment(SwingConstants.RIGHT);
		Label_IpPort.setBounds(10, 207, 294, 15);
		
		Label_NameZT = new JLabel("昵称");
		Label_NameZT.setHorizontalAlignment(SwingConstants.LEFT);
		Label_NameZT.setBounds(28, 10, 190, 30);
		
		Button_send = new JButton("发送");
		Button_send.setBounds(28, 167, 76, 30);
		Button_send.addActionListener(btl);
		Button_send.setActionCommand("发送");
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
		mnNewMenu = new JMenu("登录");		
		MenuItem_setname = new JMenuItem("设置昵称..");	
		MenuItem_setname.addActionListener(btl);
		MenuItem_setname.setActionCommand("设置昵称");
		
		MenuItem_login = new JMenuItem("登录");
		MenuItem_login.addActionListener(btl);
		MenuItem_login.setActionCommand("登录");	
		
		MenuItem_exit = new JMenuItem("退出");	
		MenuItem_exit.addActionListener(btl);
		MenuItem_exit.setActionCommand("退出");
		
		mnNewMenu_1 = new JMenu("设置");	
		MenuItem_ipport = new JMenuItem("IP,端口号..");
		MenuItem_ipport.addActionListener(btl);
		MenuItem_ipport.setActionCommand("IP端口号");
		
		//*********************************************顺序	
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
		//*********************************************更新控件
		updata();
	}
	
	/**
	 * 监听按钮事件
	 * @author Administrator
	 *
	 */
	public class buttonlistener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "登录":
				if(MenuItem_login.getText().equals("登录")){					
					client = new Client(ip, Integer.parseInt(Port));
					if(client.Link()){
						if(!client.Write("登录#" + name)){
							textArea.setText("服务器异常，请重新登录！");
						}
						client.read(textArea);
						MenuItem_login.setText("登出");
						textField_send.setEnabled(true);
						Button_send.setEnabled(true);
						MenuItem_ipport.setEnabled(false);
						MenuItem_exit.setEnabled(false);
						MenuItem_setname.setEnabled(false);
						Login = "在线";
						updata();
					}else {
						textArea.setText("登陆失败，IP地址或端口号错误！");
					}
				}else{
					if(!client.Write("登出#" + name)){
						textArea.setText("服务器异常，请重新登录！");
					}
					client.cut();
					MenuItem_login.setText("登录");
					textField_send.setEnabled(false);
					Button_send.setEnabled(false);
					MenuItem_ipport.setEnabled(true);
					MenuItem_exit.setEnabled(true);
					MenuItem_setname.setEnabled(true);
					Login = "离线";
					updata();
				}

				break;
			case "发送":		
				if(!client.Write(textField_send.getText())){
					textArea.setText("服务器异常，请重新登录！");
				}
				textField_send.setText("");
				updata();
				break;
			case "设置昵称":
				name = JOptionPane.showInputDialog("你的名字?");
				while (name == null || name.trim().equals("")) {
					name = JOptionPane.showInputDialog("名字格式有误或未输入名字，请重新输入！");	
				}
				FileRelevant.filepathsave("ClientName.txt", name);
				updata();
				break;
			case "退出":
				if(client != null){
					if(client.PDLink){
						client.Write("登出#" + name);
						client.cut();
					}
				}
				System.exit(0);
				break;
			case "IP端口号":
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
	 * 键盘监听
	 */
	public class keyListener implements KeyListener{
		public void keyPressed(KeyEvent  e)
		{   
			if(e.getSource() == textField_send)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER) //判断按下的键是否是回车键
				{  
					if(!client.Write(textField_send.getText())){
						textArea.setText("服务器异常，请重新登录！");
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
	 * 更新数据
	 */
	public void updata(){   
		ip = FileRelevant.filepathread("ServerIp.txt");
		Port = FileRelevant.filepathread("ServerPort.txt");
		name = FileRelevant.filepathread("ClientName.txt");
		Label_IpPort.setText("服务器    IP地址：" + ip + "    端口号：" + Port);
		Label_NameZT.setText("昵称：" + name + "    状态：" + Login);
	}
}
