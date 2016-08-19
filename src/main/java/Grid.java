import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grid {
	private final List<Cell> cells = new ArrayList<>();
	private final List<Set<Cell>> rows = new ArrayList<>();
	private final List<Set<Cell>> columns = new ArrayList<>();
	private final List<Set<Cell>> blocks = new ArrayList<>();

	public Grid() {
		createCells();
		linkColumns();
		linkRows();
		linkBlocks();
	}

	private void linkBlocks() {
		int blockCount = getBlockIndex(getBlockColumn(Game.COLS-1), getBlockRow(Game.ROWS-1)) + 1;
		for (int block = 0; block < blockCount; block++) {
			blocks.add(new HashSet<>());
		}

		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				Set<Cell> block = blocks.get(getBlockIndex(getBlockColumn(col), getBlockRow(row)));
				Cell cell = getCell(col, row);
				block.add(cell);
				cell.setBlock(block);
			}
		}
	}

	private void linkRows() {
		for (int row = 0; row < Game.ROWS; row++) {
			Set<Cell> rowset = new HashSet<>();
			for (int col = 0; col < Game.COLS; col++) {
				rowset.add(getCell(col, row));
			}
			rowset.forEach(cell -> cell.setRow(rowset));
			rows.add(rowset);
		}
	}

	private void linkColumns() {
		for (int col = 0; col < Game.COLS; col++) {
			Set<Cell> columnset = new HashSet<>();
			for (int row = 0; row < Game.ROWS; row++) {
				columnset.add(getCell(col, row));
			}
			columnset.forEach(cell -> cell.setColumn(columnset));
			columns.add(columnset);
		}
	}

	private void createCells() {
		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				setCell(col, row, new Cell());
			}
		}
	}

	public Cell getCell(int col, int row) {
		return cells.get(getIndex(col, row));
	}

	private void setCell(int col, int row, Cell cell) {
		cells.add(getIndex(col, row), cell);
	}

	private int getIndex(int col, int row) {
		return row * Game.COLS + col;
	}

	private int getBlockIndex(int blockCol, int blockRow) {
		return blockRow * Game.ROWS / Game.BLOCK_ROWS + blockCol;
	}

	public Set<Cell> getRow(int row) {

		return rows.get(row);
	}

	public Set<Cell> getColumn(int col) {
		return columns.get(col);
	}

	public Set<Cell> getBlock(int blockCol, int blockRow) {
		return blocks.get(getBlockIndex(blockCol, blockRow));
	}

	public void setValue(int col, int row, Integer value) {
		getCell(col, row).setValue(value);
		getAllAffectedCells(col, row).forEach(c -> c.removePossible(value));
	}

	public Stream<Cell> cells() {
		return cells.stream();
	}

	public static int getBlockRow(int row) {
		return row/Game.BLOCK_ROWS;
	}

	public static int getBlockColumn(int col) {
		return col/Game.BLOCK_COLS;
	}

	private Set<Cell> getAllAffectedCells(int col, int row) {
		return Stream.concat( //
								getRow(row).stream(), //
								Stream.concat( //
										getColumn(col).stream(), //
										getBlockForCellAt(col, row).stream()) //
								) //
						.collect(Collectors.toCollection(HashSet::new)); // HashSet deduplicates automatically
	}

	private Set<Cell> getBlockForCellAt(int col, int row) {
		return getBlock(getBlockColumn(col), getBlockRow(row));
	}

}
