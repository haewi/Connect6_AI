package edu.handong.java.round6;

import java.awt.Color;
import java.awt.Point;

public class Util {

	public static final int CLEAR=-1;
	
	Board mainBoard;
	
	public Util(Board b) {
		mainBoard = b;
	}

	Color computer, user;

	// 현재위치, 더 들어갈 깊이, alpha, beta, 최대값을 선택할지 말지, 가중치 측정 기준 색
	public Node minimax(Node position, int depth, double alpha, double beta, boolean maximizingPlayer, Color color) {
		if(depth == 0 || gameover()) {
			// 자신의 최대 가중치 및 그 위치(Node) 돌리기
			position = getBiggestWeight(position, color);
			
			System.out.println("depth=0 x, y: " + position.p.x + " " + position.p.y);
			for(int x=0; x<19; x++) {
				for(int y=0; y<19; y++) {
					System.out.print(position.st[y][x] + "  ");
				}
				System.out.println();
			}
			System.out.println();
			
			return position;
		}
		
		int currentTurn;
		if(color.equals(mainBoard.computer)) currentTurn = Board.COM;
		else currentTurn = Board.USER;
		
		// 자식 만들기 (가중치에 맞추어서) -> maximizingPlayer가 사실이면 mainBoard.computer에 맞추어서 아님 mainBoard.user에 맞추어서
		// 자신 link에 넣기
		makeChild(position, color, currentTurn);

		double maxEval = Integer.MIN_VALUE;
		double minEval = Integer.MAX_VALUE;

		if(maximizingPlayer) {
			Node tmp = null;
			for(int i=0; i<position.link.size(); i++) {
				tmp = minimax(position.link.get(i), depth-1, alpha, beta, false, color);
				double eval = tmp.score;
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if(beta <= alpha) {
					break;
				}
			}
			position.p = tmp.p;
			position.score = maxEval;
			
			System.out.println("true");
			for(int x=0; x<19; x++) {
				for(int y=0; y<19; y++) {
					System.out.print(position.st[y][x] + "  ");
				}
				System.out.println();
			}
			System.out.println();
			return position;
		}
		else {
			Node tmp = null;
			for(int i=0; i<position.link.size(); i++) {
				tmp = minimax(position.link.get(i), depth-1, alpha, beta, true, color);
				double eval = tmp.score;
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if(beta <= alpha) {
					break;
				}
			}
			position.p = tmp.p;
			position.score = minEval;

			System.out.println("false");
			for(int x=0; x<19; x++) {
				for(int y=0; y<19; y++) {
					System.out.print(position.st[y][x] + "  ");
				}
				System.out.println();
			}
			System.out.println();
			return position;
		}
	}

//	public static Node minimax2(Node position, double depth, double alpha, double beta, boolean maximizingPlayer) {
//		// 자신의 최대 가중치 계산
//		if(depth == 0 || gameover()) {
//			// 자신의 최대 가중치 및 그 위치(Node) 돌리기
//			return position;
//		}
//
//		double maxEval = Integer.MIN_VALUE;
//		double minEval = Integer.MAX_VALUE;
//
//		if(maximizingPlayer) {
//			for(int i=0; i<position.link.size(); i++) {
//				int eval = minimax1(position.link.get(i), depth-1, alpha, beta, !maximizingPlayer).score;
//				maxEval = Math.max(maxEval, eval);
//				alpha = Math.max(alpha, eval);
//				if(beta <= alpha) {
//					break;
//				}
//			}
//			//			System.out.println(maxEval);
//			return maxEval;
//		}
//		else {
//			for(int i=0; i<position.link.size(); i++) {
//				int eval = minimax1(position.link.get(i), depth-1, alpha, beta, !maximizingPlayer).score;
//				minEval = Math.min(minEval, eval);
//				beta = Math.min(beta, eval);
//				if(beta <= alpha) {
//					break;
//				}
//			}
//			//			System.out.println(minEval);
//			return minEval;
//		}
//	}


