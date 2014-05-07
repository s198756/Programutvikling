package GUI.Files;

import javax.swing.*;

/**
 * Created by ThomN on 06.05.2014.
 */
public class Driver
{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Tabs();
            }
        });
    }
}
