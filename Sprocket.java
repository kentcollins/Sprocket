
import processing.core.PApplet;

/***
 * Builds a sketch by extending the PApplet class. In order for this to work,
 * the core Processing libraries must be available on the build path -- these
 * are located in a Java archive called "core.jar" which is distributed as part
 * of the Processing IDE. Look for Processing>Contents>Java>core.jar
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
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				if (r % 2 == 1 && c % 2 == 1)
					grid[r][c] = POS;
				else {
					grid[r][c] = (r + c) % 2 == 0 ? NEG : NO;
					if (grid[r][c] == NO && Math.random() < 0.5
							&& !border(r, c))
						grid[r][c] = YES;
				}
			}
		}
	}

	public boolean border(int r, int c) {
		if (r == 0 || c == 0)
			return true;
		if (r == grid.length - 1 || c == grid[r].length - 1)
			return true;
		return false;
	}

	public void draw() {
		translate(dist, dist);
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				pushMatrix();
				translate(dist * c, dist * r);
				drawCell(r, c);
				popMatrix();
			}
		}
		// resetMatrix();
		// rectMode(CENTER);
		// fill(255, 0, 255);
		// rect(width/2, height/2, width/2, height/2, -50);
	}

	void drawCell(int r, int c) {
		if (grid[r][c] == POS) {
			fill(255, 255, 0);
			ellipse(0, 0, dist * sqrt(2), dist * sqrt(2));
			double quarter = grid.length/4f;
			if (r < quarter || r > 3*quarter) {
				fill(64);
				ellipse(0, 0, dist / 2.0f, dist / 2.0f);
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

	/**
	 * When the operating system detects a key press, it executes this method.
	 * Avoid putting drawing commands in here. Instead, use this method to
	 * modify variables that are referenced by draw().
	 */
	public void keyPressed() {
	}

	/**
	 * When the operating system detects a mouse press, it executes this method.
	 * Avoid putting drawing commands in here. Instead, use this method to
	 * modify variables that are referenced by draw().
	 */
	public void mousePressed() {
		background(64);
		fillGrid();
	}

	/**
	 * See the PApplet documentation to discover other methods that can be
	 * overridden.
	 */
}
