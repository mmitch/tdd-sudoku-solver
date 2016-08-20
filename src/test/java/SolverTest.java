import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

public class SolverTest {

	Grid grid = new Grid();
	Solver solver = new Solver(grid);
	
	@Test
	public void ergaenzeZahlWennNurEineMoeglichkeitBleibt() {
		// given
		Cell cell = grid.getCell(0, 0);
		cell.getPossibles().clear();
		cell.getPossibles().add(3);

		// when
		solver.solve();

		// then
		assertThat(cell.getValue(), isPresent());
		assertThat(cell.getValue().get(), is(3));
	}

	@Test
	public void ergaenzeFehlende4InZeile3() {
		// given
		setStartPostitionInGrid( //
				"...   ..." +
				"...   ..." +
				"...   ..." +
				"123.56789" +
				"   ...   " +
				"   ...   " +
				"...   ..." +
				"...   ..." +
				"...   ...");

		// when
		solver.solve();

		// then
		Optional<Integer> cellValue = grid.getCell(3, 3).getValue();
		assertThat(cellValue, isPresent());
		assertThat(cellValue.get(), is(4));
	}
	
	@Test
	public void ergaenzeFehlende7InSpalte7() {
		// given
		setStartPostitionInGrid( //
				"...   .1." +
				"...   .2." +
				"...   .3." +
				"   ... 4 " +
				"   ... 5 " +
				"   ... 6 " +
				"...   ..." +
				"...   .8." +
				"...   .9.");

		// when
		solver.solve();

		// then
		Optional<Integer> cellValue = grid.getCell(7, 6).getValue();
		assertThat(cellValue, isPresent());
		assertThat(cellValue.get(), is(7));
	}
	
	@Test
	public void ergaenzeFehlende2InBlock0_1() {
		// given
		setStartPostitionInGrid( //
				"...   ..." +
				"...   ..." +
				"...   ..." +
				"1 3...   " +
				"456...   " +
				"789...   " +
				"...   ..." +
				"...   ..." +
				"...   ...");

		// when
		solver.solve();

		// then
		Optional<Integer> cellValue = grid.getCell(1, 3).getValue();
		assertThat(cellValue, isPresent());
		assertThat(cellValue.get(), is(2));
	}

	@Test
	public void loeseEinfachesSudoku() {
		// given
		setStartPostitionInGrid( //
				"49.  36.." +
				"..71  .54" +
				"..15 67.." +
				"   .628  " +
				"6 2... 39" +
				" 8 7.1 2 " +
				"35.   47." +
				"..631 ..." +
				"1..8 7..3");

		// when
		solver.solve();

		// then
		assertThatGridIsSolved();
	}

	@Test
	public void loeseSchwierigesSudoku() {
		// given
		setStartPostitionInGrid( //
				".849  .3." +
				"5..  4..9" +
				"... 3 ..6" +
				"84 .1.  7" +
				" 1 ... 2 " +
				"6  .7. 84" +
				"3.. 5 ..." +
				"7..3    5" +
				".5.  739.");

		// when
		solver.solve();

		// then
		assertThatGridIsSolved();
	}

	private void assertThatGridIsSolved() {
		grid.cells() //
			.map(Cell::getValue) //
			.peek((value) -> assertThat("Zelle nicht gefüllt ", value, isPresent()))
			.filter(Optional::isPresent) //
			.collect(Collectors.groupingBy(value->value)) //
			.forEach((value, list) -> assertThat("Falsche Länge bei Value " + value, list.size(), is(Game.ALL_NUMBERS.size())));
	}

	private void setStartPostitionInGrid(String startGrid) {
		int pos = 0;
		while (pos < Math.min(Game.COLS * Game.ROWS, startGrid.length())) {
			
			char charFromDefault = startGrid.charAt(pos);
			if (Character.isDigit(charFromDefault)) {
				int col = pos % Game.COLS;
				int row = pos / Game.ROWS;
				grid.getCell(col, row) //
					.setValue(Integer.parseUnsignedInt("" + charFromDefault)); // TODO stupid conversion
			}
			
			pos++;
		}
	}
}
