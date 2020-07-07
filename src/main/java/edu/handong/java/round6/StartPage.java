package edu.handong.java.round6;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartPage extends JFrame {

	StartPage st = this;
	
	private JPanel contentPane;
	

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 434, 257);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel chooseLabel = new JLabel("Choose your color");
		chooseLabel.setFont(new Font("Palatino", Font.PLAIN, 30));
		chooseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		chooseLabel.setBounds(74, 38, 288, 74);
		contentPane.add(chooseLabel);
		
		JButton black = new JButton("Black");
		black.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				st.dispose();
				Board.startBoard(Color.white, Color.black);
			}
		});
		black.setFont(new Font("Lao MN", Font.PLAIN, 20));
		black.setBounds(83, 142, 117, 50);
		contentPane.add(black);
		
		JButton white = new JButton("White");
		white.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				st.dispose();
				Board.startBoard(Color.black, Color.white);
			}
		});
		white.setFont(new Font("Lao MN", Font.PLAIN, 20));
		white.setBounds(245, 142, 117, 50);
		contentPane.add(white);
		
		
		
	}
}