	// 주어진 Node 안의 배열로 Color에 따른 가중치를 계산하여 가장 큰 값을 리턴
	public Node getBiggestWeight(Node position, Color c) {
		
		double[][] st = getAllWeight(position.st, c);
		
		double max = Integer.MIN_VALUE;
		
		for(int y=0; y<19; y++) {
			for(int x=0; x<19; x++) {
				if(st[x][y]>max) {
					max = st[x][y];
					position.p = new Point(x, y);
				}
			}
		}
		
		position.score = max;
		return position;
	}
	
	// 만들 child의 부모와 만들 때 중심으로 둘 색
	private void makeChild(Node parent, Color c, int currentTurn) {
		
		// 가중치 구하기
		double[][] st = getAllWeight(parent.st, c);
		
		// 가장 가중치 값이 큰 두 위치 구하기
		double max1 = Integer.MIN_VALUE; // 가장 큰 값
		double max2 = Integer.MIN_VALUE; // 두번째로 큰 값
		Point p1 = null; // 가장 큰 값 좌표
		Point p2 = null; // 두번째로 큰 값 좌표
		
		for(int y=0; y<19; y++) {
			for(int x=0; x<19; x++) {
				if(max1 < st[x][y]) { // max1보다 크면
					max2 = max1;
					p2 = p1;
					max1 = st[x][y];
					p1 = new Point(x, y);
				}
				else if(max2 < st[x][y]) { // max1 > parent.st[x][y] > max2
					max2 = st[x][y];
					p2 = new Point(x, y);
				}
			}
		}
		
		Node child1 = new Node();
		child1.st = parent.st.clone();
		child1.st[p1.x][p1.y] = currentTurn;
		
		Node child2 = new Node();
		child2.st = parent.st.clone();
		child2.st[p2.x][p2.y] = currentTurn;
		
		parent.link.add(child1);
		parent.link.add(child2);
	}
	
	private boolean gameover() {
		return false;
	}

	private double[][] getAllWeight(int[][] s, Color color) {
		double[][] all = new double[19][19];
//		weight = new double[19][19];

		double[][] hor = analyzeHorizontal(s, color);
		double[][] hor2 = analyzeHorizontalReverse(s, color);
		double[][] ver = analyzeVertical(s, color);
		double[][] ver2 = analyzeVerticalReverse(s, color);
		double[][] left = analyzeLeftDiagonal(s, color);
		double[][] left2 = analyzeLeftDiagonalReverse(s, color);
		double[][] right = analyzeRightDiagonal(s, color);
		double[][] right2 = analyzeRightDiagonalReverse(s, color);

		//		if(color == mainBoard.computer) System.out.println("computer");
		//		else System.out.println("user");
		for(int y=0; y<19; y++) {
			for(int x=0; x<19; x++) {
				all[x][y] = hor[x][y] + ver[x][y] + left[x][y] + right[x][y] + hor2[x][y] + ver2[x][y] + left2[x][y] + right2[x][y];
//				if(st[x][y] != CLEAR) {
//					all[x][y] = Integer.MIN_VALUE;
////					weight[x][y] = Integer.MIN_VALUE;//Integer.MIN_VALUE;
//				}
//				System.out.print(all[x][y] + " ");
//				System.out.print((int) weight[x][y] + "  ");
			}
			//			System.out.println();
		}
		//		System.out.println();

		return all;
	}

	// 가로 가중치 계산
	public double[][] analyzeHorizontal(int[][] st, Color color) {
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
				if (st[x][y] == currentTurn) { // 돌이 검정색이고 되면 연속점 1증가
					countConsecutive++;
				}
				else if (st[x][y] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					hor[x][y] = score;
					//						weight[x][y] += score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[x][y] ==CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
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
				//					weight[x][y] += 0.5;
			}
		}

