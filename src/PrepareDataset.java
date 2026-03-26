import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrepareDataset {
    public static void trainTestSplit(List<Observation> observations,
                                      List<Observation> testList,
                                      List<Observation> trainList ) {
        if (observations.isEmpty()) {
            throw new IllegalArgumentException("No observations given");
        }

        Map<String, ArrayList<Observation>> classesDivision = new HashMap<>();
        for( Observation obs : observations ) {
            String type = obs.getObservationType();
            classesDivision.computeIfAbsent(type, k -> new ArrayList<>()).add(obs);
        }

        for(Map.Entry<String, ArrayList<Observation>> entry : classesDivision.entrySet()) {
            var list = entry.getValue();
            int testSize = list.size() / 3;
            testList.addAll(list.subList(0, testSize));
            trainList.addAll(list.subList(testSize, list.size()));
        }
    }
}
