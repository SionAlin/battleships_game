package main.ui;

import java.io.*;
import java.awt.*;
import javax.swing.*;

public class PlayerVSComputerMenu extends PaperPanel{
    
    public PlayerVSComputerMenu(MainMenu menu){
        setLayout(new BorderLayout());
        
        SketchButton back = new SketchButton("<-", () -> menu.Show("HOME"));
        back.setMaximumSize(new Dimension(60, 60));
        this.add(back);
    }

}
