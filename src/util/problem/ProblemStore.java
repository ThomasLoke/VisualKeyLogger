package util.problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemStore {
    
    private static final int INITIAL_CAPACITY = 10;
    
    public enum Level {
        Error, Warning, Info;
    }

    public static class Problem {
        
        public final String description;
        public final Throwable t;
        
        public Problem(String description) {
            this(description, null);
        }
        
        public Problem(Throwable t) {
            this(t.getMessage(), t);
        }
        
        public Problem(String description, Throwable t) {
            this.description = description;
            this.t = t;
        }
        
        @Override public String toString() {
            return description;
        }
        
    }
    
    private final Map<Level, List<Problem>> problems;
    
    public ProblemStore() {
        this(INITIAL_CAPACITY);
    }
    
    public ProblemStore(int initialCapacity) {
        problems = new HashMap<>();
        for (Level level : Level.values()) {
            problems.put(level, new ArrayList<>(initialCapacity));
        }
    }
    
    public void clear() {
        problems.forEach((l, c) -> c.clear());
    }
    
    public void addError(String description, Throwable t) {
        addProblem(Level.Error, description, t);
    }
    
    public void addProblem(Level level, String description, Throwable t) {
        List<Problem> problems = getProblems(level);
        problems.add(new Problem(description, t));
    }
    
    /** Returns the backing list -- make a copy if you don't want to unintentionally modify the contents of the ProblemStore */
    public List<Problem> getProblems(Level level) {
        return problems.get(level);
    }
    
    public boolean isEmpty(Level level) {
        return getProblems(level).isEmpty();
    }
    
    public boolean hasNoErrors() {
        return isEmpty(Level.Error);
    }
    
}
