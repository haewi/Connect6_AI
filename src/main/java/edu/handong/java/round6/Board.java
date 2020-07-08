package edu.handong.java.round6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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
	public static final int RED = 1;
	public static final int COM = 2;
	public static final int USER = 3;
	
	// 한 턴에 주는 초
	public static final int TURN = 150;
	
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
	Timer timer = new Timer();
	int c=0;
	JLabel timeover = new JLabel("There is only 5 seconds left!");
	
	// 효과음
	AudioInputStream stream1, stream2, stream3;
	Clip clip1, clip2, clip3;
	
	Color computer;
	Color user;
	
	/**
	 * Launch the application.
	 * @return 
	 */
//	public static void main(String[] args) {
	public static void startBoard(Color com, Color use) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Board(com, use);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
//	}
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					new Board();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public Board(Color com, Color use) {
		
		computer = com;
		user = use;
		
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
		
		// 바둑
		board = new BoardPanel(this);
		board.setLayout(null);
		board.setBounds(5, 5, 772, 800);
		board.setOpaque(false);
		mainFrame.getContentPane().add(board);
		board.addMouseListener(board);
		
		// 바둑판
		BoardLine line = new BoardLine();
		line.setBounds(5, 5, 762, 800);
		line.setOpaque(false);
		contentPane.add(line);
		
		// 현재 순서
		player.setHorizontalAlignment(SwingConstants.CENTER);
		player.setFont(new Font("Chalkboard", Font.PLAIN, 20));
		player.setBounds(775, 70, 115, 30);
		contentPane.add(player);
		
		// 타이머 시간
		time.setHorizontalAlignment(SwingConstants.CENTER);
		time.setFont(new Font("Chalkboard", Font.PLAIN, 20));
		time.setBounds(775, 180, 115, 30);
		contentPane.add(time);
		
		// 타이머 시간
		timeover.setHorizontalAlignment(SwingConstants.CENTER);
		timeover.setFont(new Font("Chalkboard", Font.PLAIN, 30));
		timeover.setBounds(200, 120, 400, 50);
		timeover.setVisible(false);
		timeover.setOpaque(true);
		timeover.setBackground(new Color(255, 255, 255, 180));
		timeover.setForeground(Color.red);
		contentPane.add(timeover);
		
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
		
		// start button
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(turn==RED) {
					if(computer.equals(Color.black)) {
						turn = COM;
						player.setText("Computer");
						board.computerTurn();
					}
					else {
						turn = USER;
						player.setText("User");
						startTime();
					}
					count=0;
				}
			}
		});
		startButton.setHorizontalAlignment(SwingConstants.CENTER);
		startButton.setFont(new Font("Chalkboard", Font.PLAIN, 30));
		startButton.setBounds(5, 250, 105, 50);
		info.add(startButton);
		
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
					if(clip1!=null) clip1.stop();
					stream1 = null;
					stream2 = null;
					stream3 = null;
					clip1 = null;
					clip2 = null;
					clip3 = null;
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
							timer.cancel();
							timer.purge();
							time.setText("");
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
		// 승리의 효과음 파일 열기
		try {
			stream1 = AudioSystem.getAudioInputStream(new File("soundTrack/minion_laugh.wav"));
			clip1 = AudioSystem.getClip();
			clip1.open(stream1);
			clip1.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		win.setText(str + " Won!");
		win.setVisible(true);
		win.setOpaque(true);
		win.setBackground(new Color(255, 255, 255, 180));
		turn = DEFAULT;
		timer.cancel();
		timer.purge();
		time.setText("");
		board.repaint();
	}

	public void startTime() {
		if(leftTime!=TURN) {
			leftTime = TURN;
			timer.cancel();
			timer.purge();
//			System.out.println("cancel");
		}
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(leftTime>=0) {
					time.setText(Integer.toString(leftTime));
					leftTime--;
					if(leftTime == 5) {
						warningSound();
					}
				}
				else {
					turnShiftSound();
					timer.cancel();
					timer.purge();
					changeTurn();
				}
			}
		};
		
		timer = new Timer();
		timer.schedule(task, 0, 1000);
//		System.out.println("start");
	}
	
	private void changeTurn() {
		if(count%4==1 || count%4==3) {
			count+=2;
		}
		else {
			count++;
		}
		
		if(count%4 == 3) {
			turn = Board.COM;
			player.setText("Computer");
		}
		else if(count%4 == 1) {
			turn = Board.USER;
			player.setText("User");
		}
		startTime();
		clip2.stop();
	}
	
	private void warningSound() {
		// 시간 부족 효과음 넣기
		try {
			stream2 = AudioSystem.getAudioInputStream(new File("soundTrack/smb_warning.wav"));
			clip2 = AudioSystem.getClip();
			clip2.open(stream2);
			clip2.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		c=0;
		Timer labelTimer = new Timer();
		TimerTask labelTask = new TimerTask() {
			@Override
			public void run() {
				if(c<6) {
					if(c%2==0) {
						timeover.setVisible(true);
					}
					else {
						timeover.setVisible(false);
					}
				}
				else {
					labelTimer.cancel();
					labelTimer.purge();
				}
				c++;
			}
		};
		labelTimer.schedule(labelTask, 0, 500);
	}
	
	private void turnShiftSound() {
		// 시간 부족 효과음 넣기
		try {
			stream3 = AudioSystem.getAudioInputStream(new File("soundTrack/boing.wav"));
			clip3 = AudioSystem.getClip();
			clip3.open(stream3);
			clip3.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}






