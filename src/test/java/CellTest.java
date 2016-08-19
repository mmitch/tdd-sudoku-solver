import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CellTest {

	@Test
	public void newCellHasNoValue() {
		// given

		// when
		Cell cell = new Cell();

		// then
		assertThat(cell.getValue().isPresent(), is(false));
	}

	@Test
	public void newCellHasAllPossibleNumbers() {
		// given

		// when
		Cell cell = new Cell();

		// then
		assertThat(cell.getPossibles().toArray(), is(Game.ALL_NUMBERS.toArray()));
	}

	@Test
	public void removingAPossibleNumberAffectsNotOtherNumbers() {
		// given
		Cell cell = new Cell();
		Integer numberToRemove = 3;

		// when
		cell.removePossible(numberToRemove);

		// then
		for (Integer number : Game.ALL_NUMBERS) {
			assertThat(cell.isPossible(number), is(!number.equals(numberToRemove)));
		}
	}

	@Test
	public void settingAValueRemovesAllPossibleNumbers() {
		// given
		Cell cell = new Cell();
		Integer value = 7;

		// when
		cell.setValue(value);

		// then
		assertThat(cell.getPossibles().isEmpty(), is(true));
	}
}
