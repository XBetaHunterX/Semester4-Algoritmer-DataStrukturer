package ExamTools;

import Execution.Main;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;

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
    public AlgorithmComparison() {

    }

    public String compare(String fn, String gn) {
        String functionF = fn.replaceAll(" ", "");
        String functionG = gn.replaceAll(" ", "");

        int n = 100000;
        ArrayList<Double> results = new ArrayList<>();


        for (int i = n; i < n * 100; i += n) {
            Expression expression = new ExpressionBuilder("(" + functionF.replace("n", "" + i)
                    + ")/("
                    + functionG.replace("n", "" + i) + ")").build();

            results.add(expression.evaluate());
        }

        return analyzeRatios(results);
    }

    private String analyzeRatios(ArrayList<Double> results) {
        // Analyze the ratios and classify the relationship

        if (approachesInfinitely(results)) {
            return "Tends to infinity: lim(n → ∞) (f(n) / g(n)) = ∞ implies f(n) = Ω(g(n)";
        } else if (approachesConstant(results)) {
            return "Tends to a constant: lim(n → ∞) (f(n) / g(n)) = c (where c is a constant) implies f(n) = Θ(g(n))";
        } else if (approachesZero(results)) {
            return "Tends to zero: lim(n → ∞) (f(n) / g(n)) = 0 implies f(n) = O(g(n))";
        } else {
            return "Unable to determine the relationship.";
        }
    }

    private boolean approachesInfinitely(ArrayList<Double> results) {
        for (int i = 0; i + 2 < results.size(); i++) {
            boolean grows = results.get(i + 1) - results.get(i) < results.get(i + 2) - results.get(i + 1);

            if (!grows) {
                return false;
            }
        }

        return true;
    }

    private boolean approachesZero(ArrayList<Double> results) {
        for (int i = 0; i + 1 < results.size(); i++) {
            if (results.get(i) > results.get(i + 1) && results.get(results.size() - 1) > 0) {
                return false;
            }
        }

        return true;
    }

    private boolean approachesConstant(ArrayList<Double> results) {
        double constant = results.get(results.size() - 1);

        for (int i = 0; i + 2 < results.size(); i++) {
            boolean isLess = Math.abs(results.get(i) - results.get(i + 1)) > Math.abs(results.get(i + 1) - results.get(i + 2));
            boolean approachesConstant = constant

            if (!isLess || results.get(results.size() - 1) < 0) {
                return false;
            }
        }

        return true;
    }
}
