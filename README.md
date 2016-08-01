A little dabbling in trying to write a Sudoku solver in a test-driven way.
Don't know if this will work, we will see.

The basic premise is that we'll do a lot more read operations (check the
current grid for numbers etc.) than write operations (enter a number into
the grid), so instead of computing everything all over again, the possible
numbers per cell will be stored in a Set in the cell.

Also I see a problem coming when there are no /easy/ solutions left (like
/this cell can only contain a 7/) and multiple cells have to be taken into
account to find the next match.  Perhaps it's still fast enough to just go
with trial and error then?  We'll see when we get there.