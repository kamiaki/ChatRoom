package serverTEST;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainServer {
	private JFrame frame;
	
	JScrollPane scrollPane;
	public JTextArea textArea;
	public JButton buttonStart;
	public JButton buttonoff;

	Sserver sserver;
	
	public buttonlistener btl;
	/**
	 * 主函数
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainServer window = new MainServer();
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
	public MainServer() {
		initialize();
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		btl = new buttonlistener();
		//*****************************************************主窗口
		frame = new JFrame();
		frame.setBounds(100, 100, 261, 242);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		//*****************************************************控件
		buttonStart = new JButton("启动");
		buttonStart.setBounds(34, 10, 83, 34);
		buttonStart.addActionListener(btl);
		buttonStart.setActionCommand("启动");
		
		buttonoff = new JButton("关闭");
		buttonoff.setEnabled(false);
		buttonoff.setBounds(127, 10, 83, 34);
		buttonoff.addActionListener(btl);
		buttonoff.setActionCommand("关闭");
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 64, 176, 125);	
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);	
		//*****************************************************顺序
		frame.getContentPane().add(buttonStart);
		frame.getContentPane().add(buttonoff);
		frame.getContentPane().add(scrollPane);
	}
	
	public class buttonlistener implements ActionListener{	
		@Override
		public void actionPerformed(ActionEvent arg0){
			switch (arg0.getActionCommand()) {
			case "启动":	
				JOptionPane.showMessageDialog(null, "启动...");
				buttonoff.setEnabled(true);
				buttonStart.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						sserver = new Sserver(MainServer.this, 1111);
						sserver.SetstartPD(true);
						sserver.strat();
					}
				}).start();			
				break;
			case "关闭":
				JOptionPane.showMessageDialog(null, "关闭...");				
				buttonoff.setEnabled(false);
				buttonStart.setEnabled(true);
				sserver.SetstartPD(false);
					try {
						sserver.cut();
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				break;
			default:
				break;
			}
		}
	}
}
