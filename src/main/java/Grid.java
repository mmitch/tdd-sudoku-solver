import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid implements Cloneable {
	private List<Cell> cells = new ArrayList<>();
	private List<Set<Cell>> rows;
	private List<Set<Cell>> columns;
	private List<Set<Cell>> blocks;

	public Grid() {
		createCells();
		createLinks();
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

	public Grid clone() throws CloneNotSupportedException {
		Grid clone = (Grid) super.clone();
		clone.cells = new ArrayList<Cell>();
		for (Cell cell: cells) { // no stream because of CloneNotSupportedException :-(
			clone.cells.add(cell.clone());
		}
		clone.createLinks();
		return clone;
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

	private int getIndex(int col, int row) {
		return row * Game.COLS + col;
	}

	private int getBlockIndex(int blockCol, int blockRow) {
		return blockRow * Game.ROWS / Game.BLOCK_ROWS + blockCol;
	}

	private void createLinks() {
		linkColumns();
		linkRows();
		linkBlocks();
	}

	private void linkBlocks() {
		blocks = new ArrayList<>();
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
		rows = new ArrayList<>();
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
		columns = new ArrayList<>();
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
		IntStream.range(0, Game.COLS*Game.ROWS) //
			.forEach(i -> cells.add(new Cell()));
	}


	static int getBlockRow(int row) {
		return row/Game.BLOCK_ROWS;
	}

	static int getBlockColumn(int col) {
		return col/Game.BLOCK_COLS;
	}

}
