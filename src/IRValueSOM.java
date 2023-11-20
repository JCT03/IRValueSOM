import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import learning.som.SelfOrgMap;

public class IRValueSOM {
    public static void main(String[] args) {
        SelfOrgMap<IRVector> IRMap = new SelfOrgMap<>(6, () -> new IRVector(), IRVector::euclideanDistance, IRVector::weightedAverageOf);
        BufferedReader reader;
        
        try {
			reader = new BufferedReader(new FileReader("src/IRData.txt"));
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
                IRMap.train(new IRVector(input));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println(IRMap);
    }
}