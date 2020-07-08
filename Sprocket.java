
import java.util.ArrayList;
import java.util.Collections;

import processing.core.PApplet;

/***
 * Constructs a random grid array of connected cells resembling bicycle chain.
 *
 */

public class Sprocket extends processing.core.PApplet {

	public static void main(String args[]) {
		PApplet.main(new String[] { "Sprocket" });
	}

	// variables
	int dist = 50; // center distance
	int POS = 1;
	int NEG = -1;
	int YES = 0;
	int NO = -2;
	int[][] grid; // center locations

	public void settings() {
		size(800, 800);
	}

	public void setup() {
		background(64);
		noStroke();
		grid = new int[41][41];
		dist = width / (grid.length + 1);
		fillGrid();
	}

	public void fillGrid() {
		ArrayList<Pair> toProcess = new ArrayList<>();
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				int v = r * grid[r].length + c;
				if (r % 2 == 1 && c % 2 == 1)
					grid[r][c] = POS;
				else if (r % 2 == 0 && c % 2 == 0) {
					grid[r][c] = NEG;

				} else {
					grid[r][c] = NO;
					toProcess.add(new Pair(r, c));
				}
			}
		}
		process(toProcess);
	}

	private void process(ArrayList<Pair> links) {
		Collections.shuffle(links);
		while (links.size() > 0) {
			Pair p = links.remove(links.size() - 1);
			if (!border(p)) {
				Pair[] endpoints = endpoints(p);
				if (endpoints[0] != null
						&& numLinks(endpoints[0]) < 2) {
					grid[p.r][p.c] = YES;
				}
				if (endpoints[1] != null
						&& numLinks(endpoints[1]) < 2) {
					grid[p.r][p.c] = YES;
				}

			}
		}

	}

	/**
	 * Can link and unlink pairs using YES/NO
	 * 
	 * @param p1
	 * @param p2
	 * @param linked the state to set for the link (YES or NO)
	 */
	private void link(Pair p1, Pair p2, int linked) {
		int row = (p1.r + p2.r) / 2;
		int col = (p1.c + p2.c) / 2;
		grid[row][col] = linked;
	}

	/**
	 * Given a link pair, return its endpoints. One of the endpoints may be
	 * null, if the link borders the perimeter.
	 * 
	 * @param p
	 *            a pair representing a link location in the grid
	 * @return two pairs, one of which may be null
	 */
	private Pair[] endpoints(Pair p) {
		// if even row, links are vertical
		if (p.r % 2 == 0) {
			Pair above = p.r > 0 ? new Pair(p.r - 1, p.c) : null;
			Pair below = p.r < grid.length - 1
					? new Pair(p.r + 1, p.c)
					: null;
			return new Pair[] { above, below };
		}
		// odd row, so link is horizontal
		Pair left = p.c > 0 ? new Pair(p.r, p.c - 1) : null;
		Pair right = p.c < grid[0].length - 1
				? new Pair(p.r, p.c + 1)
				: null;
		return new Pair[] { left, right };
	}

	/**
	 * Counts the number of links made surrounding this location
	 * 
	 * @param p
	 * @return
	 */
	private int numLinks(Pair p) {
		if (border(p))
			return -1;
		int num = 0;
		if (grid[p.r - 1][p.c] == YES)
			num++;
		if (grid[p.r + 1][p.c] == YES)
			num++;
		if (grid[p.r][p.c - 1] == YES)
			num++;
		if (grid[p.r][p.c + 1] == YES)
			num++;
		return num;
	}

	/**
	 * Tests whether these coordinates lie along the border.
	 * 
	 * @param r
	 * @param c
	 * @return is along first or last row or column
	 */
	private boolean border(int r, int c) {
		if (r == 0 || c == 0)
			return true;
		if (r == grid.length - 1 || c == grid[r].length - 1)
			return true;
		return false;
	}

	/**
	 * Alternate way to determine if pair lies along the border.
	 * 
	 * @param p
	 * @return is along first or last row or column
	 */
	private boolean border(Pair p) {
		return border(p.r, p.c);
	}

	public void draw() {
		// translate(width/2, 0);
		// rotate(PI/4);
		translate(dist, dist);
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				pushMatrix();
				translate(dist * c, dist * r);
				drawCell(r, c);
				popMatrix();
			}
		}
	}

	void drawCell(int r, int c) {
		if (grid[r][c] == POS) {
			fill(255, 255, 0);
			ellipse(0, 0, dist * sqrt(2), dist * sqrt(2));
			double quarter = grid.length / 4f;
			if (r < quarter || r > 3 * quarter) {
				fill(64);
				ellipse(0, 0, dist / 1.5f, dist / 1.5f);
			}
		} else if (grid[r][c] == NEG) {
			// cover with arcs in units of PI/4 if connection
			fill(255, 255, 0);
			if (exists(r - 1, c) && grid[r - 1][c] == YES) {
				arc(0, 0, dist * 2, dist * 2, 5 * PI / 4,
						7 * PI / 4);
			}
			if (exists(r + 1, c) && grid[r + 1][c] == YES) {
				arc(0f, 0f, dist * 2.1f, dist * 2.1f, PI / 4,
						3 * PI / 4);
			}
			if (exists(r, c - 1) && grid[r][c - 1] == YES) {
				arc(0f, 0f, dist * 2.1f, dist * 2.1f, 3 * PI / 4,
						5 * PI / 4);
			}
			if (exists(r, c + 1) && grid[r][c + 1] == YES) {
				arc(0, 0, dist * 2.1f, dist * 2.1f, 7 * PI / 4,
						9 * PI / 4);
			}

			// always draw the negative space circle
			fill(64);
			ellipse(0, 0, dist * sqrt(2), dist * sqrt(2));
		}
	}

	boolean exists(int r, int c) {
		return r >= 0 && r < grid.length && c >= 0
				&& c < grid[r].length;
	}

	public void keyPressed() {
	}

	public void mousePressed() {
		background(64);
		fillGrid();
	}

	class Pair {
		int r, c;

		Pair(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
}
