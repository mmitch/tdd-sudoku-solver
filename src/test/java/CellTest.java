import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
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

	@Test
	public void cloneDoesDeepCloneOfPossibles() throws CloneNotSupportedException {
		// given
		Cell original = new Cell();

		// when
		Cell clone = original.clone();

		// then
		assertThat(clone.getPossibles(), containsInAnyOrder(original.getPossibles().toArray()));
		System.out.println(original.getPossibles().hashCode());
		System.out.println(clone.getPossibles().hashCode());
		assertThat(clone.getPossibles(), not(sameInstance(original.getPossibles())));
	}

	@Test
	public void cloneClonesMissingValue() throws CloneNotSupportedException {
		// given
		Cell original = new Cell();

		// when
		Cell clone = original.clone();

		// then
		assertThat(clone.getValue(), equalTo(original.getValue()));
		assertThat(clone.getValue(), sameInstance(original.getValue())); // both Optional.empty()
	}

	@Test
	public void cloneDoesDeepCloneOfValue() throws CloneNotSupportedException {
		// given
		Cell original = new Cell();
		original.setValue(5);

		// when
		Cell clone = original.clone();

		// then
		assertThat(clone.getValue(), equalTo(original.getValue()));
		assertThat(clone.getValue(), not(sameInstance(original.getValue())));
	}

}
