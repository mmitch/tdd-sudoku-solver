import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class GridTest {

	@Test
	public void neuesGridHatNurLeereZellen() {
		// given

		// when
		Grid grid = new Grid();

		// then
		for (int row = 0; row < Game.ROWS; row++) {
			for (int col = 0; col < Game.COLS; col++) {
				assertThat(grid.getCell(col, row).getValue().isPresent(), is(false));
			}
		}
	}

	@Test
	public void reiheLesenLiefertDieKorrektenZellen() {
		// given
		Grid grid = new Grid();
		int rowNum = 2;

		// when
		List<Cell> row = grid.getRow(rowNum);

		// then
		assertThat(row.size(), is(Game.COLS));
		for (int col = 0; col < Game.COLS; col++) {
			assertThat(row.get(col), sameInstance(grid.getCell(col, rowNum)));
		}
	}

	@Test
	public void spalteLesenLiefertDieKorrektenZellen() {
		// given
		Grid grid = new Grid();
		int colNum = 1;

		// when
		List<Cell> col = grid.getColumn(colNum);

		// then
		assertThat(col.size(), is(Game.ROWS));
		for (int row = 0; row < Game.ROWS; row++) {
			assertThat(col.get(row), sameInstance(grid.getCell(colNum, row)));
		}
	}

	@Test
	public void blockLesenLiefertDieKorrektenZellen() {
		// given
		Grid grid = new Grid();
		int blockCol = 1;
		int blockRow = 2;

		// when
		List<Cell> block = grid.getBlock(blockCol, blockRow);

		// then
		assertThat(block.size(), is(Game.ALL_NUMBERS.size()));
		int i = 0;
		for (int row = blockRow * Game.BLOCK_ROWS; row < (blockRow + 1) * Game.BLOCK_ROWS; row++) {
			for (int col = blockCol * Game.BLOCK_COLS; col < (blockCol + 1) * Game.BLOCK_COLS; col++) {
				assertThat(block.get(i), sameInstance(grid.getCell(col, row)));
				i++;
			}
		}
	}

	@Test
	public void wertSetzenEntferntMoeglichkeitAusReihe() {
		// given
		Grid grid = new Grid();
		int rowNum = 2;
		int colNum = 4;
		int value = 9;

		// when
		grid.setValue(colNum, rowNum, value);

		// then
		assertThat(grid.getCell(colNum, rowNum).getValue().get(), is(value));
		for (int col = 0; col < Game.COLS; col++) {
			assertThat(String.format("cell(%d,%d)", col, rowNum), grid.getCell(col, rowNum).isPossible(value), is(false));
		}
	}

	@Test
	public void wertSetzenEntferntMoeglichkeitAusSpalte() {
		// given
		Grid grid = new Grid();
		int rowNum = 2;
		int colNum = 4;
		int value = 9;

		// when
		grid.setValue(colNum, rowNum, value);

		// then
		assertThat(grid.getCell(colNum, rowNum).getValue().get(), is(value));
		for (int row = 0; row < Game.ROWS; row++) {
			assertThat(String.format("cell(%d,%d)", colNum, row), grid.getCell(colNum, row).isPossible(value), is(false));
		}
	}

	@Test
	public void wertSetzenEntferntMoeglichkeitAusBlock() {
		// given
		Grid grid = new Grid();
		int rowNum = 2;
		int colNum = 4;
		int value = 9;
		int blockRow = 0; // TODO calculate this
		int blockCol = 1; // TODO calculate this

		// when
		grid.setValue(colNum, rowNum, value);

		// then
		assertThat(grid.getCell(colNum, rowNum).getValue().get(), is(value));
		for (int row = blockRow * Game.BLOCK_ROWS; row < (blockRow + 1) * Game.BLOCK_ROWS; row++) {
			for (int col = blockCol * Game.BLOCK_COLS; col < (blockCol + 1) * Game.BLOCK_COLS; col++) {
				assertThat(String.format("cell(%d,%d)", col, row), grid.getCell(col, row).isPossible(value), is(false));
			}
		}
	}

}
