import java.util.Arrays;

public class Movie {
    private String name;
    private double[] traits;

    public Movie(String name, double[] traits) {
        this.name = name;
        this.traits = traits;
    }

    public String getName() {
        return name;
    }

    public double[] getTraits() {
        return traits;
    }

    @Override
    public String toString() {
        return name + " " + Arrays.toString(traits);
    }
}