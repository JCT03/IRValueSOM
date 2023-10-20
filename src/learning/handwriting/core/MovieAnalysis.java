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
        File folderNeg = new File("src/learning/handwriting/core/aclImdb/train/CompleteFiles/DatasetNeg.txt");
        File folderPos = new File("src/learning/handwriting/core/aclImdb/train/CompleteFiles/DatasetPos.txt");
        Scanner sNeg = new Scanner(folderNeg);
        Scanner sPos = new Scanner(folderPos);
        ArrayList<Duple<Histogram<String>,String>> result = new ArrayList<>();
        while (sNeg.hasNextLine()) {
            String line = sNeg.nextLine();
            String score = "empty";
            if(line.contains("[//]")){
                score = line.substring(4);
            }
            String text = sNeg.nextLine();
            result.add(new Duple<>(bagOfWordsFrom(text), score));
        }
        return result;
    }
    public static Histogram<String> bagOfWordsFrom(String line) {
        Histogram<String> result = new Histogram<>();
        // YOUR CODE HERE
        // From https://www.delftstack.com/howto/java/how-to-remove-punctuation-from-string-in-java/#:~:text=Remove%20Punctuation%20From%20String%20Using%20the%20replaceAll%20%28%29,%5Cp%20%7BPunct%7D%2C%20which%20means%20all%20the%20punctuation%20symbols.
        line = line.replaceAll("\\p{Punct}", " ");
        for (String word: line.split("\\s")) {
            result.bump(word);
        }
        return result;
    }
}