		return hor;
	}

	// 가로 가중치 계산 (반대 방향)
	public double[][] analyzeHorizontalReverse(int[][] st, Color color) {
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
				}
				else if (st[x][y] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					hor[x][y] += score;
					//							weight[x][y] += score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[x][y] ==CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
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
	public double[][] analyzeVertical(int[][] st, Color color) {
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
				}
				else if (st[x][y] == CLEAR && countConsecutive > 0) {   // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					ver[x][y] = score;
					//		    			weight[x][y] += score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[x][y] ==CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
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
	public double[][] analyzeVerticalReverse(int[][] st, Color color) {
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
				}
				else if (st[x][y] == CLEAR && countConsecutive > 0) {   // 연속점에서 열린 점으로 끝났을 경우
					openEnds++;
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
					ver[x][y] = score;
					//						weight[x][y] += score;
					countConsecutive = 0;
					openEnds = 1;
				}
				else if (st[x][y] ==CLEAR) // 빈 점이 그냥 등장할 경우
					openEnds = 1;
				else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
					score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
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
	public double[][] analyzeLeftDiagonal(int[][] st, Color color) {
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
			}
			else if (st[x][y] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
				openEnds++;
				score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
				left[x][y] = score;
				//						weight[x][y] += score;
				countConsecutive = 0;
				openEnds = 1;
			}
			else if (st[x][y] == CLEAR) // 빈 점이 그냥 등장할 경우
				openEnds = 1;
			else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
				countConsecutive = 0;
				openEnds = 0;
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
	public double[][] analyzeLeftDiagonalReverse(int[][] st, Color color) {
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
		}
		else if (st[x][y] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
			openEnds++;
			score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
			left[x][y] = score;
			//						weight[x][y] += score;
			countConsecutive = 0;
			openEnds = 1;
		}
		else if (st[x][y] == CLEAR) // 빈 점이 그냥 등장할 경우
			openEnds = 1;
		else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
			countConsecutive = 0;
			openEnds = 0;
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
	public double[][] analyzeRightDiagonal(int[][] st, Color color) {
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
			}
			else if (st[y][x] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
				openEnds++;
				score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
				right[y][x] = score;
				//						weight[y][x] += score;
				countConsecutive = 0;
				openEnds = 1;
			}
			else if (st[y][x] == CLEAR) // 빈 점이 그냥 등장할 경우
				openEnds = 1;
			else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
				countConsecutive = 0;
				openEnds = 0;
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
	public double[][] analyzeRightDiagonalReverse(int[][] st, Color color) {
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
		}
		else if (st[y][x] == CLEAR && countConsecutive > 0) { // 연속점에서 열린 점으로 끝났을 경우
			openEnds++;
			score = connect6ShapeScore(countConsecutive, openEnds, currentTurn);
			right[y][x] = score;
			//						weight[y][x] += score;
			countConsecutive = 0;
			openEnds = 1;
		}
		else if (st[y][x] == CLEAR) // 빈 점이 그냥 등장할 경우
			openEnds = 1;
		else if (countConsecutive > 0) { // 연속점이 다른 돌에 만나서 끝났을 경우
			countConsecutive = 0;
			openEnds = 0;
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

	// 가중치 계산하기
	public double connect6ShapeScore(int consecutive, int openEnds, int currentTurn) { // shape에 따른 가중치 부여
		int turn = mainBoard.turn;
		
		// 연속되었지만 막혀있으면 0
		if (openEnds == 0 && consecutive < 6)
			return 0;
		switch (consecutive) { // 연속된 돌의 개수
		case 5:
			switch (openEnds) { // 열린 공간 수
			case 1:
				if (currentTurn == turn) // 현재 검사하는자의 차례이면
					return 1000000;
				return 50;
			case 2:
				if (currentTurn == turn)
					return 1000000;
				return 500000;
			}
		case 4:
			switch (openEnds) {
			case 1:
				if (currentTurn == turn)
					return 1000000;
				return 50;
			case 2:
				if (currentTurn == turn)
					return 1000000;
				return 500000;
			}
		case 3:
			switch (openEnds) {
			case 1:
				if (currentTurn == turn)
					return 7;
				return 5;
			case 2:
				if (currentTurn == turn)
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
}
