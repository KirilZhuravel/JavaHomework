import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {

    public static double calculateSimilarity(double[] vec1, double[] vec2, double[] weightVec) {
        if (vec1 == null || vec2 == null || weightVec == null) {
            throw new IllegalArgumentException("Input arrays cannot be null");
        }
        if (vec1.length == 0 || vec1.length != vec2.length || vec1.length != weightVec.length) {
            throw new IllegalArgumentException("Array lengths must match and be non-empty");
        }

        double numerator = 0.0;
        double denomA = 0.0;
        double denomB = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            double w = weightVec[i];
            numerator += vec1[i] * vec2[i] * w;
            denomA += Math.pow(vec1[i], 2) * w;
            denomB += Math.pow(vec2[i], 2) * w;
        }

        if (denomA == 0.0 || denomB == 0.0) {
            return 0.0;
        }

        return numerator / (Math.sqrt(denomA) * Math.sqrt(denomB));
    }

    public List<Movie> getRecommendations(List<Movie> inputList, double[] queryTraits, double[] weightVec, int limit) {
        Map<Movie, Double> resultMap = new HashMap<>();

        for (Movie m : inputList) {
            double simVal = calculateSimilarity(queryTraits, m.getTraits(), weightVec);
            resultMap.put(m, simVal);
        }

        return inputList.stream()
                .sorted((a, b) -> Double.compare(resultMap.get(b), resultMap.get(a)))
                .limit(limit)
                .collect(Collectors.toList());
    }
}