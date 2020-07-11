package edu.handong.java.round6;

import java.awt.Point;
import java.util.ArrayList;

public class Num {
	ArrayList<Num> link = new ArrayList<Num>();
	int num;
	Point location;
	
	public Num() {}
	
	public Num(int n) {
		num = n;
	}
}
