package edu.handong.java.round6;

import java.awt.Point;
import java.util.ArrayList;

public class Node {
	double score;
	int[][] st;
	Point p;
	ArrayList<Node> link = new ArrayList<Node>();
	
	public Node() {}
	
	public Node(double n) {
		score = n;
	}
	
}
