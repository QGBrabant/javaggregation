/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import static graphics.GUIHelper.showOnFrame;
import java.awt.Color;
import static java.awt.Color.WHITE;
import java.awt.Dimension;
import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import miscellaneous.Couple;
import orders.impls.BTupleImpl;
import orders.impls.BoxBTuple;

/**
 *
 * @author qgbrabant
 */
public class FastDrawer {

    public static void drawLattice(List<Set<BTupleImpl>> floors, int height, int width) {
        JCanvas jc = new JCanvas(latticeGraphicalStructure(floors, height, width));
        jc.setBackground(WHITE);
        jc.setPreferredSize(new Dimension(max(400, height), height));
        showOnFrame(jc, "test");

    }

    private static List<List<BoxBTuple<Couple<Double>>>> latticeGraphicalStructure(List<Set<BTupleImpl>> floors, double height, double width) {
        List<List<BoxBTuple<Couple<Double>>>> struct = new ArrayList<>();
        double vy = height / (floors.size() + 1);
        double y = vy;
        int j = 0;
        for (int i = floors.size() - 1; i >= 0; i--) {
            struct.add(new LinkedList<>());
            double vx = width / (floors.get(i).size() + 1);
            double x = vx;
            for (BTupleImpl bt : floors.get(i)) {
                struct.get(j).add(new BoxBTuple<>(bt.getBin(), new Couple<>(x, y)));
                x += vx;
            }

            y += vy;
            j++;

        }
        reorganiseFloors(struct);
        return struct;
    }

    private static void reorganiseFloors(List<List<BoxBTuple<Couple<Double>>>> floors) {
        for (int i = 0; i < floors.size(); i++) {
            lexicographicOrdering(floors.get(i));
        }
        BoxBTuple<Couple<Double>> temp;
        for (int i = 1; i < floors.size(); i++) {
            for (int j = 1; j < floors.get(i).size(); j++) {
                for (int k = 0; k < floors.get(i - 1).size(); k++) {
                    Integer relation = floors.get(i - 1).get(k).relation(floors.get(i).get(j));
                    if (relation != null && relation == 1) {
                        for (int l = k + 1; l < floors.get(i - 1).size(); l++) {
                            if (floors.get(i - 1).get(l).relation(floors.get(i).get(j - 1)) == 1) {
                                temp = floors.get(i).get(j - 1);
                                floors.get(i).remove(j - 1);
                                floors.get(i).add(j, temp);

                            }
                        }
                    }
                }
            }
        }

    }

    private static void lexicographicOrdering(List<BoxBTuple<Couple<Double>>> floor) {
        boolean ordered = false;
        BoxBTuple<Couple<Double>> temp;
        while (!ordered) {
            ordered = true;
            for (int i = 0; i < floor.size() - 1; i++) {
                temp = floor.get(i);
                if (temp.lexicographicRelation(floor.get(i + 1)) == -1) {
                    floor.remove(i);
                    floor.add(i + 1, temp);
                    ordered = false;
                }
            }
        }
    }

    public static String pukeLatex(List<Set<BTupleImpl>> floors, double height, double width) {
        List<List<BoxBTuple<Couple<Double>>>> struct = latticeGraphicalStructure(floors, height, width);
        String output = "\\begin{tikzpicture}[scale=1.,every node/.style={scale=1.}]";
        for (int i = 0; i < struct.size(); i++) {
            for (BoxBTuple<Couple<Double>> bt : struct.get(i)) {
                output += "\\node[draw,circle,inner sep = 1pt,minimum size=1pt,fill=black] (" + bt.hashCode() + ") at (" + bt.getContent().getLeft() + "," + bt.getContent().getRight() + ") {};\n";
                //output += "\\draw (" + bt.getContent().getLeft() + "," + bt.getContent().getRight() + ") node {$\\bullet$};\n";
            }
        }
        for (int i = 0; i < struct.size(); i++) {
            for (BoxBTuple<Couple<Double>> bt : struct.get(i)) {

                if (i < struct.size() - 1) {
                    for (BoxBTuple<Couple<Double>> bt2 : struct.get(i + 1)) {
                        boolean link = true;
                        for (int j = 0; j < bt.size(); j++) {
                            if (!bt.get(j) && bt2.get(j)) {
                                link = false;
                                break;
                            }
                        }
                        if (link) {
                            output += "\\draw[-] (" + bt.hashCode() + ") -- (" + bt2.hashCode() + ");\n";
                        }
                    }
                }
            }

        }
        output += "\\end{tikzpicture}";
        return output;
    }
}
