import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	public Stream<Cell> cells() {
		return cells.stream();
	}

	public List<Set<Cell>> getRows() {
		return rows;
	}

	public List<Set<Cell>> getColumns() {
		return columns;
	}

	public List<Set<Cell>> getBlocks() {
		return blocks;
	}

	Cell getCell(int col, int row) {
		return cells.get(getIndex(col, row));
	}

	Set<Cell> getRow(int row) {

		return rows.get(row);
	}

	Set<Cell> getColumn(int col) {
		return columns.get(col);
	}

	Set<Cell> getBlock(int blockCol, int blockRow) {
		return blocks.get(getBlockIndex(blockCol, blockRow));
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

	static int getBlockRow(int row) {
		return row/Game.BLOCK_ROWS;
	}

	static int getBlockColumn(int col) {
		return col/Game.BLOCK_COLS;
	}

}
