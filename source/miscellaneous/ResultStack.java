/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.io.Serializable;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author qgbrabant
 */
public class ResultStack<R extends Serializable> implements Serializable {

    private final List<R> values;

    public ResultStack() {
        values = new ArrayList<>();
    }

    public void addValue(R d) {
        this.values.add(d);
    }

    public Double getMeanValue(Function<R, Double> eval) {
        return this.values
                .stream()
                .map(eval)
                .reduce(0., (x, y) -> x + y)
                / this.values.size();
    }

    public Double getStandardDeviation(Function<R, Double> eval) {
        System.out.println("Mean values :");
        System.out.println(Arrays.toString(this.values.stream().map(eval).toArray()));
        double mean = this.getMeanValue(eval);
        return sqrt(this.values
                .stream()
                .map(eval)
                .map(x -> (x - mean) * (x - mean))
                .reduce(0., (x, y) -> x + y)
                / this.values.size());
    }

    public List<Double> getMeanValueRow(Function<R, List<Double>> eval) {
        List<Double> globalRes = new ArrayList<>();
        List<Double> res;
        for (R r : this.values) {
            res = eval.apply(r);
            for (int i = 0; i < res.size(); i++) {
                if (i >= globalRes.size()) {
                    globalRes.add(0.);
                }
                globalRes.set(i, globalRes.get(i) + res.get(i));
            }
        }
        for (int i = 0; i < globalRes.size(); i++) {
            globalRes.set(i, globalRes.get(i) / this.values.size());
        }
        return globalRes;
    }

    public int getNumberOfValues() {
        return this.values.size();
    }

    public String toString() {
        return "Result(s): " + values;
    }

}
