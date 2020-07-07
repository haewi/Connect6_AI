package edu.handong.java.round6;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class BoardLine extends JPanel{

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setStroke(new BasicStroke(3));
		g2.drawRect(40, 40, 720, 720);

		// 바둑판 줄 위치 (40~760까지 40 pixel씩) 
		g2.setStroke(new BasicStroke(1));
		for(int i=80; i<730; i+=40) {
			g2.drawLine(i, 40, i, 760);
			g2.drawLine(40, i, 760, i);
		}

		// 바둑판의 작은 원
		for(int i=160; i<730; i+=240) {
			for(int j=160; j<730; j+=240) {
				g2.fillOval(i-4, j-4, 8, 8);
			}
		}
		
	}

}