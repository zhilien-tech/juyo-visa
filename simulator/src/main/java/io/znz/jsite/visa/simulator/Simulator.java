package io.znz.jsite.visa.simulator;

import io.znz.jsite.visa.simulator.ui.MainForm;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Chaly on 2017/3/10.
 */
public class Simulator extends JFrame {

    public Simulator() {
        this.add(new MainForm());
        this.setSize(800, 600);
        this.setLocationRelativeTo(rootPane);//居中
        this.setVisible(true);
        this.setTitle("签手世界");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException {
        new Simulator();
    }
}
