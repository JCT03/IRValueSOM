package somgrid;

import java.text.DecimalFormat;

public class IRVector {
    double[] vector;

    public IRVector() {
        vector = new double[7];
    }

    public IRVector(double reading1, double reading2, double reading3, double reading4, double reading5, double reading6, double reading7) {
        vector = new double[] {reading1, reading2, reading3, reading4, reading5, reading6, reading7};
    }
    //array must be of length 7
    public IRVector(double[] readings) {
        vector = readings.clone();
    }

    public double get(int i) {
        return vector[i];
    }

    public void set(int i, double value) {
        vector[i] = value;
    }

    public static IRVector weightedAverageOf(IRVector v1, IRVector v2, double v1weight) {
        IRVector ret = new IRVector();
        for (int i = 0; i < 7; i++) {
            ret.set(i,v1.get(i)*v1weight+v2.get(i)*(1-v1weight));
        }
        return ret;
    }

    public double euclideanDistance(IRVector other) {
        double distance = 0;
        for (int i = 0; i <7; i++) {
            distance+= (vector[i]-other.get(i)) * (vector[i]-other.get(i));
        }
        return Math.sqrt(distance);
    }

    @Override
    public String toString() {
        DecimalFormat round = new DecimalFormat("0.00"); 
        String ret = "[";
        for (int i = 0; i <6; i++) {
            ret += round.format(vector[i]) + ", ";
        }
        return ret + round.format(vector[6]) + "]";
    }

}
