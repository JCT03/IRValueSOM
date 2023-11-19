
public class IRVector {
    double[] vector;

    public IRVector() {
        vector = new double[7];
    }

    public IRVector(double reading1, double reading2, double reading3, double reading4, double reading5, double reading6, double reading7) {
        vector = new double[] {reading1, reading2, reading3, reading4, reading5, reading6, reading7};
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
            ret.set(i,v1.get(i)*v1weight+v2.get(i));
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

    public boolean equals(IRVector other) {
        if (other instanceof IRVector that) {
            for (int i = 0; i < 7; i++) {
                if (this.vector[i] != that.vector[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String toString() {
        return ("" + vector);
    }

}
