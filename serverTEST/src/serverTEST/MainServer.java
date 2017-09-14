package serverTEST;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MainServer {
	private JFrame frame;
	
	JScrollPane scrollPane;
	public JTextArea textArea;
	public JButton buttonStart;
	public JButton buttonoff;
	public JLabel lblNewLabel;
	
	public JMenuBar menuBar;
	public JMenu mnNewMenu;
	public JMenuItem mntmNewMenuItem;
	public JMenuItem mntmNewMenuItem_1;
	public JMenu mnNewMenu_1;
	public JMenuItem mntmNewMenuItem_2;
		
	public buttonlistener btl;
	
	Sserver sserver;	
	int PORT = 0;
	/**
	 * ������
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
	 * ���캯��
	 */
	public MainServer() {
		initialize();
	}

	/**
	 * ��ʼ��
	 */
	private void initialize() {
		btl = new buttonlistener();
		//*****************************************************������
		frame = new JFrame();
		frame.setBounds(100, 100, 247, 274);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		//*****************************************************�ؼ�
		buttonStart = new JButton("����");
		buttonStart.setBounds(24, 10, 83, 34);
		buttonStart.addActionListener(btl);
		buttonStart.setActionCommand("����");
		
		buttonoff = new JButton("�ر�");
		buttonoff.setEnabled(false);
		buttonoff.setBounds(117, 10, 83, 34);
		buttonoff.addActionListener(btl);
		buttonoff.setActionCommand("�ر�");
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 54, 176, 125);	
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);	
		
		lblNewLabel = new JLabel("port");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(24, 189, 176, 15);

		menuBar = new JMenuBar();
		mnNewMenu = new JMenu("����");
		mntmNewMenuItem = new JMenuItem("����");
		mntmNewMenuItem.setActionCommand("����");
		mntmNewMenuItem.addActionListener(btl);
		
		mntmNewMenuItem_1 = new JMenuItem("�˳�");
		mntmNewMenuItem_1.setActionCommand("�˳�");
		mntmNewMenuItem_1.addActionListener(btl);
		
		mnNewMenu_1 = new JMenu("����");
		mntmNewMenuItem_2 = new JMenuItem("�˿ں�..");
		mntmNewMenuItem_2.setActionCommand("�˿ں�");
		mntmNewMenuItem_2.addActionListener(btl);
		//*****************************************************˳��
		frame.getContentPane().add(buttonStart);
		frame.getContentPane().add(buttonoff);
		frame.getContentPane().add(scrollPane);
		frame.getContentPane().add(lblNewLabel);
		
		frame.setJMenuBar(menuBar);
		menuBar.add(mnNewMenu);
		mnNewMenu.add(mntmNewMenuItem);
		mnNewMenu.add(mntmNewMenuItem_1);
		menuBar.add(mnNewMenu_1);
		mnNewMenu_1.add(mntmNewMenuItem_2);
		//*****************************************************��������
		updata();
	}
	
	public class buttonlistener implements ActionListener{	
		@Override
		public void actionPerformed(ActionEvent arg0){
			switch (arg0.getActionCommand()) {
			case "����":
				PORT = Integer.parseInt(FileRelevant.filepathread("port.txt"));
				JOptionPane.showMessageDialog(null, "����...");
				buttonoff.setEnabled(true);
				buttonStart.setEnabled(false);
				mntmNewMenuItem.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						sserver = new Sserver(MainServer.this, PORT);
						sserver.strat();
					}
				}).start();			
				break;
			case "�ر�":
				JOptionPane.showMessageDialog(null, "�ر�...");				
				buttonoff.setEnabled(false);
				buttonStart.setEnabled(true);
				mntmNewMenuItem.setEnabled(true);
					try {
						if(sserver != null)
							sserver.cut();
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				break;
			case "�˳�":
				System.exit(0);
				break;
			case "�˿ں�":
				String port = JOptionPane.showInputDialog("����˿ں�");
				FileRelevant.filepathsave("port.txt", port);	
				updata();
				break;
			default:
				break;
			}
		}
	}
	
	public void updata(){
		lblNewLabel.setText("�˿ں�:" + FileRelevant.filepathread("port.txt"));
	}
}
