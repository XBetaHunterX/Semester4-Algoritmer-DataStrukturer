package ExamTools;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.ArrayList;
import java.util.Arrays;

/*
Explanation of Relationships between Time Complexities:

1. Tends to infinity:
   If the ratio of runtimes tends to infinity as n grows, it means that the numerator grows faster than the denominator,
   indicating that the algorithm in the numerator has a higher time complexity. Symbolically,
   lim(n → ∞) (f(n) / g(n)) = ∞ implies f(n) = Ω(g(n)).

2. Tends to a constant:
   If the ratio of runtimes tends to a constant as n grows, it means that both algorithms have the same order of growth.
   Symbolically, lim(n → ∞) (f(n) / g(n)) = c (where c is a constant) implies f(n) = Θ(g(n)).

3. Tends to zero:
   If the ratio of runtimes tends to zero as n grows, it means that the denominator grows faster than the numerator,
   indicating that the algorithm in the numerator has a lower time complexity. Symbolically,
   lim(n → ∞) (f(n) / g(n)) = 0 implies f(n) = O(g(n)).

In each case, f(n) represents the time complexity of the algorithm in the numerator, and g(n) represents the time complexity
of the algorithm in the denominator.
*/

public class AlgorithmComparison {
    public static final String[] standardGrowthFunctions= {"1", "log(n)", "sqrt(n)", "n", "n*log(n)", "n*sqrt(n)", "n^2", "n^3", "n^10", "2^n"};
    public AlgorithmComparison() {

    }

    public String compare(String fn, String gn) {
        String functionF = fn.replaceAll(" ", "");
        String functionG = gn.replaceAll(" ", "");

        int n = 1000;
        ArrayList<Double> results = new ArrayList<>();


        for (int i = n; i < n * 100; i += n) {
            String stringExpression = "(" + functionF.replace("n", "" + i)
                    + ")/("
                    + functionG.replace("n", "" + i) + ")";
            Expression expression = new ExpressionBuilder(stringExpression).build();
            results.add(expression.evaluate());
        }

        return analyzeRatios(results);
    }

    private String analyzeRatios(ArrayList<Double> results) {
        // Analyze the ratios and classify the relationship

        if (approachesInfinitely(results)) {
            return "Tends to infinity: lim(n → ∞) (f(n) / g(n)) = ∞ implies f(n) = Ω(g(n)";
        } else if (approachesZero(results)) {
            return "Tends to zero: lim(n → ∞) (f(n) / g(n)) = 0 implies f(n) = O(g(n))";
        } else if (approachesConstant(results)) {
            return "Tends to a constant: lim(n → ∞) (f(n) / g(n)) = c (where c is a constant) implies f(n) = Θ(g(n))";
        } else {
            return "Unable to determine the relationship. Values may have overflowed.";
        }
    }

    private boolean approachesInfinitely(ArrayList<Double> results) {
        for (int i = 0; i + 2 < results.size(); i++) {
            boolean grows = results.get(i) < results.get(i + 1);
            boolean decelerates = results.get(i + 1) - results.get(i) > results.get(i + 2) - results.get(i + 1);

            if (results.get(i).isInfinite() || results.get(i).isNaN()) {
                return true;
            }

            if (!grows || decelerates) {
                return false;
            }
        }

        return true;
    }

    private boolean approachesZero(ArrayList<Double> results) {
        for (int i = 0; i + 1 < results.size(); i++) {
            boolean decreases = results.get(i) > results.get(i + 1);
            boolean isGreaterThanZero = results.get(results.size() - 1) > 0;
            boolean slopeIsNegative = 0 > results.get(i + 1) - results.get(i);
            boolean isLessThanOne = results.get(results.size() - 1) < 1;

            if (results.get(i).isInfinite() || results.get(i).isNaN()) {
                return true;
            }

            if (!decreases || !isGreaterThanZero || !slopeIsNegative || !isLessThanOne) {
                return false;
            }
        }

        return true;
    }

    private boolean approachesConstant(ArrayList<Double> results) {
        double constant = results.get(results.size() - 1);

        if (results.get(results.size() - 1).equals(1.0)) {
            return true;
        }

        for (int i = 0; i + 2 < results.size(); i++) {
            boolean isGreaterThanOne = results.get(results.size() - 1) >= 1;
            boolean approachesConstant = Math.abs(constant - results.get(i)) >  Math.abs(constant - results.get(i + 1));

            if (results.get(i).isInfinite() || results.get(i).isNaN()) {
                return true;
            }

            if (!isGreaterThanOne || !approachesConstant) {
                return false;
            }
        }

        return true;
    }

    public String compareToAll(String fn) {
        StringBuilder product = new StringBuilder();

        // Calculate the maximum length of the left-hand side
        int maxLeftLength = 0;
        for (String function : standardGrowthFunctions) {
            int leftLength = (fn + " : " + function).length();
            maxLeftLength = Math.max(maxLeftLength, leftLength);
        }

        // Build the comparison strings with aligned arrows
        for (String function : standardGrowthFunctions) {
            String comparison = this.compare(fn, function);
            String arrowPadding = " ".repeat(maxLeftLength - (fn + " : " + function).length());
            product.append(fn).append(" : ").append(function).append(arrowPadding).append(" ---> ").append(comparison).append("\n");
        }

        return product.toString();
    }

}
