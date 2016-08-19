import java.util.Arrays;
import java.util.Collection;

public interface Game {

	Collection<Integer> ALL_NUMBERS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
	int ROWS = ALL_NUMBERS.size();
	int COLS = ALL_NUMBERS.size();
	int BLOCK_ROWS = 3;
	int BLOCK_COLS = 3;

}
