package com.magizdev.gobbler.view;

public class GameModel {
	public class Node {
		public int x;
		public int y;
		public int data;
	}
	
	public static final int BLANK = 0;

	private int xCount;
	private int yCount;
	private int[][] map;
	private int iconCounts = 17;
	private Node[] path = null;

	public GameModel(int x, int y, int iconCount) {
		xCount = x;
		yCount = y;
		this.iconCounts = iconCount;
		map = new int[xCount][yCount];
	}
	
	public int getNode(int x, int y){
		return map[x][y];
	}

	public void init() {
		int x = 1;
		int y = 0;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				map[i][j] = x;
				if (y == 1) {
					x++;
					y = 0;
					if (x == iconCounts) {
						x = 1;
					}
				} else {
					y = 1;
				}
			}
		}
	}

}
