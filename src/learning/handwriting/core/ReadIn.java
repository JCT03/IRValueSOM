package learning.handwriting.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class ReadIn {
    public static void main(String[] args) throws IOException {
        File folderNeg = new File("src/learning/handwriting/core/aclImdb/train/sets/neg");
        File folderPos = new File("src/learning/handwriting/core/aclImdb/train/sets/pos");
        File[] listOfNegFiles = folderNeg.listFiles();
        File[] listOfPosFiles = folderPos.listFiles();
        System.out.println(listOfNegFiles.length);
        System.out.println(listOfPosFiles.length);
        FileWriter myWriter = new FileWriter("src/learning/handwriting/core/aclImdb/train/CompleteFiles/Dataset.txt");
        for (File file : listOfNegFiles) {
            if (file.isFile()) {
                Scanner s = new Scanner(file);
                myWriter.write("[//]" + file.getName().split("_")[1].charAt(0) + "\n");
                myWriter.write(s.nextLine() + "\n");
            }
        }
        myWriter.close();
        FileWriter myWriter2 = new FileWriter("src/learning/handwriting/core/aclImdb/train/CompleteFiles/Dataset.txt");
        for (File file : listOfPosFiles) {
            if (file.isFile()) {
                Scanner s = new Scanner(file);
                if(file.getName().split("_")[1].charAt(0) == '1')
                {
                    myWriter2.write("[//]" + "10" + "\n");
                }else {
                    myWriter2.write("[//]" + file.getName().split("_")[1].charAt(0) + "\n");
                }
                myWriter2.write(s.nextLine() + "\n");
            }
        }
        myWriter2.close();
    }
}
