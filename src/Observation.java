import java.util.ArrayList;

public class Observation {
    private final ArrayList<Double> attributes;
    private final String observationType;

    public Observation(ArrayList<Double> attributes, String observationType) {
        this.attributes = attributes;
        this.observationType = observationType;
    }
    public Observation(ArrayList<Double> attributes) {
        this(attributes, "observationType");
    }

    public ArrayList<Double> getAttributes() {
        return attributes;
    }
    public String getObservationType() {
        return observationType;
    }
}
 