import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class Cell implements Cloneable {
	private Optional<Integer> value = Optional.empty();
	private Set<Integer> possibles = new HashSet<Integer>(Game.ALL_NUMBERS);

	// initialization only to keep getAllNeighbours() happy when testing Cells outside of a Grid
	private Set<Cell> row = new HashSet<>();
	private Set<Cell> column = new HashSet<>();
	private Set<Cell> block = new HashSet<>();

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
		getAllNeighbours().forEach(neighbour -> neighbour.removePossible(value));
	}

	public Cell clone() throws CloneNotSupportedException {
		Cell clone = (Cell) super.clone();
		clone.possibles = new HashSet<>(getPossibles());
		clone.value = Optional.ofNullable(getValue().orElse(null));
		return clone;
	}

	public Set<Cell> getRow() {
		return row;
	}

	public void setRow(Set<Cell> row) {
		this.row = row;
	}

	public Set<Cell> getColumn() {
		return column;
	}

	public void setColumn(Set<Cell> column) {
		this.column = column;
	}

	public Set<Cell> getBlock() {
		return block;
	}

	public void setBlock(Set<Cell> block) {
		this.block = block;
	}

	private Stream<Cell> getAllNeighbours() {
		return Stream.concat(row.stream(), Stream.concat(column.stream(), block.stream())) //
				.filter(cell -> cell != this);
	}
}
