import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solver {

	private Grid grid;

	public Solver(Grid grid) {
		this.grid = grid;
	}

	public void solve() {
		checkAllRows();
		checkAllColumns();
		checkAllBlocks();
	}

	private void checkAllColumns() {
		grid.getColumns().forEach(this::checkCellSet);
	}

	private void checkAllRows() {
		grid.getRows().forEach(this::checkCellSet);
	}

	private void checkAllBlocks() {
		grid.getBlocks().forEach(this::checkCellSet);
	}
	
	private void checkCellSet(Set<Cell> cells) {
		checkCellSetForDeterminedValues(cells);
		checkCellSetForUniquePossibilities(cells);
	}

	private void checkCellSetForDeterminedValues(Set<Cell> cells) {
		cells.stream() //
			.filter(cell -> cell.getPossibles().size() == 1) //
			.forEach(cell -> setValueAndCheckRecursively(cell, cell.getPossibles().iterator().next()));
	}

	private void checkCellSetForUniquePossibilities(Set<Cell> cells) {
		Stream<Integer> uniquePossibilities = cells.stream() //
			.map(Cell::getPossibles) //
			.flatMap(Set::stream) //
			.collect(Collectors.groupingBy(value -> value)) //
			.values() //
			.stream() //
			.filter(valueSet -> valueSet.size() == 1) //
			.flatMap(valueSet -> valueSet.stream());
		
		uniquePossibilities //
			.forEach(value -> cells.stream() //
					.filter(cell -> cell.isPossible(value))
					.forEach(cell -> setValueAndCheckRecursively(cell, value)) //
					);
	}

	private void setValueAndCheckRecursively(Cell cell, Integer value) {
		cell.setValue(value);
		checkCellSet(cell.getRow());
		checkCellSet(cell.getColumn());
		checkCellSet(cell.getBlock());
	}

}
