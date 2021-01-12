package gr4.tableadapter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author  Grios - angel.grios@gmail.com
 * @version 0.1
 * 
 * This abstract class represents the super class in order to implement custom JavaFX TableView adapters.
 */
public abstract class AbstractTableAdapter
{
    /**
     * The TableView Node that you want to adapt.
     */
    TableView tableView;
    
    /**
     * The columns that the TableView object will to contain.
     */
    TableColumn[] columns;
    
    /**
     * The column names that will displayed in the TableView object.
     */
    Object[] columnNames;
    
    /**
     * The TableCell[] array that "tells" to the TableView the way to render each cell on it.
     */
    TableCell[] tableCellValues;
    
    /**
     * A foo observable collection that tells to the TableView the number of rows that will be displayed.
     */
    ObservableList<SimpleIntegerProperty> listRows;
            
    /**
     * Returns the value contained into the TableView object in the specified row and column.<br>
     * You must implement this method on each AbstractTableAdapter subclass.
     * @param rowIndex      The row index where the value is.
     * @param columnIndex   he column index where the value is.
     * @return 
     */
    public abstract Object getValueAt(int rowIndex, int columnIndex);
    
    /**
     * Sets the value to the TableView object specifying the value, the row and the column.<br>
     * You must implement this method on each AbstractTableAdapter subclass.
     * @param value         The value that must be setted.
     * @param rowIndex      The row index where the value will be setted.
     * @param columnIndex   The column index where the value will be setted..
     */
    public abstract void setValueAt(Object value, int rowIndex, int columnIndex);
    
    /**
     * Sets a custom render for the specified column.<br>
     * You must implement this method on each AbstractTableAdapter subclass.
     * @param tableCell
     * @param columnIndex 
     */
    public abstract void setTableCellValue(TableCell<Object, Object> tableCell, int columnIndex);
    public abstract void adapt();
    
    /**
     * Update the TableView content. Invoke this methd after update values in the underlying data of table adapters.
     */
    public void refresh()
    {
        if (tableView.getColumns().size() > 0)
        {
            ((TableColumn) tableView.getColumns().get(0)).setVisible(!((TableColumn) tableView.getColumns().get(0)).isVisible());
            ((TableColumn) tableView.getColumns().get(0)).setVisible(!((TableColumn) tableView.getColumns().get(0)).isVisible());
        }
    }
}
