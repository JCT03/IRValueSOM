package learning.sentiment.learners;

import learning.core.Histogram;
import learning.decisiontree.RandomForest;
import learning.sentiment.core.SentimentAnalyzer;

public class SentimentForest15 extends RandomForest<Histogram<String>, String, String, Integer> {
    public SentimentForest15() {
        super(15, SentimentAnalyzer::allFeatures, Histogram::getCountFor, i -> i + 1);
    }
}
