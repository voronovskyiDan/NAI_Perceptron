import java.util.ArrayList;
import java.util.List;

public class Perceptron {
    private int epochCounter;
    private ArrayList<Double> weights;
    private double threshold;

    public Perceptron( ArrayList<Double> weights, double threshold) {
        epochCounter = 0;
        this.weights = weights;
        this.threshold = threshold;
    }

    public int predict(List<Double> input) {
        if (input.size() != weights.size())
            throw new IllegalArgumentException();

        double net = 0.0;
        for (int i = 0; i < weights.size(); i++) {
            net += weights.get(i) * input.get(i);
        }
        net -= threshold;

        if(net >= 0)
            return 1;
        return 0;
    }
}
 