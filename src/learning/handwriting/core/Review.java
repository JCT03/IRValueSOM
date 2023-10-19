package learning.handwriting.core;

import java.util.ArrayList;

public class Review {
    private int score;
    private ArrayList<String> words;

    public Review(int Score, ArrayList<String> Words){
        score = Score;
        words = Words;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public Review(ArrayList<String> Words){
        words = Words;
    }

    public int distance(Review review){
        int finalDist = 0;
        ArrayList<String> other = review.getWords();
        for(String word : words){
            if(!other.contains(word)){
                finalDist +=1;
            }
        }
        return finalDist;
    }
}
