import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static String activeClass = "setosa";
    private static String disactiveClass = "versicolor";
    private static boolean ignoreLine = true;
    private static final int epoch = 10;

    public static void main(String[] args) {
        List<Observation> data = uploadTestFile();

        data.removeIf(o ->
                !o.getObservationType().equals("setosa") && !o.getObservationType().equals("versicolor")
        );

        List<Observation> train = new ArrayList<>();
        List<Observation> test = new ArrayList<>();
        PrepareDataset.trainTestSplit(data,test,train);

        ArrayList<Double> weights = new ArrayList<>(List.of(0.0, 0.0, 0.0, 0.0));
        Perceptron perceptron = new Perceptron(weights, 0, 0.5, 0.5, activeClass, disactiveClass);

        for(int i = 0; i < epoch ; i++) {
            perceptron.train(train, getListOfExpectedClasses(train));
            double performace = testPerceptronPerformance(perceptron, test);
            System.out.println( "Epoch: "+ perceptron.getEpochCounter() + " | Accuracy: " + performace);

            if(performace == 1.0)
                break;
        }
        System.out.println("Training completed.");
        exportWeightsToFile(perceptron, "weights.csv");

        Scanner s = new Scanner(System.in);
        System.out.println("Input values to predict: (coma separated)\n");
        while (true){
            String inputStr = s.nextLine();
            try{
                List<Double> input = parseInputValues(inputStr);
                String res = parsePrediction(perceptron.predict(input));
                System.out.println("Prediction: " + res);
            }catch (Exception e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void exportWeightsToFile(Perceptron perceptron, String filePath) {
        List<Double> weights = perceptron.getWeights();
        double b = perceptron.getThreshold();

        String header = IntStream.range(0, weights.size())
                .mapToObj(i -> "w" + (i + 1))
                .collect(Collectors.joining(","))
                + ",b";

        String values = Stream.concat(
                        weights.stream().map(String::valueOf),
                        Stream.of(String.valueOf(b))
                )
                .collect(Collectors.joining(","));

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write(header);
            writer.newLine();
            writer.write(values);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parsePrediction(int prediction){
        return switch (prediction) {
            case 0 -> disactiveClass;
            case 1 -> activeClass;
            default -> null;
        };
    }

    private static List<Double> parseInputValues(String inputStr){
        inputStr = inputStr.trim();

        String[] inputArr =  inputStr.split(",");
        Double[] inputValues = new Double[inputArr.length];
        for(int i = 0; i < inputArr.length; i++){
            inputValues[i] = Double.parseDouble(inputArr[i]);
        }
        return new ArrayList<>(List.of(inputValues));
    }
    private static double testPerceptronPerformance(Perceptron perceptron, List<Observation> dataSet) {
        List<String> expectedClasses = new ArrayList<>();
        List<String> actualClasses = new ArrayList<>();
        for(Observation observation : dataSet){
            String prediction = perceptron.predict(observation.getAttributes()) == 1 ? perceptron.getActiveClass() : perceptron.getDisactiveClass();
            actualClasses.add(prediction);
            expectedClasses.add(observation.getObservationType());
        }
        return EvaluationMetrics.measureAccuracy(expectedClasses, actualClasses);
    }
    private static List<String> getListOfExpectedClasses(List<Observation> observations) {
        List<String> expectedClasses = new ArrayList<>();
        for (Observation observation : observations) {
            expectedClasses.add(observation.getObservationType());
        }
        return expectedClasses;
    }
    private static List<Observation> uploadTestFile() {
        List<Observation> data = new ArrayList<>();
        String path = "iris.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (ignoreLine) {
                    ignoreLine = false;
                    continue;
                }
                String[] parts = line.split(",");

                Observation observation = new Observation(new ArrayList<>(List.of(
                        Double.parseDouble(parts[0].trim()),
                        Double.parseDouble(parts[1].trim()),
                        Double.parseDouble(parts[2].trim()),
                        Double.parseDouble(parts[3].trim())
                )), parts[4].trim());

                data.add(observation);
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return data;
    }
}
