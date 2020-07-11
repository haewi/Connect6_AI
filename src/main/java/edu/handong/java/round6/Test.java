package edu.handong.java.round6;

import javax.swing.JFrame;

public class Test {
	
	JFrame mainFrame = new JFrame();
	
	public static void main(String[] args) {
		new Test();
	}
	
//	int [] t = new int[] {-1, 3, 5,  1, -6, -4, 0, 9};
//	int[][][] test = new int[][][] {{{-1, 3}, {5, 1}}, {{-6, -4}, {0, 9}}}; 
	
	public Test() {
		Num num = makeTree2();
		
		
		System.out.println(minimax1(num, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
	}
	
	private Num makeTree1() {
		Num num = new Num(); // 1열
		
		num.link.add(new Num()); // 3, -4
		num.link.add(new Num());
		
		Num tmp1 = num.link.get(0); // 3, 5
		tmp1.link.add(new Num());
		tmp1.link.add(new Num());
		
		Num tmp2 = tmp1.link.get(0);
		tmp2.link.add(new Num(-1));
		tmp2.link.add(new Num(3));
		
		tmp2 = tmp1.link.get(1);
		tmp2.link.add(new Num(5));
		tmp2.link.add(new Num(1));
		
		tmp1 = num.link.get(1); // -4, 9
		tmp1.link.add(new Num());
		tmp1.link.add(new Num());
		
		tmp2 = tmp1.link.get(0);
		tmp2.link.add(new Num(-6));
		tmp2.link.add(new Num(-4));
		
		tmp2 = tmp1.link.get(1);
		tmp2.link.add(new Num(0));
		tmp2.link.add(new Num(9));
		
		return num;
	}
	
	private Num makeTree2() {
		Num num = new Num(); // 1열
		
		num.link.add(new Num());
		num.link.add(new Num());
		
		Num tmp1 = num.link.get(0);
		tmp1.link.add(new Num());
		tmp1.link.add(new Num());
		
		Num tmp2 = tmp1.link.get(0);
		tmp2.link.add(new Num());
		tmp2.link.add(new Num());
		
		Num tmp3 = tmp2.link.get(0);
		tmp3.link.add(new Num(8));
		tmp3.link.add(new Num(5));
		
		tmp3 = tmp2.link.get(1);
		tmp3.link.add(new Num(6));
		tmp3.link.add(new Num(-4));
		
		tmp2 = tmp1.link.get(1);
		tmp2.link.add(new Num());
		tmp2.link.add(new Num());
		
		tmp3 = tmp2.link.get(0);
		tmp3.link.add(new Num(3));
		tmp3.link.add(new Num(8));
		
		tmp3 = tmp2.link.get(1);
		tmp3.link.add(new Num(4));
		tmp3.link.add(new Num(-6));
		
		//-----------tmp1 get2
		
		tmp1 = num.link.get(1);
		tmp1.link.add(new Num());
		tmp1.link.add(new Num());
		
		tmp2 = tmp1.link.get(0);
		tmp2.link.add(new Num());
		tmp2.link.add(new Num());
		
		tmp3 = tmp2.link.get(0);
		tmp3.link.add(new Num(1));
		tmp3.link.add(new Num(3));
		
		tmp3 = tmp2.link.get(1);
		tmp3.link.add(new Num(5));
		tmp3.link.add(new Num(2));
		
		tmp2 = tmp1.link.get(1);
		tmp2.link.add(new Num());
		tmp2.link.add(new Num());
		
		tmp3 = tmp2.link.get(0);
		tmp3.link.add(new Num(0));
		tmp3.link.add(new Num(-3));
		
		tmp3 = tmp2.link.get(1);
		tmp3.link.add(new Num(3));
		tmp3.link.add(new Num(6));
		
		return num;
	}

	//minmax
//	private int minimax1(Num position, int depth, boolean maximizingPlayer) {
//		if(depth == 0 || gameover()) return position.num;
//		
//		int maxEval = Integer.MIN_VALUE;
//		int minEval = Integer.MAX_VALUE;
//		
//		if(maximizingPlayer) {
//			for(int i=0; i<position.link.size(); i++) {
//				int eval = minimax1(position.link.get(i), depth-1, false);
//				maxEval = Math.max(maxEval, eval);
//			}
////			System.out.println(maxEval);
//			return maxEval;
//		}
//		else {
//			for(int i=0; i<position.link.size(); i++) {
//				int eval = minimax1(position.link.get(i), depth-1, true);
//				minEval = Math.min(minEval, eval);
//			}
////			System.out.println(minEval);
//			return minEval;
//		}
//	}
//	
//	private int minimax2(Num position, int depth, int alpha, int beta, boolean maximizingPlayer) {
//		if(depth == 0 || gameover()) return position.num;
//		
//		int maxEval = Integer.MIN_VALUE;
//		int minEval = Integer.MAX_VALUE;
//		
//		if(maximizingPlayer) {
//			for(int i=0; i<position.link.size(); i++) {
//				int eval = minimax2(position.link.get(i), depth-1, alpha, beta, false);
//				maxEval = Math.max(maxEval, eval);
//				alpha = Math.max(alpha, eval);
//				if(beta <= alpha) {
//					break;
//				}
//			}
//			System.out.println(maxEval);
//			return maxEval;
//		}
//		else {
//			for(int i=0; i<position.link.size(); i++) {
//				int eval = minimax2(position.link.get(i), depth-1, alpha, beta, true);
//				minEval = Math.min(minEval, eval);
//				beta = Math.min(beta, eval);
//				if(beta <= alpha) {
//					break;
//				}
//			}
//			System.out.println(minEval);
//			return minEval;
//		}
//	}
//	
//	private boolean gameover() {
//		return false;
//	}
//	
	
	// minminmaxmax
	public static int minimax1(Num position, int depth, int alpha, int beta, boolean maximizingPlayer) {
		if(depth == 0 || gameover()) return position.num;
		
		int maxEval = Integer.MIN_VALUE;
		int minEval = Integer.MAX_VALUE;
		
		if(maximizingPlayer) {
			for(int i=0; i<position.link.size(); i++) {
				int eval = minimax2(position.link.get(i), depth-1, alpha, beta, maximizingPlayer);
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if(beta <= alpha) {
					break;
				}
			}
//			System.out.println(maxEval);
			return maxEval;
		}
		else {
			for(int i=0; i<position.link.size(); i++) {
				int eval = minimax2(position.link.get(i), depth-1, alpha, beta, maximizingPlayer);
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if(beta <= alpha) {
					break;
				}
			}
//			System.out.println(minEval);
			return minEval;
		}
	}
	
	public static int minimax2(Num position, int depth, int alpha, int beta, boolean maximizingPlayer) {
		if(depth == 0 || gameover()) return position.num;
		
		int maxEval = Integer.MIN_VALUE;
		int minEval = Integer.MAX_VALUE;
		
		if(maximizingPlayer) {
			for(int i=0; i<position.link.size(); i++) {
				int eval = minimax1(position.link.get(i), depth-1, alpha, beta, !maximizingPlayer);
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if(beta <= alpha) {
					break;
				}
			}
//			System.out.println(maxEval);
			return maxEval;
		}
		else {
			for(int i=0; i<position.link.size(); i++) {
				int eval = minimax1(position.link.get(i), depth-1, alpha, beta, !maximizingPlayer);
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if(beta <= alpha) {
					break;
				}
			}
//			System.out.println(minEval);
			return minEval;
		}
	}
	
	private static boolean gameover() {
		return false;
	}
}
