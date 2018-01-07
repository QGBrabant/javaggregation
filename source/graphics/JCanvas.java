/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

/**
 *
 * @author qgbrabant
 */
import java.awt.Graphics;
import java.util.List;
import javax.swing.*;
import miscellaneous.Couple;
import orders.impls.BoxBTuple;

public class JCanvas extends JPanel {

    private List<List<BoxBTuple<Couple<Double>>>> struct;

    public JCanvas(List<List<BoxBTuple<Couple<Double>>>> s) {
        this.struct = s;
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int squaresize = 5;
        if (struct.size() > 20) {
            squaresize = 0;
        }
        for (int i = 0; i < struct.size(); i++) {
            for (BoxBTuple<Couple<Double>> bt : struct.get(i)) {
                g.drawRect(
                        (int)(bt.getContent().getLeft() - (squaresize / 2)),
                        (int)(bt.getContent().getRight() - squaresize / 2),
                        squaresize,
                        squaresize);
                g.fillRect(
                        (int)(bt.getContent().getLeft() - squaresize / 2),
                        (int)(bt.getContent().getRight() - squaresize / 2),
                        squaresize,
                        squaresize);
                //g.drawString(bt.toString(), bt.getContent().getLeft(), bt.getContent().getRight());

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
                            g.drawLine(bt.getContent().getLeft().intValue(), bt.getContent().getRight().intValue(), bt2.getContent().getLeft().intValue(), bt2.getContent().getRight().intValue());
                        }
                    }
                }
            }

        }
    }
}
