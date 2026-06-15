import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
public class Main{
    public static void main(String[] args) {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){}
        SwingUtilities.invokeLater(() -> {
            new WelcomePage();
        });
    }
}