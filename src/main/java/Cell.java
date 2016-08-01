import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Cell {
	private Optional<Integer> value = Optional.empty();
	private final Set<Integer> possibles = new HashSet<Integer>(Game.ALL_NUMBERS);

	public Optional<Integer> getValue() {
		return value;
	}

	public Set<Integer> getPossibles() {
		return possibles;
	}

	public boolean isPossible(Integer number) {
		return possibles.contains(number);
	}

	public void removePossible(Integer numberToRemove) {
		possibles.remove(numberToRemove);
	}

	public void setValue(Integer value) {
		this.value = Optional.of(value);
		possibles.clear();
	}
}
