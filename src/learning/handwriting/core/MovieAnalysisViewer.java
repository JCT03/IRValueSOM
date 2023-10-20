package learning.handwriting.core;

import core.AIReflector;
import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;
import learning.handwriting.gui.DrawingEditor;
import learning.sentiment.core.Sentence;
import learning.sentiment.core.SentimentAnalyzer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class MovieAnalysisViewer extends JFrame {
    public static void main(String[] args) {
        MovieAnalysisViewer gui = new MovieAnalysisViewer();
        gui.setVisible(true);
    }

    private JMenuItem open, train, assess;
    private JTextArea message;
    private JTextField sentiment, progress;
    private JButton classify;
    private JComboBox learner;
    private Classifier<Histogram<String>,String> ai;
    private AIReflector<Classifier<Histogram<String>, String>> finder;
    private JFrame assessmentFrame = new JFrame();
    private JFileChooser chooser;

    public MovieAnalysisViewer() {
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        finder = new AIReflector<>(Classifier.class, "learning.sentiment.learners");

        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        open = new JMenuItem("Open");
        file.add(open);
        bar.add(file);
        JMenu learnerMenu = new JMenu("Learner");
        train = new JMenuItem("Train");
        train.addActionListener(e -> trainAI());
        learnerMenu.add(train);
        assess = new JMenuItem("Assess");
        assess.addActionListener(e -> {
            getSentimentStrings().ifPresent(sentimentStrings -> {
                DrawingEditor.assess(sentimentStrings, assessmentFrame, finder, learner.getSelectedItem().toString(), progress);
            });
        });
        learnerMenu.add(assess);
        bar.add(learnerMenu);
        setJMenuBar(bar);

        chooser = new JFileChooser();

        setLayout(new FlowLayout());
        JLabel msg = new JLabel("Enter statement to classify:");
        add(msg);
        message = new JTextArea(10, 40);
        message.setEditable(true);
        add(message);
        classify = new JButton("Classify");
        classify.addActionListener(e -> {
            if (ai == null) {
                JOptionPane.showMessageDialog(MovieAnalysisViewer.this, "Train an AI first");
            } else {
                sentiment.setText(ai.classify(SentimentAnalyzer.bagOfWordsFrom(message.getText())));
            }
        });
        add(classify);
        sentiment = new JTextField(20);
        sentiment.setEditable(false);
        add(sentiment);
        learner = new JComboBox();
        for (String type: finder.getTypeNames()) {
            learner.addItem(type);
        }
        add(learner);

        progress = new JTextField(20);
        progress.setEditable(false);
        add(progress);
    }

    public static ArrayList<Duple<Histogram<String>,String>> openSentimentStrings() throws FileNotFoundException {
        System.out.println("Testing things");
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
    private Optional<ArrayList<Duple<Histogram<String>,String>>> getSentimentStrings() {
        int choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            try {
                return Optional.of(openSentimentStrings());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(MovieAnalysisViewer.this, "Exception: " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    void trainAI() {
        getSentimentStrings().ifPresent(sentimentStrings -> {
            try {
                ai = finder.newInstanceOf(learner.getSelectedItem().toString());
                new Thread(() -> {
                    Timer timer = DrawingEditor.createProgressTimer(progress);
                    try {
                        timer.start();
                        ai.train(sentimentStrings);
                        timer.stop();
                        JOptionPane.showMessageDialog(MovieAnalysisViewer.this, "Training complete");
                    } catch (Exception | StackOverflowError exc) {
                        timer.stop();
                        progress.setText("Error at:" + progress.getText());
                        JOptionPane.showMessageDialog(MovieAnalysisViewer.this, "Exception: " + exc.getMessage());
                    }
                }).start();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MovieAnalysisViewer.this, "Exception: " + ex.getMessage());
            }
        });
    }
}
