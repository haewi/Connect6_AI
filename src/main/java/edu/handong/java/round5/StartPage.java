package edu.handong.java.round5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;

public class StartPage extends JFrame {

	StartPage st = this;
	
	private JPanel contentPane;
	int count=0;
	String player1 = "", player2 = "";
	JLabel water1 = new JLabel("Player1");
	JLabel water2 = new JLabel("Player2");
	JLabel fire1 = new JLabel("Player1");
	JLabel fire2 = new JLabel("Player2");
	JLabel earth1 = new JLabel("Player1");
	JLabel earth2 = new JLabel("Player2");
	JLabel air1 = new JLabel("Player1");
	JLabel air2 = new JLabel("Player2");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartPage frame = new StartPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartPage() {
		count=0;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 728, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel welcome = new JLabel("Connect Six");
		welcome.setForeground(new Color(102, 102, 255));
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setBounds(140, 85, 448, 73);
		welcome.setFont(new Font("Copperplate", Font.BOLD, 70));
		welcome.setBackground(Color.white);
		contentPane.add(welcome);
		
		JLabel welcomeImage = new JLabel();
		welcomeImage.setBounds(140, 85, 448, 262);
		ImageIcon icon = new ImageIcon("/Users/yeahn/Desktop/2020-Summer/Ukmok/image/illust.png");
		Image image = icon.getImage();
		image = image.getScaledInstance(448, 262, Image.SCALE_SMOOTH);
		welcomeImage.setIcon(new ImageIcon(image));
		contentPane.add(welcomeImage);
		
		ImageIcon waterIcon = new ImageIcon("/Users/yeahn/Desktop/2020-Summer/Ukmok/image/water_choose.png");
		Image waterImage = waterIcon.getImage();
		waterImage = waterImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JButton water = new JButton(new ImageIcon(waterImage));
		water.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(count==0) {
					player1 = "water";
					water1.setVisible(true);
					count++;
				}
				else {
					if(player1.equals("water")) return;
					player2 = "water";
					water2.setVisible(true);
					startGame();
				}
			}
		});
		water.setBounds(120, 377, 80, 80);
		contentPane.add(water);
		
		ImageIcon fireIcon = new ImageIcon("/Users/yeahn/Desktop/2020-Summer/Ukmok/image/fire_choose.png");
		Image fireImage = fireIcon.getImage();
		fireImage = fireImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JButton fire = new JButton(new ImageIcon(fireImage));
		fire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(count==0) {
					player1 = "fire";
					fire1.setVisible(true);
					count++;
				}
				else {
					if(player1.equals("fire")) return;
					player2 = "fire";
					fire2.setVisible(true);
					startGame();
				}
			}
		});
		fire.setBounds(256, 377, 80, 80);
		contentPane.add(fire);
		
		ImageIcon earthIcon = new ImageIcon("/Users/yeahn/Desktop/2020-Summer/Ukmok/image/earth_choose.png");
		Image earthImage = earthIcon.getImage();
		earthImage = earthImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JButton earth = new JButton(new ImageIcon(earthImage));
		earth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(count==0) {
					player1 = "earth";
					earth1.setVisible(true);
					count++;
				}
				else {
					if(player1.equals("earth")) return;
					player2 = "earth";
					earth2.setVisible(true);
					startGame();
				}
			}
		});
		earth.setBounds(392, 377, 80, 80);
		contentPane.add(earth);
		
		ImageIcon airIcon = new ImageIcon("/Users/yeahn/Desktop/2020-Summer/Ukmok/image/air_choose.png");
		Image airImage = airIcon.getImage();
		airImage = airImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		JButton air = new JButton(new ImageIcon(airImage));
		air.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(count==0) {
					player1 = "air";
					air1.setVisible(true);
					count++;
				}
				else {
					if(player1.equals("air")) return;
					player2 = "air";
					air2.setVisible(true);
					startGame();
				}
			}
		});
		air.setBounds(528, 377, 80, 80);
		contentPane.add(air);
		
		water1.setHorizontalAlignment(SwingConstants.CENTER);
		water1.setBounds(120, 463, 80, 40);
		water1.setForeground(Color.white);
		water1.setFont(new Font("Chalkboard", Font.BOLD, 20));
		water1.setVisible(false);
		contentPane.add(water1);
		
		water2.setHorizontalAlignment(SwingConstants.CENTER);
		water2.setForeground(Color.WHITE);
		water2.setFont(new Font("Chalkboard", Font.BOLD, 20));
		water2.setBounds(120, 463, 80, 40);
		water2.setVisible(false);
		contentPane.add(water2);
		
		fire1.setHorizontalAlignment(SwingConstants.CENTER);
		fire1.setBounds(256, 463, 80, 40);
		fire1.setForeground(Color.white);
		fire1.setFont(new Font("Chalkboard", Font.BOLD, 20));
		fire1.setVisible(false);
		contentPane.add(fire1);
		
		fire2.setHorizontalAlignment(SwingConstants.CENTER);
		fire2.setForeground(Color.WHITE);
		fire2.setFont(new Font("Chalkboard", Font.BOLD, 20));
		fire2.setBounds(256, 463, 80, 40);
		fire2.setVisible(false);
		contentPane.add(fire2);
		
		earth1.setHorizontalAlignment(SwingConstants.CENTER);
		earth1.setBounds(392, 463, 80, 40);
		earth1.setForeground(Color.white);
		earth1.setFont(new Font("Chalkboard", Font.BOLD, 20));
		earth1.setVisible(false);
		contentPane.add(earth1);
		
		earth2.setHorizontalAlignment(SwingConstants.CENTER);
		earth2.setForeground(Color.WHITE);
		earth2.setFont(new Font("Chalkboard", Font.BOLD, 20));
		earth2.setBounds(392, 463, 80, 40);
		earth2.setVisible(false);
		contentPane.add(earth2);
		
		air1.setHorizontalAlignment(SwingConstants.CENTER);
		air1.setBounds(528, 463, 80, 40);
		air1.setForeground(Color.white);
		air1.setFont(new Font("Chalkboard", Font.BOLD, 20));
		air1.setVisible(false);
		contentPane.add(air1);
		
		air2.setHorizontalAlignment(SwingConstants.CENTER);
		air2.setForeground(Color.WHITE);
		air2.setFont(new Font("Chalkboard", Font.BOLD, 20));
		air2.setBounds(528, 463, 80, 40);
		air2.setVisible(false);
		contentPane.add(air2);
		
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon("/Users/yeahn/Desktop/2020-Summer/Ukmok/image/avater_mountain.jpg"));
		background.setBounds(0, 0, 728, 515);
		contentPane.add(background);
		
	}
	
	int c=0;
	public void startGame() {
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(c==1) {
					timer.cancel();
					st.dispose();
					Board.startBoard();
				}
				c++;
			}
		};
		timer.schedule(task, 0, 500);
	}
}
