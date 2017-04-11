import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dumper {

	final static PrintStream ps = System.out;

	public static void dump(Grid grid) {
		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				ps.printf("%s ", cellValueToString(grid.getCell(col, row)));
			}
			ps.println();
		}
	}

	public static void dumpDetailled(Grid grid) {
		for (int row = 0; row < Game.ROWS; row++) {
			String zeile = "";
			for (int col = 0; col < Game.COLS; col++) {
				zeile = multilineMerge(zeile, dumpCellDetailled(grid.getCell(col, row)));
			}
			ps.println(zeile);
		}
	}

	private static String dumpCellDetailled(Cell cell) {
		final String SPACER = String.format("%" + Game.BLOCK_COLS + "s", "");
		String s;
		s = cellValueToString(cell) + SPACER;
		int number = 0;
		for (int row = 0; row < Game.BLOCK_ROWS; row++) {
			s += "\n ";
			for (int col = 0; col < Game.BLOCK_COLS; col++) {
				number++;
				s += cellPossibleToString(cell, number);
			}
		}
		return s;
	}

	private static String cellPossibleToString(Cell cell, Integer number) {
		if (cell.isPossible(number)) {
			return String.format("%01d", number);
		} else {
			return " ";
		}
	}

	private static String multilineMerge(String current, String additional) {
		List<String> currentList = new ArrayList<String>(Arrays.asList(current.split("\n")));
		List<String> additionalList = Arrays.asList(additional.split("\n"));

		while (currentList.size() < additionalList.size()) {
			currentList.add("");
		}

		String ret = "";
		for (int i = 0; i < additionalList.size(); i++) {
			if (!ret.isEmpty()) {
				ret += "\n";
			}
			ret += currentList.get(i) + additionalList.get(i);
		}

		return ret;
	}

	private static String cellValueToString(Cell cell) {
		return cell.getValue().map(value -> String.format("%01d", value)).orElse(".");
	}

}
