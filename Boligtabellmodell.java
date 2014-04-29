package Superiore;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.sql.*;
import com.sun.rowset.CachedRowSetImpl;


/**
 * Created by Dragon on 29.04.14.
 */
public class Boligtabellmodell extends AbstractTableModel {
    private CachedRowSetImpl c;
    private Boligtabell b;
    private ResultSetMetaData r;
    private int rownumber;

    public Boligtabellmodell(){
        c = (CachedRowSetImpl) b.visAltIBoligtabellen();

    }

    public Class<?> getColumnclass(int rad){
        try{
            String cname = r.getColumnClassName(rad+1);
            return Class.forName(cname);
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            JOptionPane.showMessageDialog(null, "Klasse ikke funnet!");
        }catch(SQLException sqle){
            sqle.printStackTrace();
            JOptionPane.showMessageDialog(null, "Feil i SQL!");
        }
        return Object.class;
    }

    public int getColumnCount(){
        try{
            return r.getColumnCount();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return 0;
    }

    
}
