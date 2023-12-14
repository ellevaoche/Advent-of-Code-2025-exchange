import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * see: https://adventofcode.com/2023/day/14
 */
public class Y23Day14 {

	/*
	 * Example:
	 * 
	 * O....#....
	 * O.OO#....#
	 * .....##...
	 * OO.#O....O
	 * .O.....O#.
	 * O.#..O.#.#
	 * ..O..#O..O
	 * .......O..
	 * #....###..
	 * #OO..#....
	 * 
	 */

	private static final String INPUT_RX = "^([.#O]*)$";
	
	public static record InputData(String row) {
		@Override public String toString() {
			return row;
		}
	}
	
	public static class InputProcessor implements Iterable<InputData>, Iterator<InputData> {
		private Scanner scanner;
		public InputProcessor(String inputFile) {
			try {
				scanner = new Scanner(new File(inputFile));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		@Override public Iterator<InputData> iterator() { return this; }
		@Override public boolean hasNext() { return scanner.hasNext(); }
		@Override public InputData next() {
			String line = scanner.nextLine().trim();
			while (line.length() == 0) {
				line = scanner.nextLine();
			}
			if (line.matches(INPUT_RX)) {
				String row = line.replaceFirst(INPUT_RX, "$1");
				return new InputData(row);
			}
			else {
				throw new RuntimeException("invalid line '"+line+"'");
			}
		}
	}

	
	public static class World {
		char[][] field;
		List<String> rows;
		int maxX;
		int maxY;
		public World() {
			rows = new ArrayList<>();
		}
		public void init() {
			maxY = rows.size();
			maxX = rows.get(0).length();
			field = new char[maxY][];
			for (int y=0; y<maxY; y++) {
				field[y] = rows.get(y).toCharArray();
			}
		}
		public void addRow(String row) {
			rows.add(row);
		}
		public void tiltNorth() {
			boolean changed = true;
			while (changed) {
				changed = false;
				for (int y=0; y<maxY; y++) {
					for (int x=0; x<maxX; x++) {
						if ((get(x, y)=='O') && (get(x, y-1) == '.')) {
							changed = true;
							set(x,y,'.');
							set(x,y-1,'O');
						}
					}
				}
			}
		}
		public int countLoad() {
			int result = 0;
			for (int y=0; y<maxY; y++) {
				for (int x=0; x<maxX; x++) {
					if (get(x,y)=='O') {
						result = result + maxY-y;
					}
				}
			}
			return result;
		}
		private void set(int x, int y, char c) {
			field[y][x]=c;
		}
		char get(int x, int y) {
			if ((x<0) || (x>=maxX) || (y<0) || (y>=maxY)) {
				return '#';
			}
			return field[y][x];
		}
		@Override public String toString() {
			StringBuilder result = new StringBuilder();
			for (int y=0; y<maxY; y++) {
				result.append(new String(field[y])).append('\n');
			}
			return result.toString();
		}
	}

	public static void mainPart1(String inputFile) {
		World world = new World();
		for (InputData data:new InputProcessor(inputFile)) {
			System.out.println(data);
			world.addRow(data.row);
		}
		System.out.println("---");
		world.init();
		System.out.println(world);
		world.tiltNorth();
		System.out.println(world);
		System.out.println("LOAD: "+world.countLoad());
	}
	
	
	public static void mainPart2(String inputFile) {
	}


	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("--- PART I ---");
//		mainPart1("exercises/day14/Feri/input-example.txt");
		mainPart1("exercises/day14/Feri/input.txt");               
		System.out.println("---------------");                           
		System.out.println("--- PART II ---");
		mainPart2("exercises/day14/Feri/input-example.txt");
//		mainPart2("exercises/day14/Feri/input.txt");              
		System.out.println("---------------");    //
	}
	
}
