package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;

    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }

    @Override
    public L classify(V value) {
        // TODO: Find the distance from value to each element of data. Use Histogram.getPluralityWinner()
        //  to find the most popular label.
        Comparator<Duple<V, L>> dataComparator = new Comparator<Duple<V, L>>() { //https://www.callicoder.com/java-priority-queue/
        @Override
        public int compare(Duple<V, L> a, Duple<V, L> b) {
            if (distance.applyAsDouble(a.getFirst(), value) > distance.applyAsDouble(b.getFirst(), value)) {
                return 1;
            }
            else if (distance.applyAsDouble(a.getFirst(), value) < distance.applyAsDouble(b.getFirst(), value)) {
                return -1;
            }
            else {
                return 0;
            }
        }
        };
        PriorityQueue<Duple<V, L>> sortedV = new PriorityQueue<>(dataComparator);
        for (Duple<V, L> dataPoint: data) {
            sortedV.add(dataPoint);
        }
        Histogram<L> h = new Histogram<>();
        System.out.println("value " +value);
        for (int i = 0; i <k; i++) {
            System.out.println(sortedV.peek());
            h.bump(sortedV.poll().getSecond());
        }
        System.out.println(h);
        return h.getPluralityWinner();
    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        // TODO: Add all elements of training to data.
        for (Duple<V, L> dataPiece: training) {
            data.add(dataPiece);
        }
    }
}
