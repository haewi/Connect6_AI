package edu.handong.java.round5;

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
		
		if(turn != Board.DEFAULT) {
			if(e.getX()<20 || e.getX()>780 || e.getY()<20 || e.getY()>780) return;
			mouseX = e.getX();
			mouseY = e.getY();
			
			// 새로운 돌 정보 만들기
			Stone s = new Stone();
			s.locate = getNearPoint(); // 가장 가까운 점 찾기
			if(stones.get((s.locate.x-40)/40).get((s.locate.y-40)/40).color != null) return; // 이미 돌이 존재하는 위치이면 무시
			// 색깔 지정
			if(turn == Board.BLACK) {
				s.color = Color.black;
			}
			else if(turn == Board.WHITE) {
				s.color = Color.white;
			}
			else if(turn == Board.RED) {
				s.color = Color.red;
			}
			mainBoard.count++;
//			System.out.println(mainBoard.count);
			
			if(mainBoard.count > 5) {
				if(mainBoard.count == 6) {
					mainBoard.turn = Board.BLACK;
					mainBoard.player.setText("Black");
				}
				else if(mainBoard.count%4 == 3) {
					mainBoard.turn = Board.WHITE;
					mainBoard.player.setText("White");
				}
				else if(mainBoard.count%4 == 1) {
					mainBoard.turn = Board.BLACK;
					mainBoard.player.setText("Black");
					}
				// timer 실행하기
				if(mainBoard.count > 5) {
					mainBoard.startTime();
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
			if(check == Board.BLACK) {
				mainBoard.winnerMessage("Black");
			}
			else if(check == Board.WHITE) {
				mainBoard.winnerMessage("White");
			}
			
			this.repaint();
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
		
		if(count >= 6) {
			if(s.color == Color.black) return Board.BLACK;
			if(s.color == Color.white) return Board.WHITE;
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
		
		if(count >= 6) {
			if(s.color == Color.black) return Board.BLACK;
			if(s.color == Color.white) return Board.WHITE;
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
		
		if(count >= 6) {
			if(s.color == Color.black) return Board.BLACK;
			if(s.color == Color.white) return Board.WHITE;
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
				
				if(count >= 6) {
					if(s.color == Color.black) return Board.BLACK;
					if(s.color == Color.white) return Board.WHITE;
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





