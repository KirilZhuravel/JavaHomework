import java.io.*;
import java.util.*;

public class Main {
    private static List<Movie> movieDb = new ArrayList<>();
    private static double[] globalWeights;
    private static String[] columnHeaders;
    private static final String SOURCE_FILE = "src/movies.csv";

    public static void main(String[] args) {
        if (!initializeDb()) {
            return;
        }

        RecommendationEngine engine = new RecommendationEngine();
        Scanner inputScan = new Scanner(System.in);
        boolean isActive = true;

        while (isActive) {
            System.out.println("\n--- Recommendation System ---");
            System.out.println("1. Match by Movie");
            System.out.println("2. Match by Preferences");
            System.out.println("3. Exit");
            System.out.print("Select: ");

            try {
                String line = inputScan.nextLine();
                int selection = Integer.parseInt(line);

                switch (selection) {
                    case 1:
                        processMovieMatch(engine, inputScan);
                        break;
                    case 2:
                        processUserPrefs(engine, inputScan);
                        break;
                    case 3:
                        isActive = false;
                        break;
                    default:
                        System.out.println("Invalid selection.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void processMovieMatch(RecommendationEngine engine, Scanner s) {
        for (int i = 0; i < movieDb.size(); i++) {
            System.out.println(i + ". " + movieDb.get(i).getName());
        }
        System.out.print("Index: ");
        try {
            int idx = Integer.parseInt(s.nextLine());
            if (idx >= 0 && idx < movieDb.size()) {
                Movie target = movieDb.get(idx);
                List<Movie> res = engine.getRecommendations(movieDb, target.getTraits(), globalWeights, 3);
                System.out.println("Results:");
                for (Movie m : res) {
                    System.out.println("- " + m.getName());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    private static void processUserPrefs(RecommendationEngine engine, Scanner s) {
        double[] customVec = new double[columnHeaders.length];
        for (int i = 0; i < columnHeaders.length; i++) {
            System.out.print("Rate " + columnHeaders[i] + " (0-5): ");
            try {
                double val = Double.parseDouble(s.nextLine());
                customVec[i] = val;
            } catch (NumberFormatException e) {
                customVec[i] = 0.0;
            }
        }
        List<Movie> res = engine.getRecommendations(movieDb, customVec, globalWeights, 3);
        System.out.println("Results:");
        for (Movie m : res) {
            System.out.println("- " + m.getName());
        }
    }

    private static boolean initializeDb() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SOURCE_FILE))) {
            String row = reader.readLine();
            if (row == null) return false;

            String[] headerParts = row.split(",");
            columnHeaders = Arrays.copyOfRange(headerParts, 1, headerParts.length);

            row = reader.readLine();
            if (row == null) return false;
            String[] weightParts = row.split(",");
            globalWeights = new double[weightParts.length - 1];
            for (int i = 1; i < weightParts.length; i++) {
                globalWeights[i - 1] = Double.parseDouble(weightParts[i].trim());
            }

            while ((row = reader.readLine()) != null) {
                if (row.trim().isEmpty()) continue;
                String[] data = row.split(",");
                String mName = data[0];
                double[] vals = new double[data.length - 1];
                for (int i = 1; i < data.length; i++) {
                    vals[i - 1] = Double.parseDouble(data[i].trim());
                }
                movieDb.add(new Movie(mName, vals));
            }
            return true;
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
            return false;
        }
    }
}