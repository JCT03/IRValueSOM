package search.bestfirst;
import search.SearchNode;
import search.SearchQueue;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;
import java.util.Comparator;
import java.util.HashMap;

public class BestFirstQueue<T> implements SearchQueue<T> {
    // HINT: Use java.util.PriorityQueue. It will really help you.
    private PriorityQueue<SearchNode<T>> queue;
    private HashMap<Object,Integer> visited;
    private ToIntFunction<T> heuristic;

    public BestFirstQueue(ToIntFunction<T> heuristic) {
        this.heuristic = heuristic;
        Comparator<SearchNode<T>> nodeComparator = new Comparator<SearchNode<T>>() { //https://www.callicoder.com/java-priority-queue/
            @Override
            public int compare(SearchNode<T> a, SearchNode<T> b) {
                return (heuristic.applyAsInt(a.getValue())+a.getDepth()) - (heuristic.applyAsInt(b.getValue())+a.getDepth());
            }
        };
        queue = new PriorityQueue<>(nodeComparator);
        visited = new HashMap<>();

    }

    @Override
    public void enqueue(SearchNode<T> node)  {
        int newEstimate = heuristic.applyAsInt(node.getValue())+node.getDepth() ;
        if (!visited.containsKey(node.getValue())|| visited.get(node.getValue()) > newEstimate)  {
            queue.add(node);
            visited.put(node.getValue(), newEstimate);
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        if (queue.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(queue.remove());
        }
    }
}