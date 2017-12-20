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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class GUIHelper {
	
	public static void showOnFrame(JComponent component, String frameName) {
		JFrame frame = new JFrame(frameName);
		WindowAdapter wa = new WindowAdapter() {
                        @Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		frame.addWindowListener(wa);
		frame.getContentPane().add(component);
		frame.pack();
		frame.setVisible(true);
	}

}
