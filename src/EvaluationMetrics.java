import java.util.List;

public class EvaluationMetrics {
    public static double measureAccuracy(List<String> real, List<String> predicted) {
        if (real.size() != predicted.size())
            throw new IllegalArgumentException("Size mismatch");

        double accuracy = 0;
        double n = real.size();
        for (int i = 0; i < n; i++) {
            if(real.get(i).equals(predicted.get(i)))
                accuracy++;
        }
        return accuracy/n;
    }
}
