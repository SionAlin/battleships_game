package main.ui;

import java.io.*;
import java.awt.*;
import javax.swing.*;

public class PlayerVSPlayerMenu extends PaperPanel{
    
    public PlayerVSPlayerMenu(MainMenu menu){
        setLayout(new BorderLayout());
        
        SketchButton back = new SketchButton("<-", () -> menu.Show("HOME"));
        back.setMaximumSize(new Dimension(60, 60));
        this.add(back);
    }

}
