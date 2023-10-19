package learning.handwriting.core;

import core.AIReflector;
import core.Duple;
import learning.core.Assessment;
import learning.core.Classifier;
import learning.core.Histogram;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieAnalysis {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, FileNotFoundException, InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage: SentimentAnalyzer ClassifierClassName numDataSegments");
        } else {
            AIReflector<Classifier<Histogram<String>, String>> reflector = new AIReflector<>(Classifier.class, "learning.sentiment.learners");
            ArrayList<Duple<Histogram<String>, String>> sentimentStrings = openSentimentStrings("learning.handwriting.core.aclImdb.train");
            ArrayList<ArrayList<Duple<Histogram<String>, String>>> partitions = Assessment.partition(Integer.parseInt(args[1]), sentimentStrings);
            ArrayList<Assessment<String>> assessments = Assessment.multiTrial(() -> reflector.optionalInstanceOf(args[0]).get(), partitions);

        }
    }
    public static ArrayList<Duple<Histogram<String>,String>> openSentimentStrings(String filename) throws FileNotFoundException {
        Scanner s = new Scanner(filename);
        ArrayList<Duple<Histogram<String>,String>> result = new ArrayList<>();
        while (s.hasNextLine()) {
            //String[] label_text = s.
            //result.add(new Duple<>(bagOfWordsFrom(label_text[1]), label_text[0]));
        }
        return result;
    }
}
