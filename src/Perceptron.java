import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class Perceptron {
    private int epochCounter;
    private int dimension;
    private String activeClass;
    private String disactiveClass;

    private List<Double> weights;
    private double threshold;
    private double alpha;
    private double beta;

    public Perceptron(List<Double> weights, double threshold, double alpha, double beta, String activeClass, String disactiveClass ) {
        epochCounter = 0;
        this.weights = weights;
        this.dimension = weights.size();
        this.threshold = threshold;
        this.alpha = alpha;
        this.beta = beta;

        this.activeClass = activeClass;
        this.disactiveClass = disactiveClass;
    }

    public void train(List<Observation> inputs, List<String> labels) {
        if (inputs.size() != labels.size()) {
            System.out.println("Error: inputs and labels do not match");
        }
        for(int i = 0; i < inputs.size(); i++) {
            Observation observation = inputs.get(i);
            String desiredLabel = labels.get(i);

            int result = predict(observation.getAttributes());
            String resultLabel = result == 1 ? activeClass : disactiveClass;

            if(!resultLabel.equals(desiredLabel)){
                int desired = desiredLabel.equals(activeClass) ? 1 : 0;
                applyDeltaRule(observation,result,desired);
            }
        }

        epochCounter++;
    }

    private void applyDeltaRule(Observation X, int result, int desired) {
        List<Double> vectorX = X.getAttributes();
        for(int i = 0; i < dimension; i++) {
            weights.set(i, weights.get(i) + (desired - result) * vectorX.get(i) * alpha);
        }
        threshold = threshold - (desired - result) * beta;
    }

    public int predict(List<Double> inputs) {
        if (inputs.size() != dimension)
            throw new IllegalArgumentException("Input dimensionality is incorrect");

        double net = 0.0;
        for (int i = 0; i < dimension; i++) {
            net += weights.get(i) * inputs.get(i);
        }
        net -= threshold;

        if(net >= 0)
            return 1;
        return 0;
    }

    public int getEpochCounter() {
        return epochCounter;
    }
    public String getActiveClass() {
        return activeClass;
    }
    public String getDisactiveClass() {
        return disactiveClass;
    }
    public List<Double> getWeights() {
        return weights;
    }
    public double getThreshold() {
        return threshold;
    }
}
 