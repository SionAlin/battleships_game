package main.ui;

import java.io.*;
import java.awt.*;
import javax.swing.*;

public class SketchLabel extends JLabel{
    
    private final Color ink = new Color(35, 75, 155);
    private final Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
    
    public SketchLabel(String text){
        super(text);
        setForeground(this.ink);
        setFont(this.font);
    }
}
