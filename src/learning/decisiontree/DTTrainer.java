package learning.decisiontree;

import core.Duple;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DTTrainer<V,L, F, FV extends Comparable<FV>> {
	private ArrayList<Duple<V,L>> baseData;
	private boolean restrictFeatures;
	private Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures;
	private BiFunction<V,F,FV> getFeatureValue;
	private Function<FV,FV> successor;
	
	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 boolean restrictFeatures, BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		baseData = data;
		this.restrictFeatures = restrictFeatures;
		this.allFeatures = allFeatures;
		this.getFeatureValue = getFeatureValue;
		this.successor = successor;
	}
	
	public DTTrainer(ArrayList<Duple<V, L>> data, Function<ArrayList<Duple<V,L>>, ArrayList<Duple<F,FV>>> allFeatures,
					 BiFunction<V,F,FV> getFeatureValue, Function<FV,FV> successor) {
		this(data, allFeatures, false, getFeatureValue, successor);
	}

	// TODO: Call allFeatures.apply() to get the feature list. Then shuffle the list, retaining
	//  only targetNumber features. Should pass DTTest.testReduced().
	public static <V,L, F, FV  extends Comparable<FV>> ArrayList<Duple<F,FV>>
	reducedFeatures(ArrayList<Duple<V,L>> data, Function<ArrayList<Duple<V, L>>, ArrayList<Duple<F,FV>>> allFeatures,
					int targetNumber) {
			ArrayList<Duple<F,FV>> features = allFeatures.apply(data);
			Collections.shuffle(features);
			for (int i = features.size(); i > targetNumber; i--) {
				features.remove(i-1);
			}
		return features;
    }
	
	public DecisionTree<V,L,F,FV> train() {
		return train(baseData);
	}

	public static <V,L> int numLabels(ArrayList<Duple<V,L>> data) {
		return data.stream().map(Duple::getSecond).collect(Collectors.toUnmodifiableSet()).size();
	}
	
	private DecisionTree<V,L,F,FV> train(ArrayList<Duple<V,L>> data) {
		// TODO: Implement the decision tree learning algorithm
		if (numLabels(data) == 1) {
			// TODO: Return a leaf node consisting of the only label in data
			return new DTLeaf<V,L,F,FV>(data.get(0).getSecond());
		} else {
			// TODO: Return an interior node.
			//  If restrictFeatures is false, call allFeatures.apply() to get a complete list
			//  of features and values, all of which you should cosider when splitting.
			//  If restrictFeatures is true, call reducedFeatures() to get sqrt(# features)
			//  of possible features/values as candidates for the split. In either case,
			//  for each feature/value combination, use the splitOn() function to break the
			//  data into two parts. Then use gain() on each split to figure out which
			//  feature/value combination has the highest gain. Use that combination, as
			//  well as recursively created left and right nodes, to create the new
			//  interior node.
			//  Note: It is possible for the split to fail; that is, you can have a split
			//  in which one branch has zero elements. In this case, return a leaf node
			//  containing the most popular label in the branch that has elements.
			ArrayList<Duple<F,FV>> features = null;
			ArrayList<Duple<F,FV>> all = allFeatures.apply(data);
			int numFeatures = all.size();
			if (!restrictFeatures) {
				features = all;
			}
			else {
				features = reducedFeatures(data, allFeatures, (int) Math.sqrt(numFeatures));
			}
			double bestGain = 0;
			Duple<F,FV> bestCombo = null;
			ArrayList<Duple<V,L>> bestLeft = null;
			ArrayList<Duple<V,L>> bestRight = null;
			for (Duple<F,FV> combo: features) {
				Duple<ArrayList<Duple<V, L>>, ArrayList<Duple<V, L>>> children =splitOn(data, combo.getFirst(), combo.getSecond(),getFeatureValue);
				double thisGain = gain(data,children.getFirst(),children.getSecond());
				if (thisGain > bestGain) {
					bestGain= thisGain;
					bestCombo = combo;
					bestLeft = children.getFirst();
					bestRight = children.getSecond();
				}
			}
			System.out.println("data");
			System.out.println("left" + bestLeft);
			System.out.println("right" + bestRight);
			if ((bestLeft == null) || (bestLeft.size() ==0)) {
				return new DTLeaf<V,L,F,FV>(mostPopularLabelFrom(bestRight));
			}
			else if ((bestRight == null) || (bestRight.size() ==0)) {
				return new DTLeaf<V,L,F,FV>(mostPopularLabelFrom(bestLeft));
			}
			return new DTInterior<V,L,F,FV>(bestCombo.getFirst(), bestCombo.getSecond(), train(bestLeft), train(bestRight), getFeatureValue, successor);
		}		
	}

	public static <V,L> L mostPopularLabelFrom(ArrayList<Duple<V, L>> data) {
		Histogram<L> h = new Histogram<>();
		for (Duple<V,L> datum: data) {
			h.bump(datum.getSecond());
		}
		return h.getPluralityWinner();
	}

	// TODO: Generates a new data set by sampling randomly with replacement. It should return
	//    an `ArrayList` that is the same length as `data`, where each element is selected randomly
	//    from `data`. Should pass `DTTest.testResample()`.
	public static <V,L> ArrayList<Duple<V,L>> resample(ArrayList<Duple<V,L>> data) {
		ArrayList<Duple<V,L>> randomSample = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			int randIndex = (int) (Math.random() * (data.size()-1));
			randomSample.add(data.get(randIndex));
		}
		return randomSample;
	}

	public static <V,L> double getGini(ArrayList<Duple<V,L>> data) {
		// TODO: Calculate the Gini coefficient:
		//  For each label, calculate its portion of the whole (p_i).
		//  Use of a Histogram<L> for this purpose is recommended.
		//  Gini coefficient is 1 - sum(for all labels i, p_i^2)
		//  Should pass DTTest.testGini().
		Histogram<L> LHist = new Histogram<>();
		for (Duple<V,L> dataEntry : data) {
			LHist.bump(dataEntry.getSecond());
		}
		double sum = 0;
		for (L label : LHist) {
			sum += LHist.getPortionFor(label) * LHist.getPortionFor(label);
		}
		return 1-sum;
	}

	public static <V,L> double gain(ArrayList<Duple<V,L>> parent, ArrayList<Duple<V,L>> child1,
									ArrayList<Duple<V,L>> child2) {
		// TODO: Calculate the gain of the split. Add the gini values for the children.
		//  Subtract that sum from the gini value for the parent. Should pass DTTest.testGain().
		return getGini(parent) - getGini(child1) - getGini(child2);
	}

	public static <V,L, F, FV  extends Comparable<FV>> Duple<ArrayList<Duple<V,L>>,ArrayList<Duple<V,L>>> splitOn
			(ArrayList<Duple<V,L>> data, F feature, FV featureValue, BiFunction<V,F,FV> getFeatureValue) {
		// TODO:
		//  Returns a duple of two new lists of training data.
		//  The first returned list should be everything from this set for which
		//  feature has a value less than or equal to featureValue. The second
		//  returned list should be everything else from this list.
		//  Should pass DTTest.testSplit().
		ArrayList<Duple<V,L>> lessEqual = new ArrayList<>();
		ArrayList<Duple<V,L>> more = new ArrayList<>();
		for (Duple<V,L> dataEntry: data) {
			if (getFeatureValue.apply(dataEntry.getFirst(), feature).compareTo(featureValue) <= 0) {
				lessEqual.add(dataEntry);
			}
			else {
				more.add(dataEntry);
			}
		}
		return new Duple<ArrayList<Duple<V,L>>,ArrayList<Duple<V,L>>>(lessEqual, more);
	}
}
