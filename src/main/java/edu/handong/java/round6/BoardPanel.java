package edu.handong.java.round6;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoardPanel extends JPanel implements MouseListener {

	Board mainBoard = null;
	int turn = Board.DEFAULT;
	int mouseX, mouseY;
	ArrayList<ArrayList<Stone>> stones = new ArrayList<ArrayList<Stone>>();
	JLabel win = new JLabel("");
	
	// 효과음
	AudioInputStream stream, stream2;
	Clip clip, clip2;
	
	public BoardPanel(Board b) {
		mainBoard = b;
		for(int i=0; i<19; i++) {
			stones.add(new ArrayList<Stone>());
			for(int j=0; j<19; j++) {
				stones.get(i).add(new Stone());
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		turn = mainBoard.turn;
		
		Graphics2D g2 = (Graphics2D) g;
		
		// 바둑돌 그려주기
		for(ArrayList<Stone> a : stones) {
			for(Stone stone : a) {
				if(stone.color != null) {
					g2.setColor(stone.color);
					g2.fillOval(stone.locate.x-15, stone.locate.y-15, 30, 30);
				}
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		turn = mainBoard.turn;
		
		// 컴퓨터 차례이면 무시
		if(turn == Board.COM) {
			return;
		}
		
		if(turn != Board.DEFAULT) {
			if(e.getX()<20 || e.getX()>780 || e.getY()<20 || e.getY()>780) return;
			mouseX = e.getX();
			mouseY = e.getY();
			
			// 새로운 돌 정보 만들기
			Stone s = new Stone();
			
			// 색깔 지정
			if(turn == Board.USER) {
				s.color = mainBoard.user;
			}
			else if(turn == Board.RED) {
				s.color = Color.red;
			}
			
			s.locate = getNearPoint(); // 가장 가까운 점 찾기
			if(stones.get((s.locate.x-40)/40).get((s.locate.y-40)/40).color != null) return; // 이미 돌이 존재하는 위치이면 무시
			
			// 순서 바꾸어주기
			if(mainBoard.turn != Board.RED) {
				mainBoard.count++;
				
				if(mainBoard.count%4 == 1 || mainBoard.count%4 == 3) {
					mainBoard.turn = Board.COM;
					mainBoard.player.setText("Computer");
					mainBoard.startTime();
					computerTurn();
				}
			}
			
			// 효과음 파일 열고 실행
			try {
				stream = AudioSystem.getAudioInputStream(new File("soundTrack/water_drop.wav"));
				clip = AudioSystem.getClip();
				clip.open(stream);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			clip.start();
			
			// 바둑돌 저장
			stones.get((s.locate.x-40)/40).set((s.locate.y-40)/40, s);
			int check = checkWinner(s);
			if(check == Board.COM) {
				mainBoard.winnerMessage("Black");
			}
			else if(check == Board.USER) {
				mainBoard.winnerMessage("White");
			}
			
			this.repaint();
		}
	}
	
	public void computerTurn() {
		System.out.println("computer Turn");
		
		// 돌 개수 추가
		mainBoard.count++;
		
		// User 차례가 되었으면 넘기기 아니면 다시 한번 계산하기
		if(mainBoard.count%4 == 1 || mainBoard.count%4 == 3) {
			mainBoard.turn = Board.USER;
			mainBoard.player.setText("USER");
			mainBoard.startTime();
		}
		else {
			computerTurn();
		}
	}
	
	public Point getNearPoint() {
		Point p = new Point(mouseX, mouseY);
		if(mouseX%40 < 20) {
			p.x -=mouseX%40;
		}
		else {
			p.x += 40-mouseX%40;
		}
		if(mouseY%40 < 20) {
			p.y -=mouseY%40;
		}
		else {
			p.y += 40-mouseY%40;
		}
		
		return p;
	}
	
	public void removeAll() {
		for(ArrayList<Stone> al: stones) {
			al.clear();
			for(int i=0; i<19; i++) {
				al.add(new Stone());
			}
		}
	}
	
	private int checkWinner(Stone s) {
		
		if(s.color.equals(Color.red)) return Board.DEFAULT;
		
		Point current = s.locate;
		
		// 가로 확인
		int count=1;
		for(int x=current.x+40; x<current.x+6*40; x+=40) {
//			System.out.println((x-40)/40 + " " + (current.y-40)/40);
			if((x-40)/40<0 || (x-40)/40 > 18) break;
			else if(stones.get((x-40)/40).get((current.y-40)/40).color==null) break;
			else if(s.color.equals(stones.get((x-40)/40).get((current.y-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
		for(int x=current.x-40; x>current.x-6*40; x-=40) {
//			System.out.println((x-40)/40 + " " + (current.y-40)/40);
			if((x-40)/40<0 || (x-40)/40 > 760) break;
			else if(stones.get((x-40)/40).get((current.y-40)/40).color==null) break;
			else if(s.color.equals(stones.get((x-40)/40).get((current.y-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
//		System.out.println(count);
		
		if(count == 6) {
			if(s.color.equals(mainBoard.computer)) return Board.COM;
			if(s.color.equals(mainBoard.user)) return Board.USER;
		}
		
		//세로 확인
		count=1;
		for(int y=current.y+40; y<current.y+6*40; y+=40) {
//			System.out.println((current.x-40)/40 + " " + (y-40)/40);
			if((y-40)/40<0 || (y-40)/40 > 18) break;
			else if(stones.get((current.x-40)/40).get((y-40)/40).color==null) break;
			else if(s.color.equals(stones.get((current.x-40)/40).get((y-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
		for(int y=current.y-40; y>current.y-6*40; y-=40) {
//			System.out.println((current.x-40)/40 + " " + (y-40)/40);
			if((y-40)/40<0 || (y-40)/40 > 18) break;
			else if(stones.get((current.x-40)/40).get((y-40)/40).color==null) break;
			else if(s.color.equals(stones.get((current.x-40)/40).get((y-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
//		System.out.println(count);
		
		if(count == 6) {
			if(s.color.equals(mainBoard.computer)) return Board.COM;
			if(s.color.equals(mainBoard.user)) return Board.USER;
		}
		
		// 대각선 확인
		count=1;
		for(int x=current.x, y=current.y, t=40; t<6*40; t+=40) {
			if((x+t-40)/40<0 || (x+t-40)/40 > 18 || (y+t-40)/40<0 || (y+t-40)/40 > 18) break;
			else if(stones.get((x+t-40)/40).get((y+t-40)/40).color==null) break;
			else if(s.color.equals(stones.get((x+t-40)/40).get((y+t-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
		for(int x=current.x, y=current.y, t=40; t<6*40; t+=40) {
			if((x-t-40)/40<0 || (x-t-40)/40 > 18 || (y-t-40)/40<0 || (y-t-40)/40 > 18) break;
			else if(stones.get((x-t-40)/40).get((y-t-40)/40).color==null) break;
			else if(s.color.equals(stones.get((x-t-40)/40).get((y-t-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
//		System.out.println(count);
		
		if(count == 6) {
			if(s.color.equals(mainBoard.computer)) return Board.COM;
			if(s.color.equals(mainBoard.user)) return Board.USER;
		}
		
		// 반대각선 확인
		count=1;
		for(int x=current.x, y=current.y, t=40; t<6*40; t+=40) {
			if((x+t-40)/40<0 || (x+t-40)/40 > 18 || (y-t-40)/40<0 || (y-t-40)/40 > 18) break;
			else if(stones.get((x+t-40)/40).get((y-t-40)/40).color==null) break;
			else if(s.color.equals(stones.get((x+t-40)/40).get((y-t-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
		for(int x=current.x, y=current.y, t=40; t<6*40; t+=40) {
			if((x-t-40)/40<0 || (x-t-40)/40 > 18 || (y+t-40)/40<0 || (y+t-40)/40 > 18) break;
			else if(stones.get((x-t-40)/40).get((y+t-40)/40).color==null) break;
			else if(s.color.equals(stones.get((x-t-40)/40).get((y+t-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
//				System.out.println(count);

		if(count == 6) {
			if(s.color.equals(mainBoard.computer)) return Board.COM;
			if(s.color.equals(mainBoard.user)) return Board.USER;
		}
		
		return Board.DEFAULT;
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}





