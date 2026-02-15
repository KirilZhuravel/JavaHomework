import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecommendationEngineTest {

    @Test
    void testIdenticalVectors() {
        double[] vecA = {1.0, 2.0, 3.0};
        double[] vecB = {1.0, 2.0, 3.0};
        double[] w = {1.0, 1.0, 1.0};

        assertEquals(1.0, RecommendationEngine.calculateSimilarity(vecA, vecB, w), 0.0001);
    }

    @Test
    void testOrthogonalVectors() {
        double[] vecA = {1.0, 0.0};
        double[] vecB = {0.0, 1.0};
        double[] w = {1.0, 1.0};

        assertEquals(0.0, RecommendationEngine.calculateSimilarity(vecA, vecB, w), 0.0001);
    }

    @Test
    void testWeightedSimilarity() {
        double[] vecA = {1.0, 1.0};
        double[] vecB = {1.0, 0.0};
        double[] w = {0.5, 0.5};

        assertEquals(0.7071, RecommendationEngine.calculateSimilarity(vecA, vecB, w), 0.01);
    }

    @Test
    void testLengthMismatch() {
        double[] vecA = {1.0, 2.0};
        double[] vecB = {1.0, 2.0, 3.0};
        double[] w = {1.0, 1.0};

        assertThrows(IllegalArgumentException.class, () -> {
            RecommendationEngine.calculateSimilarity(vecA, vecB, w);
        });
    }
}