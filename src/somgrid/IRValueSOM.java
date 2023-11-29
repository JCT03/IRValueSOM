package somgrid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import learning.som.SelfOrgMap;

public class IRValueSOM {
    SelfOrgMap<IRVector> IRMap;
    double max;
    public IRValueSOM(int sides) {
        max = 0;
        IRMap = new SelfOrgMap<>(sides, () -> new IRVector(), IRVector::euclideanDistance, IRVector::weightedAverageOf);
        BufferedReader reader;
        ArrayList<IRVector> vectors = new ArrayList<>();
        try {
			reader = new BufferedReader(new FileReader("src/somgrid/IRData.txt"));
			String line = reader.readLine();
			while (line != null) {
				double[] input = new double[7];
                int endIndex = line.indexOf(',');
                input[0] = Double.parseDouble(line.substring(1,endIndex));
                line = line.substring(endIndex+1);
                for (int i = 1; i < 6; i++) {
                    endIndex = line.indexOf(',');
                    input[i] = Double.parseDouble(line.substring(0,endIndex));
                    line = line.substring(endIndex+1);
                }
                endIndex = line.indexOf(']');
                input[6] = Double.parseDouble(line.substring(0,endIndex));
                vectors.add(new IRVector(input));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        Collections.shuffle(vectors);
        for (int i = 0; i < vectors.size(); i++) {
            IRMap.train(vectors.get(i));
        }
        for (int x = 0; x < sides; x++) {
            for (int y = 0; y < sides; y++) {
                for (int i = 0; i < 7; i++) {
                    if (IRMap.getNode(x,y).get(i) > max) {
                        max = IRMap.getNode(x,y).get(i);
                    }
                }
            }
        }
    }
    public double getValue(int x, int y, int IRNum) {
        return IRMap.getNode(x,y).get(IRNum);
    }
    public double getMax() {
        return max;
    }
}