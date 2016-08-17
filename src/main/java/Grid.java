import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grid {
	private List<Cell> cells = new ArrayList<Cell>();

	public Grid() {
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

	public List<Cell> getRow(int row) {
		List<Cell> rowList = new ArrayList<>();
		for (int col = 0; col < Game.COLS; col++) {
			rowList.add(getCell(col, row));
		}
		return rowList;
	}

	public List<Cell> getColumn(int col) {
		List<Cell> colList = new ArrayList<>();
		for (int row = 0; row < Game.ROWS; row++) {
			colList.add(getCell(col, row));
		}
		return colList;
	}

	public List<Cell> getBlock(int blockCol, int blockRow) {
		List<Cell> colList = new ArrayList<>();
		for (int row = blockRow * Game.BLOCK_ROWS; row < (blockRow + 1) * Game.BLOCK_ROWS; row++) {
			for (int col = blockCol * Game.BLOCK_COLS; col < (blockCol + 1) * Game.BLOCK_COLS; col++) {
				colList.add(getCell(col, row));
			}
		}
		return colList;
	}

	private Set<Cell> getAllAffectedCells(int col, int row) {
		Set<Cell> cells = new HashSet<>(getRow(row));
		cells.addAll(getColumn(col));
		cells.addAll(getBlock(col/Game.BLOCK_COLS, row/Game.BLOCK_ROWS));
		return cells;
	}

	public void setValue(int col, int row, Integer value) {
		getCell(col, row).setValue(value);
		getAllAffectedCells(col, row).forEach(c -> c.removePossible(value));
	}

}
