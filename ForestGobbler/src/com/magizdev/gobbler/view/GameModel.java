package com.magizdev.gobbler.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {
	public class Node {
		public int x;
		public int y;

		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static final int BLANK = 0;
	public static final int GRAVITY_UP = 1;
	public static final int GRAVITY_RIGHT = 2;
	public static final int GRAVITY_DOWN = 3;
	public static final int GRAVITY_LEFT = 4;

	private int xCount;
	private int yCount;
	private int iconCounts;
	private int[][] map;
	private int gravity = 3;

	private List<Node> path = new ArrayList<Node>();
	private List<Node> n1E = new ArrayList<Node>();
	private List<Node> n2E = new ArrayList<Node>();

	public GameModel(int x, int y, int iconCount) {
		xCount = x;
		yCount = y;
		this.iconCounts = iconCount;
		map = new int[xCount][yCount];
	}

	public int getNode(int x, int y) {
		return map[x][y];
	}

	public void init() {
		int x = 0;
		int temp = iconCounts - 1;
		for (int i = 1; i < xCount - 1; i++) {
			for (int j = 1; j < yCount - 1; j++) {
				map[i][j] = (x++ / 2) % temp + 1;
			}
		}
		change();
		path.clear();
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public List<Node> getPath() {
		return this.path;
	}

	public int getWidth() {
		return yCount;
	}

	public int getHeight() {
		return xCount;
	}

	public void clear(Node pair1, Node pair2) {
		map[pair1.x][pair1.y] = BLANK;
		map[pair2.x][pair2.y] = BLANK;
		onGravity(pair1);
		onGravity(pair2);
	}

	private void onGravity(Node node) {
		int x = node.x;
		int y = node.y;
		int move = 0;
		switch (this.gravity) {
		case GRAVITY_UP:
			for (int i = 1; i + move < yCount; i++) {
				if (map[x][i] == BLANK) {
					while (i + move != yCount && map[x][i + move] == BLANK) {
						move++;
					}
					map[x][i] = map[x][i + move];
					map[x][i + move] = BLANK;
				}
			}
			break;
		case GRAVITY_RIGHT:
			for (int i = xCount - 1; i - move >= 0; i--) {
				if (map[i][y] == BLANK) {
					while (i - move != 0 && map[i - move][y] == BLANK) {
						move++;
					}
					map[i][y] = map[i - move][y];
					map[i - move][y] = BLANK;
				}
			}
			break;
		case GRAVITY_DOWN:
			for (int i = xCount - 1; i - move >= 0; i--) {
				if (map[x][i] == BLANK) {
					while (i - move != 0 && map[x][i - move] == BLANK) {
						move++;
					}
					map[x][i] = map[x][i - move];
					map[x][i - move] = BLANK;
				}
			}
			break;
		case GRAVITY_LEFT:
			for (int i = 1; i + move < xCount; i++) {
				if (map[i][y] == BLANK) {
					while (i + move != xCount && map[i + move][y] == BLANK) {
						move++;
					}
					map[i][y] = map[i + move][y];
					map[i + move][y] = BLANK;
				}
			}
			break;
		default:
			break;
		}
	}

	public void change() {
		Random random = new Random();
		List<Node> nonEmptyNodes = new ArrayList<Node>();
		int tmpV, tmpX, tmpY;
		for (int x = 1; x < xCount - 1; x++) {
			for (int y = 1; y < yCount - 1; y++) {
				// tmpX = 1 + random.nextInt(xCount - 2);
				// tmpY = 1 + random.nextInt(yCount - 2);
				// tmpV = map[x][y];
				// map[x][y] = map[tmpX][tmpY];
				// map[tmpX][tmpY] = tmpV;

				if (map[x][y] != BLANK) {
					nonEmptyNodes.add(new Node(x, y));
				}
			}
		}

		for (int i = 0; i < nonEmptyNodes.size(); i++) {
			int temp = random.nextInt(nonEmptyNodes.size());
			tmpV = map[nonEmptyNodes.get(i).x][nonEmptyNodes.get(i).y];
			map[nonEmptyNodes.get(i).x][nonEmptyNodes.get(i).y] = map[nonEmptyNodes
					.get(temp).x][nonEmptyNodes.get(temp).y];
			map[nonEmptyNodes.get(temp).x][nonEmptyNodes.get(temp).y] = tmpV;
		}
		if (die()) {
			change();
		}
	}

	public boolean die() {
		boolean die = noLivePath();
		path.clear();
		return die;
	}

	public boolean noLivePath() {
		for (int y = 1; y < yCount - 1; y++) {
			for (int x = 1; x < xCount - 1; x++) {
				if (map[x][y] != 0) {
					for (int j = y; j < yCount - 1; j++) {
						if (j == y) {
							for (int i = x + 1; i < xCount - 1; i++) {
								if (link(new Node(x, y), new Node(i, j))) {
									return false;
								}
							}
						} else {
							for (int i = 1; i < xCount - 1; i++) {
								if (link(new Node(x, y), new Node(i, j))) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	public boolean win() {
		for (int x = 0; x < xCount; x++) {
			for (int y = 0; y < yCount; y++) {
				if (map[x][y] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean link(Node n1, Node n2) {
		if ((n1.x == n2.x && n1.y == n2.y)
				|| map[n1.x][n1.y] != map[n2.x][n2.y]) {
			return false;
		}
		path.clear();
		if (map[n1.x][n1.y] == map[n2.x][n2.y]) {
			if (linkD(n1, n2)) {
				path.add(n1);
				path.add(n2);
				return true;
			}

			Node n = new Node(n1.x, n2.y);
			if (map[n.x][n.y] == 0) {
				if (linkD(n1, n) && linkD(n, n2)) {
					path.add(n1);
					path.add(n);
					path.add(n2);
					return true;
				}
			}
			n = new Node(n2.x, n1.y);
			if (map[n.x][n.y] == 0) {
				if (linkD(n1, n) && linkD(n, n2)) {
					path.add(n1);
					path.add(n);
					path.add(n2);
					return true;
				}
			}
			expandX(n1, n1E);
			expandX(n2, n2E);

			for (Node node1 : n1E) {
				for (Node node2 : n2E) {
					if (node1.x == node2.x) {
						if (linkD(node1, node2)) {
							path.add(n1);
							path.add(node1);
							path.add(node2);
							path.add(n2);
							return true;
						}
					}
				}
			}

			expandY(n1, n1E);
			expandY(n2, n2E);
			for (Node node1 : n1E) {
				for (Node node2 : n2E) {
					if (node1.y == node2.y) {
						if (linkD(node1, node2)) {
							path.add(n1);
							path.add(node1);
							path.add(node2);
							path.add(n2);
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}

	private boolean linkD(Node n1, Node n2) {
		if (n1.x == n2.x) {
			int y1 = Math.min(n1.y, n2.y);
			int y2 = Math.max(n1.y, n2.y);
			boolean flag = true;
			for (int y = y1 + 1; y < y2; y++) {
				if (map[n1.x][y] != 0) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return true;
			}
		}
		if (n1.y == n2.y) {
			int x1 = Math.min(n1.x, n2.x);
			int x2 = Math.max(n1.x, n2.x);
			boolean flag = true;
			for (int x = x1 + 1; x < x2; x++) {
				if (map[x][n1.y] != 0) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return true;
			}
		}
		return false;
	}

	private void expandX(Node n, List<Node> nl) {
		nl.clear();
		for (int x = n.x + 1; x < xCount; x++) {
			if (map[x][n.y] != 0) {
				break;
			}
			nl.add(new Node(x, n.y));
		}
		for (int x = n.x - 1; x >= 0; x--) {
			if (map[x][n.y] != 0) {
				break;
			}
			nl.add(new Node(x, n.y));
		}
	}

	private void expandY(Node n, List<Node> nl) {
		nl.clear();
		for (int y = n.y + 1; y < yCount; y++) {
			if (map[n.x][y] != 0) {
				break;
			}
			nl.add(new Node(n.x, y));
		}
		for (int y = n.y - 1; y >= 0; y--) {
			if (map[n.x][y] != 0) {
				break;
			}
			nl.add(new Node(n.x, y));
		}
	}
}
