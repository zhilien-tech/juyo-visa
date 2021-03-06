package io.znz.jsite.visa.simulator;

import io.znz.jsite.visa.simulator.ui.MainForm;

import javax.swing.JFrame;

/**
 * Created by Chaly on 2017/3/10.
 */
public class Simulator extends JFrame {

	private static final long serialVersionUID = 1L;

	public Simulator() {
		this.add(new MainForm());
		this.setSize(800, 600);
		this.setLocationRelativeTo(rootPane);//居中
		this.setVisible(true);
		this.setTitle("优悦签");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Simulator();
	}
}
