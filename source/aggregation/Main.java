/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregation;

import graphics.FastDrawer;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import miscellaneous.Context;
import miscellaneous.Couple;
import orders.OTools;
import orders.impls.BTupleImpl;
import orders.impls.DistributiveLatticeArrayImpl;
import orders.impls.DistributiveLatticeBinaryImpl;
import orders.impls.IndexedBTuple;

/**
 * Main class for launching processes concerning lattice polynomials.
 *
 * @author qgbrabant
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Context.parseArgs(args);

        List<String> arguments = Context.getArgList();

        if (arguments.isEmpty()) {
            System.out.println("Use one of the following argument:");
            System.out.println("\tinterpolationTest");
            return;
        }
            
        switch (arguments.get(0)) {
            case "interpolationTest":
                test_interpolation_random_data();
                break;
        }

    }

    /**
     * Measures the time required for computing the lower and upper constraints
     * defining IP(f), where f is a randomly generated partial function.
     */
    public static void test_interpolation_random_data() {
        //ResultPack res = ResultPack.getResultPack("_", 5);
        String expName = Context.getOption("-expName", "_");

        List<Integer> arities = Context.getIntegerListOption("-n", Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 10, 15, 20, 30}));
        List<Integer> dataset_sizes = Context.getIntegerListOption("-m", Arrays.asList(new Integer[]{10, 100, 1000, 10000, 50000}));
        List<Integer> dims = Context.getIntegerListOption("-k", Arrays.asList(new Integer[]{1, 2, 3, 5, 7, 10, 15, 20, 30}));
        List<Double> probs = Context.getDoubleListOption("-p", Arrays.asList(new Double[]{0.6}));
        List<Double> granularities = Context.getDoubleListOption("-g", Arrays.asList(new Double[]{100.}));

        Integer nbRepetitions = Integer.parseInt(Context.getOption("-nbrep", "1"));

        for (int rep = 0; rep < nbRepetitions; rep++) {
            for (int n : arities) {
                for (int m : dataset_sizes) {
                    for (int d : dims) {
                        for (double p : probs) {
                            for (double g : granularities) {
                                Context.addResult(
                                        test(n, m, d, p, (int) g),
                                        expName, (double) n, (double) m, (double) d, (double) p, (double) g
                                );
                            }
                        }
                    }
                }
            }
            if (Context.getOption("-save", "false").equals("true")) {
                Context.save();
            }
        }
    }

    /**
     * Implementation of one test, as described in "Interpolation of partial
     * functions by lattice polynomial functions: a polynomial time algorithm"
     * The constraints specifying IP(f) are computed, for a randomly generated
     * partial lattice polynomial function f.
     *
     * @param n arity of f
     * @param m size of the domain of f
     * @param k height of the lattice
     * @param p probability value used in the random ordering of the join
     * irreducible elements of the lattice (if p=1, the lattice is a chain, if
     * p=0, the lattice is the powerset 2^[k]).
     * @param g maximal number of focal sets of f
     * @return time required for computing the constraints and checking their
     * consistency
     */
    public static double test(int n, int m, int k, double p, int g) {
        System.out.println("Test : arity=" + n
                + " nb_chunks=" + m
                + " lattice_dim=" + k
                + " probability_of_ordering=" + p
                + " smoothness=" + g
        );

        DistributiveLatticeArrayImpl L
                = new DistributiveLatticeArrayImpl(
                        new DistributiveLatticeBinaryImpl(OTools.randomJoinIrreducibleElements(k, p)
                        )
                );

        //FastDrawer.drawLattice(L.getFloors(), 400);
        Capacity<IndexedBTuple> generator = LinkedCapacity.randomInstance(n, L, g);

        AggregationTable<IndexedBTuple> ds
                = AggregationTableImpl.randomInstance(m, generator);

        long start = System.nanoTime();

        Couple<Map<BTupleImpl, IndexedBTuple>> couple
                = PLPInterpolationProcesses.computeLPConstraints(ds);

        assert PLPInterpolationProcesses.isConsistent(couple);

        double end = ((double) System.nanoTime() - start) / 1000000;

        System.out.println(end + " ms.");

        return end;
    }

    /*public static void test1() {
        File f = new File("/home/qgbrabant/Downloads/all_data_mixed.txt");
        Lexicon<BTupleImpl> lex = new Lexicon();
        lex.put("1", new BTupleImpl(new Boolean[]{false, false, false, false, false}));
        lex.put("2", new BTupleImpl(new Boolean[]{false, false, false, false, true}));
        lex.put("3", new BTupleImpl(new Boolean[]{false, false, false, true, true}));
        lex.put("4", new BTupleImpl(new Boolean[]{false, false, true, true, true}));
        lex.put("5", new BTupleImpl(new Boolean[]{false, true, true, true, true}));
        lex.put("-1", new BTupleImpl(new Boolean[]{true, false, false, false, false}));
        lex.addSeparator(",");
        HashSet<BTupleImpl> elements = new HashSet<>();
        elements.addAll(lex.values());
        DistributiveLatticeBinaryImpl L = new DistributiveLatticeBinaryImpl(elements, 5);
        AggregationTableImpl ds = new AggregationTableImpl(L, 7, lex, f, 10000);

        System.out.println(
                PLPInterpolationProcesses.isConsistent(PLPInterpolationProcesses.computeLPConstraints(L, ds))
        );

        for (int d = 0; d < 5; d++) {
            System.out.println(d);
            System.out.println(L.getStMinus(d));
            System.out.println(L.getStPlus(d));
        }
    }

    public static void test3(int height, double p, int nb, int frameheight) {
        for (int i = 0; i < nb; i++) {
            FastDrawer.drawLattice(new DistributiveLatticeBinaryImpl(OTools.randomJoinIrreducibleElements(height, p)).getFloors(), frameheight, frameheight);
        }
    }

    public static void test4() {

        //DistributiveLatticeBinaryImpl L = new DistributiveLatticeBinaryImpl(RandomGenerator.randomJoinIrreducibleElements(6, 0.6));
    }*/
    public static double test5(int n, int m, int k, double p, double smoothness) {
        System.out.println("Test : arity=" + n
                + " nb_chunks=" + m
                + " lattice_dim=" + k
                + " probability_of_ordering=" + p
                + " smoothness=" + smoothness);

        DistributiveLatticeArrayImpl L
                = new DistributiveLatticeArrayImpl(
                        new DistributiveLatticeBinaryImpl(OTools.randomJoinIrreducibleElements(k, p)
                        )
                );

        //FastDrawer.drawLattice(L.getFloors(), 400);
        Capacity<IndexedBTuple> generator
                = //GranularCapacity.<BTuple>randomGranularCapacity(n, L, smoothness);
                LinkedCapacity.<IndexedBTuple>randomInstance(n, L, smoothness);

        AggregationTable<IndexedBTuple> ds
                = AggregationTableImpl.randomInstance(m, generator);

        long start = System.nanoTime();

        Couple<Map<BTupleImpl, IndexedBTuple>> couple
                = PLPInterpolationProcesses.computeLPConstraints(ds);

        assert PLPInterpolationProcesses.isConsistent(couple);

        double end = ((double) System.nanoTime() - start) / 1000000;

        Set<CapacityNode> lowerConstraints = new HashSet<>();
        Set<CapacityNode> upperConstraints = new HashSet<>();

        for (Entry<BTupleImpl, IndexedBTuple> e : couple.getLeft().entrySet()) {
            lowerConstraints.add(
                    new CapacityNode(e.getKey().getBin(), e.getValue())
            );
        }

        for (Entry<BTupleImpl, IndexedBTuple> e : couple.getRight().entrySet()) {
            upperConstraints.add(
                    new CapacityNode(e.getKey().getBin(), e.getValue())
            );
        }

        GranularCapacity lowerBound = new GranularCapacity(n, L, lowerConstraints);
        GranularCapacity upperBound = new GranularCapacity(n, L, upperConstraints);

        return end;
    }

    /*public static double testExhaustiveLearning(int n, int m, int k, double p, int g) {
        System.out.println("Test : arity=" + n + " nb_chunks=" + m + " lattice_dim=" + k + " probability_of_ordering=" + p + " smoothness=" + g);

        ComplementedLatticeArrayImpl L = new ComplementedLatticeArrayImpl(new DistributiveLatticeBinaryImpl(OTools.randomJoinIrreducibleElements(k, p)));

        Capacity<IndexedBTuple> generator
                = //GranularCapacity.<BTuple>randomGranularCapacity(n, L, smoothness);
                LinkedCapacity.<BTupleImpl>randomInstance(n, L, g);
        AggregationTable<IndexedBTuple> ds = AggregationTable.<IndexedBTuple>randomInstance(m, generator);
        long start = System.nanoTime();
        
        GranularCapacity<IndexedBTuple> mu = GranularCapacity.exhaustiveLearning(n, L, ds);
        
        double end = ((double) System.nanoTime() - start) / 1000000;
        
        return end;
    }*/
    public static void test6(int nblines, int nbcolumns, double p, int dim) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);

        System.out.println("\\begin{figure}");

        for (int i = 0; i < nblines * nbcolumns; i++) {
            System.out.println("\\begin{minipage}{" + df.format(1. / nbcolumns) + "\\textwidth}");
            System.out.println("\\centering");
            System.out.println(FastDrawer.pukeLatex(new DistributiveLatticeBinaryImpl(OTools.randomJoinIrreducibleElements(dim, p)).getFloors(), 12.2 / nbcolumns, 12.2 / nbcolumns));
            System.out.println("\\end{minipage}");
        }
        System.out.println("\\end{figure}");

    }

}
