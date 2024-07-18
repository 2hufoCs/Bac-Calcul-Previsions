import java.util.ArrayList;

public class Stats {

    public static int sumOfTerms(ArrayList<Integer> numbers) {
        int sum = 0;
        for (int term : numbers) {
            sum += term;
        }
        return sum;
    }

    public static float minimumRequiredNote(float currentScore, float requiredScore, ArrayList<Integer> leftoverCoefs) {
        if (currentScore > requiredScore) {
            return 0;
        }

        float scoreDiff = requiredScore - currentScore;
        int coefSum = sumOfTerms(leftoverCoefs);
        return scoreDiff / coefSum;
    }

    public static float weightedMean(ArrayList<Float> values, ArrayList<Integer> weights) {
        if (values.size() != weights.size()) {
            System.out.println("error doing weighted mean, not the same number of notes and coefs");
            return 0;
        }
        if (values.size() == 0 || weights.size() == 0) {
            return 0;
        }

        float weightedSum = 0;
        for (int i = 0; i < values.size(); i++) {
            weightedSum += values.get(i) * weights.get(i);
        }
        return weightedSum / sumOfTerms(weights);
    }

    public static float weightedSum(ArrayList<Float> values, ArrayList<Integer> weights) {
        if (values.size() != weights.size()) {
            System.out.println("error doing weighted mean, not the same number of notes and coefs");
            return 0;
        }
        if (values.size() == 0 || weights.size() == 0) {
            return 0;
        }

        float weightedSum = 0;
        for (int i = 0; i < values.size(); i++) {
            weightedSum += values.get(i) * weights.get(i);
        }
        return weightedSum;
    }
}