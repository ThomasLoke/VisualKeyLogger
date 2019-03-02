package util.problem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class ProblemStore {
    
    private static final int INITIAL_CAPACITY = 10;
    
    public enum Level {
        Error, Warning, Info;
    }

    public static class Problem {
        
        public final Level level;
        public final @NonNull String description;
        public final @Nullable Throwable t;
        
        public Problem(Level level, String description) {
            this(level, description, null);
        }
        
        public Problem(Level level, @NonNull Throwable t) {
            this(level, t.getMessage(), t);
        }
        
        public Problem(Level level, @NonNull String description, @Nullable Throwable t) {
            this.level = level;
            this.description = description;
            this.t = t;
        }
        
        @Override public String toString() {
            return String.format("[%s] %s%s", level, description, t == null ? "" : (": " + t.getMessage()));
        }
        
    }
    
    private final List<Problem> problems;
    private final Map<Level, List<Integer>> indicesForLevel;
    
    public ProblemStore() {
        this(INITIAL_CAPACITY);
    }
    
    public ProblemStore(int initialCapacity) {
        problems = new ArrayList<>(initialCapacity);
        indicesForLevel = new HashMap<>();
        for (Level level : Level.values()) {
            indicesForLevel.put(level, new ArrayList<>());
        }
    }
    
    public synchronized void clear() {
        problems.clear();
        indicesForLevel.values().forEach(Collection::clear);
    }
    
    public void addError(String description, @Nullable Throwable t) {
        addProblem(Level.Error, description, t);
    }
    
    public void addWarning(String description) {
        addProblem(Level.Warning, description, null);
    }
    
    public void addInfo(String description) {
        addProblem(Level.Info, description, null);
    }
    
    public synchronized void addProblem(Level level, String description, @Nullable Throwable t) {
        problems.add(new Problem(level, description, t));
        addIndexForLevel(level, problems.size() - 1);
    }
    
    private synchronized void addIndexForLevel(Level level, int index) {
        List<Integer> indices = indicesForLevel.get(level);
        indices.add(index);
    }
    
    public synchronized List<Problem> getProblems(Level level) {
        List<Integer> indices = indicesForLevel.get(level);
        return indices.stream().map(idx -> problems.get(idx)).collect(Collectors.toList());
    }
    
    public boolean isEmpty(Level level) {
        return getProblems(level).isEmpty();
    }
    
    public boolean hasNoErrors() {
        return isEmpty(Level.Error);
    }
    
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        problems.forEach(p -> sb.append(p.toString()));
        return sb.toString();
    }
}
