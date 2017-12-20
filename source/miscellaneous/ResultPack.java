/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miscellaneous;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author qgbrabant
 */
public class ResultPack<R extends Serializable> implements Serializable {

    

    private final Map<Tuple<Double>, ResultStack<R>> table;
    private final String[] variableNames;
    private final String expName; // identification of the result pack
    private final int dimensionality;

    public ResultPack(String expName, int d) {
        this.expName = expName;
        this.dimensionality = d;
        this.table = new HashMap<>();
        this.variableNames = new String[d];
        for (int i = 0; i < d; i++) {
            this.variableNames[i] = "x" + i;
        }
    }

    private ResultPack(String expName, int d, String[] variableNames) {
        this(expName, d);
        for (int i = 0; i < d; i++) {
            this.variableNames[i] = variableNames[i];
        }
    }

    public String getExpName() {
        return expName;
    }

    public int getDimensionality() {
        return dimensionality;
    }
    

    public void addResult(TupleImpl<Double> k, R result) {
        assert k.size() == this.dimensionality;
        if (!this.table.containsKey(k)) {
            this.table.put(k, new ResultStack());
        }
        this.table.get(k).addValue(result);
    }

    public String toString() {
        String res = "Table " + this.expName + ":\n";
        res += "variables: ";
        for (String vn : this.variableNames) {
            res += vn + ", ";
        }
        res += this.table;
        return res;
    }

    public void save() {
        File fichier = new File(Context.getOption("-output") + this.expName + "_d" + this.dimensionality + ".result");

        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(fichier));
            oos.writeObject(this);
        } catch (IOException ex) {
            System.out.println("Cannot save the results.");
            Logger.getLogger(ResultPack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean contains(TupleImpl<Double> k) {
        return this.table.containsKey(k);
    }

    public int getNumberOfValues(TupleImpl<Double> key) {
        if (this.table.containsKey(key)) {
            return this.table.get(key).getNumberOfValues();
        } else {
            return 0;
        }
    }

    public Set<Entry<Tuple<Double>, ResultStack<R>>> getParetoOptima(Function<R, Double> eval1, boolean biggerIsBetter1, Function<R, Double> eval2, boolean biggerIsBetter2) {
        Set<Entry<Tuple<Double>, ResultStack<R>>> paretoz = new HashSet<>();
        Entry<Tuple<Double>, ResultStack<R>> p;
        boolean maximal;
        double scoreA1;
        double scoreA2;
        double scoreB1;
        double scoreB2;

        Iterator<Entry<Tuple<Double>, ResultStack<R>>> it;

        for (Entry<Tuple<Double>, ResultStack<R>> candidate : this.table.entrySet()) {
            maximal = true;

            scoreA1 = candidate.getValue().getMeanValue(eval1);
            if (!biggerIsBetter1) {
                scoreA1 *= -1;
            }
            scoreA2 = candidate.getValue().getMeanValue(eval2);
            if (!biggerIsBetter2) {
                scoreA2 *= -1;
            }

            it = paretoz.iterator();

            while (it.hasNext() && maximal) {
                p = it.next();

                scoreB1 = p.getValue().getMeanValue(eval1);
                if (!biggerIsBetter1) {
                    scoreB1 *= -1;
                }
                scoreB2 = p.getValue().getMeanValue(eval2);
                if (!biggerIsBetter2) {
                    scoreB2 *= -1;
                }

                if (!(scoreA1 == scoreB1 && scoreA2 == scoreB2)) {
                    if (scoreA1 >= scoreB1 && scoreA2 >= scoreB2) {
                        it.remove();
                    }
                }
                
                if (scoreA1 <= scoreB1 && scoreA2 <= scoreB2) {
                    maximal = false;
                }
            }
            if (maximal) {
                paretoz.add(candidate);
            }
        }

        return paretoz;
    }

    /*public String latexTable(Double[] params, Function<R, Double> eval) {
        if (params.length != this.dimensionality) {
            return "Wrong number of paramters, cannot display result table.";
        }
        int x = -1;
        int y = -1;
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                if (x == -1) {
                    x = i;
                } else if (y == -1) {
                    y = i;
                } else {
                    return "Cannot output table with more than 2 dimensions.";
                }
            }
        }

        List<Double> xaxis = new ArrayList<>();
        List<Double> yaxis = new ArrayList<>();
        double xval;
        double yval;
        int i;
        for (Entry<Tuple<Double>, Result<R>> e : this.table.entrySet()) {
            xval = e.getKey().get(x);
            yval = e.getKey().get(y);
            i = 0;
            if (xaxis.size() == 0) {
                xaxis.add(xval);
            }
            if (yaxis.size() == 0) {
                yaxis.add(yval);
            }
            while (i < xaxis.size()) {
                if (xaxis.get(i) == xval) {
                    break;
                } else if (xaxis.get(i) > xval) {
                    xaxis.add(i, xval);
                    break;
                } else if (i == xaxis.size() - 1) {
                    xaxis.add(xval);
                }
                i++;
            }
            i = 0;
            while (i < yaxis.size()) {
                if (yaxis.get(i) == yval) {
                    break;
                } else if (yaxis.get(i) > yval) {
                    yaxis.add(i, yval);
                    break;
                } else if (i == yaxis.size() - 1) {
                    yaxis.add(yval);
                }
                i++;
            }
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        String res = "\\begin{tabular}{|r|";
        for (int j = 0; j < xaxis.size(); j++) {
            res += "c|";
        }
        res += "}\n  \\hline \n";
        TupleImpl<Double> key;
        for (int k = 0; k < yaxis.size(); k++) {
            res += " & " + yaxis.get(k);
        }
        res += " \\\\ \n";
        for (double xv : xaxis) {
            res += "\\hline \n" + xv + " & ";
            for (int k = 0; k < yaxis.size(); k++) {
                params[x] = xv;
                params[y] = yaxis.get(k);
                key = new TupleImpl<Double>(params);
                res += df.format(this.table.get(key).getMeanValue(eval));
                if (k < yaxis.size() - 1) {
                    res += " & ";
                }
            }
            res += "\\\\\n";
        }
        res += "\\hline \n \\end{tabular}";

        return res;
    }*/
    public void clear() {
        this.table.clear();
    }
}
