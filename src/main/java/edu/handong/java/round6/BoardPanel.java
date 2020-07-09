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
	
	public static final int CLEAR = 0;
	
	Board mainBoard = null;
	int turn = Board.DEFAULT;
	int mouseX, mouseY;
	ArrayList<ArrayList<Stone>> stones = new ArrayList<ArrayList<Stone>>();
	int[][] st = new int[19][19];
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
			int color = Board.DEFAULT;
			
			// 색깔 지정
			if(turn == Board.USER) {
				s.color = mainBoard.user;
				color = Board.USER;
			}
			else if(turn == Board.RED) {
				s.color = Color.red;
				color = Board.RED;
			}
			
			s.locate = getNearPoint(); // 가장 가까운 점 찾기
			if(stones.get((s.locate.x-40)/40).get((s.locate.y-40)/40).color != null) return; // 이미 돌이 존재하는 위치이면 무시
			
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
			st[(s.locate.x-40)/40][(s.locate.y-40)/40] = color;
			
			// 순서 바꾸어주기
			if(mainBoard.turn != Board.RED) {
				mainBoard.count++;
				
				if(mainBoard.count%4 == 1 || mainBoard.count%4 == 3) {
					mainBoard.turn = Board.COM;
					mainBoard.player.setText("Computer");
					computerTurn();
				}
			}
			
			// 승부 판단
			int check = checkWinner(s);
			if(check == Board.COM) {
				mainBoard.winnerMessage("Computer");
			}
			else if(check == Board.USER) {
				mainBoard.winnerMessage("User");
			}
			
			this.repaint();
		}
	}
	
	public void computerTurn() {
		// 돌 생성
		Stone s = new Stone();
		s.color = mainBoard.computer;
		
		// 돌 위치 선택
		Point p = null;
		
		// 방어
		double[][] user = getAllWeight(mainBoard.user);
		for(int x=0; x<19; x++) {
			for(int y=0; y<19; y++) {
//				System.out.print(user[y][x] + "  ");
				if(user[x][y] > 100000) {
					p = new Point(x, y);
				}
			}
//			System.out.println();
		}
//		System.out.println();
		
		// 방어할게 없으면 공격
		if(p==null) {
			double[][] com = getAllWeight(mainBoard.computer);
			double max = Integer.MIN_VALUE;
			for(int x=0; x<19; x++) {
				for(int y=0; y<19; y++) {
//					System.out.print(com[y][x] + "  ");
					if(com[x][y] > max) {
						p = new Point(x, y);
						max = com[x][y];
					}
				}
//				System.out.println();
			}
//			System.out.println();
		}
		
		s.locate = new Point(40+p.x*40, 40+p.y*40);
		
		// 돌 저장
		stones.get(p.x).set(p.y, s);
		st[p.x][p.y] = Board.COM;
		
		int check = checkWinner(s);
		if(check == Board.COM) {
			mainBoard.winnerMessage("Computer");
			return;
		}
		else if(check == Board.USER) {
			mainBoard.winnerMessage("User");
			return;
		}
		
		this.repaint();
		
		// 돌 개수 추가
		mainBoard.count++;
		
		// User 차례가 되었으면 넘기기
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
		int count=checkHorizontal(s, current);
		
		if(count == 6) {
			if(s.color.equals(mainBoard.computer)) return Board.COM;
			if(s.color.equals(mainBoard.user)) return Board.USER;
		}
		
		// 세로 확인
		count = checkVertical(s, current);
		
		if(count == 6) {
			if(s.color.equals(mainBoard.computer)) return Board.COM;
			if(s.color.equals(mainBoard.user)) return Board.USER;
		}
		
		// 대각선 확인
		count = checkRightDiagonal(s, current);
		
		if(count == 6) {
			if(s.color.equals(mainBoard.computer)) return Board.COM;
			if(s.color.equals(mainBoard.user)) return Board.USER;
		}
		
		// 반대각선 확인
		count = checkLeftDiagonal(s, current);

		if(count == 6) {
			if(s.color.equals(mainBoard.computer)) return Board.COM;
			if(s.color.equals(mainBoard.user)) return Board.USER;
		}
		
		return Board.DEFAULT;
	}
	
	// 가중치 계산하기
	public double connect6ShapeScore(int consecutive, int openEnds, int currentTurn) { // shape에 따른 가중치 부여
		// 연속되었지만 막혀있으면 0
		if (openEnds == 0 && consecutive < 6)
			return 0;
		switch (consecutive) { // 연속된 돌의 개수
		case 5:
			switch (openEnds) { // 열린 공간 수
			case 1:
				if (currentTurn == this.turn) // 현재 검사하는자의 차례이면
					return 1000000;
				return 50;
			case 2:
				if (currentTurn == this.turn)
					return 1000000;
				return 500000;
			}
		case 4:
			switch (openEnds) {
			case 1:
				if (currentTurn == this.turn)
					return 1000000;
				return 50;
			case 2:
				if (currentTurn == this.turn)
					return 1000000;
				return 500000;
			}
		case 3:
			switch (openEnds) {
			case 1:
				if (currentTurn == this.turn)
					return 7;
				return 5;
			case 2:
				if (currentTurn == this.turn)
					return 10000;
				return 50;
			}
		case 2:
			switch (openEnds) {
			case 1:
				return 2;
			case 2:
				return 5;
			}
		case 1:
			switch (openEnds) {   
			case 1:
				return 0.5;
			case 2:
				return 1;
			}
		default:
			return 2000000;      // 우승!
		}
	}
	

	private double[][] getAllWeight(Color color) {
		double[][] all = new double[19][19];
		
		double[][] hor = analyzeHorizontal(color);
		double[][] hor2 = analyzeHorizontalReverse(color);
		double[][] ver = analyzeVertical(color);
		double[][] ver2 = analyzeVerticalReverse(color);
		double[][] left = analyzeLeftDiagonal(color);
		double[][] left2 = analyzeLeftDiagonalReverse(color);
		double[][] right = analyzeRightDiagonal(color);
		double[][] right2 = analyzeRightDiagonalReverse(color);
		
		for(int y=0; y<19; y++) {
			for(int x=0; x<19; x++) {
				all[x][y] = hor[x][y] + ver[x][y] + left[x][y] + right[x][y] + hor2[x][y] + ver2[x][y] + left2[x][y] + right2[x][y];
				if(st[x][y] != CLEAR) all[x][y] = Integer.MIN_VALUE;
//				System.out.print(all[x][y] + " ");
				
			}
//			System.out.println();
		}
//		System.out.println();
		
		return all;
	}
	
	// 가로 가중치 계산
	public double[][] analyzeHorizontal(Color color) {
		double score = Double.MIN_VALUE;
		int countConsecutive = 0;
		int openEnds = 0;

		int currentTurn;

		if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
		else if(color.equals(mainBoard.user)) currentTurn = Board.USER;
		else return null;

		double[][] hor = new double[19][19];
		
		for (int y = 0; y < 19; y++) {
			for (int x = 0; x < 19; x++) {
				
				if (st[x][y] == currentTurn) {// 돌이 검정색이 되면 연속점 1증가
					countConsecutive++;
					hor[x][y] = Integer.MIN_VALUE;
				}
				else if (st[x][y] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					hor[x][y] += score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[x][y] ==CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
					hor[x][y] = Integer.MIN_VALUE;
					countConsecutive = 0;
					openEnds = 0;
				}
				else openEnds = 0; // 빈 점이 벽에 만나서 끝났을 경우
			}
			if (countConsecutive > 0) { // 연속점이 벽에 만나서 끝났을 경우
				score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
			}
			countConsecutive = 0;
			openEnds = 0;
		}

		for (int y = 7; y < 12; y++) {
			for (int x = 7; x < 12; x++) {
				hor[x][y] += 0.5;
			}
		}
		
		return hor;
	}
	
	// 가로 가중치 계산 (반대 방향)
		public double[][] analyzeHorizontalReverse(Color color) {
			double score = Double.MIN_VALUE;
			int countConsecutive = 0;
			int openEnds = 0;

			int currentTurn;

			if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
			else if(color.equals(mainBoard.user)) currentTurn = Board.USER;
			else return null;

			double[][] hor = new double[19][19];
			
			for (int y = 0; y < 19; y++) {
				for (int x = 18; x >= 0 ; x--) {
					
					if (st[x][y] == currentTurn) {// 돌이 검정색이 되면 연속점 1증가
						countConsecutive++;
						hor[x][y] = Integer.MIN_VALUE;
					}
					else if (st[x][y] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
						openEnds++;
						score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
						hor[x][y] += score;
						countConsecutive = 0;
						openEnds = 1;
					}
					else if (st[x][y] ==CLEAR) // 빈 점이 그냥 등장할 경우
						openEnds = 1;
					else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
						hor[x][y] = Integer.MIN_VALUE;
						countConsecutive = 0;
						openEnds = 0;
					}
					else openEnds = 0; // 빈 점이 벽에 만나서 끝났을 경우
				}
				if (countConsecutive > 0) { // 연속점이 벽에 만나서 끝났을 경우
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
				}
				countConsecutive = 0;
				openEnds = 0;
			}
			
			return hor;
		}
	
	// 세로 가중치 계산
	public double[][] analyzeVertical(Color color) {
	    double score = Double.MIN_VALUE;
	    int countConsecutive = 0;
	    int openEnds = 0;
	    
	    int currentTurn;

	    if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
		else if(color.equals(mainBoard.user)) currentTurn = Board.USER;
		else return null;
	    
	    double[][] ver = new double[19][19];
	    
	    for (int x = 0; x < 19; x++) {
	    	for (int y = 0; y < 19; y++) {
	    		
	    		if (st[x][y] == currentTurn){ // 돌이 검정색이 되면 연속점 1증가
	    			countConsecutive++;
	    			ver[x][y] = Integer.MIN_VALUE;
	    		}
	    		else if (st[x][y] == CLEAR && countConsecutive > 0) {   // 연속점에서 열린 점으로 끝났을 경우
	    			openEnds++;
	    			score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
	    			ver[x][y] = score;
	    			countConsecutive = 0;
	    			openEnds = 1;
	    		}
	    		else if (st[x][y] ==CLEAR) // 빈 점이 그냥 등장할 경우
	    			openEnds = 1;
	    		else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
//	    			}
	    			ver[x][y] = Integer.MIN_VALUE;
	    			countConsecutive = 0;
	    			openEnds = 0;
	    		}
	    		else { // 빈 점이 벽에 만나서 끝났을 경우
	    			openEnds = 0;
	    		}
	    	}
	    	if (countConsecutive > 0) // 연속점이 벽에 만나서 끝났을 경우
	    		score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
	    	countConsecutive = 0;
	    	openEnds = 0;
	    }
	    return ver;
	}
	
	// 세로 가중치 계산 (반대 방향)
	public double[][] analyzeVerticalReverse(Color color) {
		double score = Double.MIN_VALUE;
		int countConsecutive = 0;
		int openEnds = 0;

		int currentTurn;

		if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
		else if(color.equals(mainBoard.user)) currentTurn = Board.USER;
		else return null;

		double[][] ver = new double[19][19];

		for (int x = 0; x < 19; x++) {
			for (int y = 18; y >= 0 ; y--) {
				
				if (st[x][y] == currentTurn){ // 돌이 검정색이 되면 연속점 1증가
					countConsecutive++;
					ver[x][y] = Integer.MIN_VALUE;
				}
				else if (st[x][y] == CLEAR && countConsecutive > 0) {   // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					ver[x][y] = score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[x][y] ==CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					ver[x][y] = Integer.MIN_VALUE;
					countConsecutive = 0;
					openEnds = 0;
				}
				else { // 빈 점이 벽에 만나서 끝났을 경우
					openEnds = 0;
				}
			}
			if (countConsecutive > 0) // 연속점이 벽에 만나서 끝났을 경우
				score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
			countConsecutive = 0;
			openEnds = 0;
		}
		return ver;
	}
	
	// 반대각선 가중치 계산
	public double[][] analyzeLeftDiagonal(Color color) {
		double score = 0;
		int countConsecutive = 0;
		int openEnds = 0;
		int n = 19;

		int currentTurn;

		if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
		else if(color.equals(mainBoard.user)) currentTurn = Board.USER;
		else return null;

		double[][] left = new double[19][19];

		for(int i = 0; i <= 2 * n - 2; i++) {
			int lb, ub;
			if(19 <= i) lb = - (2 * n - 2 - i);
			else lb = - i;
			ub = -lb;

			for(int diff = lb; diff <= ub; diff += 2) {
				int x = (i + diff) >> 1;
				int y = i - x;
				
				if (st[x][y] == currentTurn) { // 돌이 검정색이 되면 연속점 1증가
					countConsecutive++;
					left[x][y] = Integer.MIN_VALUE;
				}
				else if (st[x][y] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					left[x][y] = score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[x][y] == CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
					countConsecutive = 0;
					openEnds = 0;
					left[x][y] = Integer.MIN_VALUE;
				}
				else openEnds = 0;               
			}
			if (countConsecutive > 0) // 연속점이 벽에 만나서 끝났을 경우
				score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
			countConsecutive = 0;
			openEnds = 0;
		}
		return left;
	}

	// 반대각선 가중치 계산 (반대 방향)
	public double[][] analyzeLeftDiagonalReverse(Color color) {
		double score = 0;
		int countConsecutive = 0;
		int openEnds = 0;
		int n = 19;

		int currentTurn;

		if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
		else if(color.equals(mainBoard.user)) currentTurn = Board.USER;
		else return null;

		double[][] left = new double[19][19];

		for(int i = 0; i <= 2 * n - 2; i++) {
			int lb, ub;
			if(19 <= i) lb = - (2 * n - 2 - i);
			else lb = - i;
			ub = -lb;

			for(int diff = ub; diff >= lb; diff -= 2) {
				int x = (i + diff) >> 1;
				int y = i - x;
				
				if (st[x][y] == currentTurn) { // 돌이 검정색이 되면 연속점 1증가
					countConsecutive++;
					left[x][y] = Integer.MIN_VALUE;
				}
				else if (st[x][y] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					left[x][y] = score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[x][y] == CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
					countConsecutive = 0;
					openEnds = 0;
					left[x][y] = Integer.MIN_VALUE;
				}
				else openEnds = 0;               
			}
			if (countConsecutive > 0) // 연속점이 벽에 만나서 끝났을 경우
				score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
			countConsecutive = 0;
			openEnds = 0;
		}
		return left;
	}
	
	// 대각선 가중치 계산
	public double[][] analyzeRightDiagonal(Color color) {
		double score = 0;
		int countConsecutive = 0;
		int openEnds = 0;
		int n = 19;

		int currentTurn;

		if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
		else if(color.equals(mainBoard.user)) currentTurn = Board.USER;
		else return null;

		double[][] right = new double[19][19];

		for(int i = 0; i <= 2 * n - 2; i++) {
			int lb, ub;
			if(19 <= i) lb = - (2 * n - 2 - i);
			else lb = - i;
			ub = -lb;

			for(int diff = lb; diff <= ub; diff += 2) {
				int x = (i + diff) >> 1;
				int y = 18 - i + x;
				
				if (st[y][x] == currentTurn) { // 돌이 검정색이 되면 연속점 1증가
					countConsecutive++;
					right[y][x] = Integer.MIN_VALUE;
				}
				else if (st[y][x] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					right[y][x] = score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[y][x] == CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
					countConsecutive = 0;
					openEnds = 0;
					right[y][x] = Integer.MIN_VALUE;
				}
				else openEnds = 0;               
			}
			if (countConsecutive > 0) // 연속점이 벽에 만나서 끝났을 경우
				score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
			countConsecutive = 0;
			openEnds = 0;
		}
		return right;
	  }
	
	// 대각선 가중치 계산 (반대 방향)
	public double[][] analyzeRightDiagonalReverse(Color color) {
		double score = 0;
		int countConsecutive = 0;
		int openEnds = 0;
		int n = 19;

		int currentTurn;

		if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
		else if(color.equals(mainBoard.user)) currentTurn = Board.USER;
		else return null;

		double[][] right = new double[19][19];

		for(int i = 0; i <= 2 * n - 2; i++) {
			int lb, ub;
			if(19 <= i) lb = - (2 * n - 2 - i);
			else lb = - i;
			ub = -lb;

			for(int diff = ub; diff >= lb; diff -= 2) {
				int x = (i + diff) >> 1;
				int y = 18 - i + x;
				
				if (st[y][x] == currentTurn) { // 돌이 검정색이 되면 연속점 1증가
					countConsecutive++;
					right[y][x] = Integer.MIN_VALUE;
				}
				else if (st[y][x] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					right[y][x] = score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[y][x] == CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
					countConsecutive = 0;
					openEnds = 0;
					right[y][x] = Integer.MIN_VALUE;
				}
				else openEnds = 0;               
			}
			if (countConsecutive > 0) // 연속점이 벽에 만나서 끝났을 경우
				score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
			countConsecutive = 0;
			openEnds = 0;
		}
		return right;
	}
	
	// 가로 확인
	private int checkHorizontal(Stone s, Point current) {
		int count=1;
		for(int x=current.x+40; x<current.x+6*40; x+=40) {
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
		return count;
	}
	
	//세로 확인
	private int checkVertical(Stone s, Point current) {
		int count=1;
		for(int y=current.y+40; y<current.y+6*40; y+=40) {
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
			if((y-40)/40<0 || (y-40)/40 > 18) break;
			else if(stones.get((current.x-40)/40).get((y-40)/40).color==null) break;
			else if(s.color.equals(stones.get((current.x-40)/40).get((y-40)/40).color)) {
				count++;
			}
			else {
				break;
			}
		}
//				System.out.println(count);
		return count;
	}
	
	// 대각선 확인
	private int checkRightDiagonal(Stone s, Point current) {
		int count=1;
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
		return count;
	}
	
	// 반대각선 확인
	private int checkLeftDiagonal(Stone s, Point current) {
		int count=1;
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
		return count;
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





