import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;

public class GridTest {

	private Grid grid = new Grid();

	@Test
	public void neuesGridHatNurLeereZellen() {
		// given

		// when

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
		int rowNum = 2;

		// when
		Collection<Cell> row = grid.getRow(rowNum);

		// then
		assertThat(row.size(), is(Game.COLS));
		Cell[] expectedRow = new Cell[Game.COLS];
		for (int col = 0; col < Game.COLS; col++) {
			expectedRow[col] = grid.getCell(col, rowNum);
		}
		assertThat(row, containsInAnyOrder(expectedRow));
	}

	@Test
	public void spalteLesenLiefertDieKorrektenZellen() {
		// given
		int colNum = 1;

		// when
		Collection<Cell> col = grid.getColumn(colNum);

		// then
		assertThat(col.size(), is(Game.ROWS));
		Cell[] expectedCol = new Cell[Game.ROWS];
		for (int row = 0; row < Game.ROWS; row++) {
			expectedCol[row] = grid.getCell(colNum, row);
		}
		assertThat(col, containsInAnyOrder(expectedCol));
	}

	@Test
	public void blockLesenLiefertDieKorrektenZellen() {
		// given
		int blockCol = 1;
		int blockRow = 2;

		// when
		Collection<Cell> block = grid.getBlock(blockCol, blockRow);

		// then
		assertThat(block.size(), is(Game.ALL_NUMBERS.size()));
		Cell[] expectedBlock = new Cell[Game.ALL_NUMBERS.size()];
		int i=0;
		for (int row = blockRow * Game.BLOCK_ROWS; row < (blockRow + 1) * Game.BLOCK_ROWS; row++) {
			for (int col = blockCol * Game.BLOCK_COLS; col < (blockCol + 1) * Game.BLOCK_COLS; col++) {
				expectedBlock[i] = grid.getCell(col, row);
				i++;
			}
		}
		assertThat(block, containsInAnyOrder(expectedBlock));
	}

	@Test
	public void wertSetzenEntferntMoeglichkeitAusReihe() {
		// given
		int rowNum = 2;
		int colNum = 4;
		int value = 9;

		// when
		grid.getCell(colNum, rowNum).setValue(value);

		// then
		assertThat(grid.getCell(colNum, rowNum).getValue().get(), is(value));
		for (int col = 0; col < Game.COLS; col++) {
			assertThatCell(col, rowNum, c -> c.isPossible(value), false);
		}
	}

	@Test
	public void wertSetzenEntferntMoeglichkeitAusSpalte() {
		// given
		int rowNum = 2;
		int colNum = 4;
		int value = 9;

		// when
		grid.getCell(colNum, rowNum).setValue(value);

		// then
		assertThat(grid.getCell(colNum, rowNum).getValue().get(), is(value));
		for (int row = 0; row < Game.ROWS; row++) {
			assertThatCell(colNum, row, c -> c.isPossible(value), false);
		}
	}

	@Test
	public void wertSetzenEntferntMoeglichkeitAusBlock() {
		// given
		int rowNum = 2;
		int colNum = 4;
		int value = 9;
		int blockRow = 0; // TODO calculate this
		int blockCol = 1; // TODO calculate this

		// when
		grid.getCell(colNum, rowNum).setValue(value);

		// then
		assertThat(grid.getCell(colNum, rowNum).getValue().get(), is(value));
		for (int row = blockRow * Game.BLOCK_ROWS; row < (blockRow + 1) * Game.BLOCK_ROWS; row++) {
			for (int col = blockCol * Game.BLOCK_COLS; col < (blockCol + 1) * Game.BLOCK_COLS; col++) {
				assertThatCell(col, row, c -> c.isPossible(value), false);
			}
		}
	}

	@Test
	public void blockColumnFuerSpalte6Ist2()
	{
		assertThat(Grid.getBlockColumn(6), is(2));
	}

	@Test
	public void blockRowFuerZeile3Ist1()
	{
		assertThat(Grid.getBlockRow(3), is(1));
	}

	@Test
	public void alleZellenInZeile6KennenIhreNachbarn()
	{
		Collection<Cell> row = grid.getRow(6);

		for (Cell c: row) {
			assertThat(c.getRow(), containsInAnyOrder(row.toArray()));
		}
	}

	@Test
	public void alleZellenInSpalte3KennenIhreNachbarn()
	{
		Collection<Cell> column = grid.getColumn(3);

		for (Cell c: column) {
			assertThat(c.getColumn(), containsInAnyOrder(column.toArray()));
		}
	}

	@Test
	public void alleZellenInBlock0_1kennenIhreNachbarn()
	{
		Collection<Cell> block = grid.getBlock(0, 1);

		for (Cell c: block) {
			assertThat(c.getBlock(), containsInAnyOrder(block.toArray()));
		}
	}

	@Test
	public void geklontesGridHatGleichenInhaltAberNeueInstanz() throws Exception
	{
		// given
		Cell cell = grid.getCell(3, 2);
		cell.setValue(1);

		// when
		Grid cloneGrid = grid.clone();

		// then
		Cell cloneCell = cloneGrid.getCell(3, 2);
		assertThat(cloneCell, not(sameInstance(cell)));
		assertThat(cloneCell.getValue(), is(cell.getValue()));
	}

	@Test
	public void gekolontesGridHatNeueInstanzFuerSpalten() throws Exception {
		// given
		Set<Cell> column = grid.getColumn(5);
		column.iterator().next().setValue(8);

		// when
		Grid cloneGrid = grid.clone();

		// then
		Set<Cell> cloneColumn = cloneGrid.getColumn(5);
		assertThat(cloneColumn, not(sameInstance(column)));
		for (Cell cloneCell: cloneColumn) {
			assertThat(cloneCell.getPossibles(), not(hasItem(8)));
		}
	}

	private void assertThatCell(int col, int row, Predicate<Cell> actualFunc, boolean expected) {
		assertThat(
				String.format("cell(%d,%d)", col, row),
				actualFunc.test(grid.getCell(col, row)),
				is(expected)
				);
	}

}
