package hw3.puzzle;

public interface WorldState {
    /** Provides an estimate of the number of moves to reach the goal.
     * Must be less than or equal to the correct distance. */
    int estimatedDistanceToGoal();

    /** Provides an iterable of all the neighbors of this hw3.puzzle.WorldState. */
    /** Provides an iterable of all the neighbors of this WorldState. */
    Iterable<WorldState> neighbors();

    default boolean isGoal() {
        return estimatedDistanceToGoal() == 0;
    }
}
