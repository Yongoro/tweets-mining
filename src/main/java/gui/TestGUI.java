package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;

public class TestGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGUI window = new TestGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		JMenuItem fileItemOpen = new JMenuItem("Open");
		menuFile.add(fileItemOpen);
		
		JSeparator separator = new JSeparator();
		menuFile.add(separator);
		
		JMenuItem fileItemExit = new JMenuItem("Exit");
		menuFile.add(fileItemExit);
		
		JMenu menuBD = new JMenu("Database");
		menuBar.add(menuBD);
		
		JMenuItem dbItemOpen = new JMenuItem("Open");
		menuBD.add(dbItemOpen);
		
		JMenuItem dbItemClose = new JMenuItem("Close");
		menuBD.add(dbItemClose);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JProgressBar progressBar = new JProgressBar();
		frame.getContentPane().add(progressBar, BorderLayout.SOUTH);
		
		JPanel topPanel = new JPanel();
		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		
		JButton btnBack = new JButton("Back");
		topPanel.add(btnBack);
		
		JButton btnNext = new JButton("Next");
		topPanel.add(btnNext);
		
		JPanel centerPanel = new JPanel();
		frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
		
	}

}
