package edu.handong.java.round5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

public class Board {
	
	Board mainBoard = this;
	
	JFrame mainFrame = new JFrame();
	private JPanel contentPane;
	BoardPanel board = null;
	JPanel info = new JPanel();
	
	// 놓을 돌 종류
	public static final int DEFAULT = 0;
	public static final int CLEAR = 1;
	public static final int RED = 2;
	public static final int BLACK = 3;
	public static final int WHITE = 4;
	
	// 현재 놓을 돌 종류
	int turn = DEFAULT;
	
	// 돌 개수
	int count=0;
	
	// 현재 플레이어 순서
	JLabel player = new JLabel("");
	
	// 승리의 메세지
	JLabel win = new JLabel("");
	
	// 현재 남은 시간
	int leftTime=15;
	JLabel time = new JLabel("");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Board();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Board() {
		// 메인 Frame
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds(0, 0, 900, 855);
		mainFrame.setVisible(true);
		
		// content panel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainFrame.setContentPane(contentPane);
		contentPane.setLayout(null);
//		contentPane.setBackground(new Color(207, 174, 102));
		
		// 바둑판
		board = new BoardPanel(this);
		board.setLayout(null);
		board.setBounds(5, 5, 762, 800);
		board.setOpaque(false);
		mainFrame.getContentPane().add(board);
		board.addMouseListener(board);
		
		// 현재 순서
		player.setHorizontalAlignment(SwingConstants.CENTER);
		player.setFont(new Font("Chalkboard", Font.PLAIN, 20));
		player.setBounds(775, 70, 115, 30);
		contentPane.add(player);
		
		// 타이머 시간
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				while(leftTime>0) {
					System.out.println(leftTime);
					leftTime--;
				}
				timer.cancel();
			}
		};
		
		// 정보판
		info.setBounds(775, 5, 115, 800);
		info.setLayout(null);
		info.setBackground(new Color(255, 255, 255, 180));
		mainFrame.getContentPane().add(info);
		
		// 타이머
		JLabel timerLabel = new JLabel("Timer");
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(new Font("Chalkboard", Font.PLAIN, 30));
		timerLabel.setBounds(0, 140, 115, 30);
		info.add(timerLabel);
		
		// 순서
		JLabel turnInfo = new JLabel("Turn");
		turnInfo.setHorizontalAlignment(SwingConstants.CENTER);
		turnInfo.setFont(new Font("Chalkboard", Font.PLAIN, 30));
		turnInfo.setBounds(0, 30, 115, 30);
		info.add(turnInfo);
		
		win.setHorizontalAlignment(SwingConstants.CENTER);
		win.setFont(new Font("Gill Sans", Font.PLAIN,  70));
		win.setForeground(Color.red);
		win.setBounds(200, 375, 400, 100);
		win.setVisible(false);
		board.add(win);
		
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		JMenu game = new JMenu("Game");
		menuBar.add(game);
		
		JMenuItem start = new JMenuItem("Start Game");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(turn == DEFAULT) {
					turn = RED;
					player.setText("Red");
					win.setVisible(false);
					board.removeAll();
					board.repaint();
				}
				else {
					JFrame check = new JFrame();
					check.setVisible(true);
					check.setBounds(200, 300, 400, 130);
					check.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					check.getContentPane().setLayout(null);
					
					JLabel checkMessage = new JLabel("Do you really want to restart your game?");
					checkMessage.setFont(new Font("Chalkboard", Font.PLAIN, 16));
					checkMessage.setBounds(40, 20, 320, 30);
					check.getContentPane().add(checkMessage);
					
					JButton yes = new JButton("Yes");
					JButton no = new JButton("No");
					
					yes.setFont(new Font("Chalkboard", Font.PLAIN, 14));
					no.setFont(new Font("Chalkboard", Font.PLAIN, 14));
					
					yes.setBounds(150, 55, 50, 35);
					no.setBounds(210, 55, 50, 35);
					
					yes.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							turn = RED;
							player.setText("Red");
							count=0;
							board.removeAll();
							check.dispose();
							board.repaint();
						}
					});
					no.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							check.dispose();
						}
					});
					
					check.getContentPane().add(yes);
					check.getContentPane().add(no);
				}
			}
			
		});
		game.add(start);
		
		JMenuItem instruction = new JMenuItem("Instruction");
		game.add(instruction);
		

		JLabel woodTexture = new JLabel();
		woodTexture.setBounds(0, 0, 900, 855);
		woodTexture.setIcon(new ImageIcon("/Users/yeahn/Desktop/2020-Summer/Ukmok/image/wood.jpg"));
		woodTexture.setVisible(true);
		contentPane.add(woodTexture);
	}
	
	public void winnerMessage(String str) {
		win.setText(str + " Won!");
		win.setVisible(true);
		win.setOpaque(true);
		win.setBackground(new Color(255, 255, 255, 180));
		turn = DEFAULT;
		board.repaint();
	}
}
