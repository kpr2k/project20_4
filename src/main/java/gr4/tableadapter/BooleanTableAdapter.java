
package gr4.tableadapter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.Arrays;

/**
 *  This class is a table-adapter that allows you to display a boolean matrix inside a JavaFX TableView component.
 * @author Grios angel.grios@gmail.com
 */
public class BooleanTableAdapter extends AbstractTableAdapter
{
    Boolean[][] data;
    boolean[] autoRender;
    boolean autoRenderAll;
    
    /**
     * Builds a BooleanTableAdapter with no data.
     */
    public BooleanTableAdapter()
    {
        this(null, null);
    }
    
    /**
     * Builds a BooleanTableAdapter specifying the TableView control and the Boolean-object-type matrix.
     * @param tableView The TableView control that will display the data.
     * @param data The data to be displayed.
     */
    public BooleanTableAdapter(TableView tableView, Boolean[][] data)
    {
        this(tableView, data, null);
    }
    
    /**
     * Builds a BooleanTableAdapter specifying the TableView control, the Boolean-object-type matrix and the column's names.
     * @param tableView The TableView control that will display the data.
     * @param data The data to be displayed.
     * @param columnNames The column titles that want to display in the TableView control.
     */
    public BooleanTableAdapter(TableView tableView, Boolean[][] data, Object[] columnNames)
    {
        this(tableView, data, columnNames, true);
    }
    
    /**
     * Builds a BooleanTableAdapter specifying the TableView control, the Boolean-object-type matrix, the column's names and
     * a value that specifies if the values will be autorendered or not.
     * @param tableView The TableView control that will display the data.
     * @param data The data to be displayed.
     * @param columnNames The column titles that want to display in the TableView control.
     * @param autoRenderAll Specifies if the data will be autorendered. If the value is true the data will be rendered as checkboxex. If this value is false
     * the values will be rendered as text.
     */
    public BooleanTableAdapter(TableView tableView, Boolean[][] data, Object[] columnNames, boolean autoRenderAll)
    {
        this.tableView = tableView;
        this.data = data;
        this.columnNames = columnNames;
        this.autoRenderAll = autoRenderAll;
    }
    
    /**
     * Set if all values must be autorendred. If the value is true the data will be rendered as checkboxex. If this value is false
     * the values will be rendered as text.
     * @param value 
     */
    public void setAutoRenderAll(boolean value)
    {
        autoRenderAll = value;
        if (value)
        {
            for (int i = 0; i < autoRender.length; i++)
                autoRender[i] = value;
        }
    }
    
    /**
     * Set if the values in the specified column must be autorendred. If the value is true the data will be rendered as checkboxex. If this value is false
     * the values will be rendered as text.
     * @param columnIndex
     * @param value 
     */
    public void setAutoRender(int columnIndex, boolean value)
    {
        autoRender[columnIndex] = value;
    }
    
    /**
     * Return the autorender value for the specified column.
     * @param columnIndex
     * @return 
     */
    public boolean isAutoRender(int columnIndex)
    {
        return autoRender[columnIndex];
    }
    
    /**
     * Return the value in the data matrix for the specified row and column.
     * @param rowIndex  The row index.
     * @param columnIndex   The column index.
     * @return 
     */
    @Override
    public Boolean getValueAt(int rowIndex, int columnIndex)
    {
        return data[rowIndex][columnIndex];
    }

    /**
     * Set the value into the data matrix at the specified row and column.
     * @param value The value to be stored into the data matrix.
     * @param rowIndex  The row in the data matrix.
     * @param columnIndex The column in the data matrix.
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        data[rowIndex][columnIndex] = (Boolean) value;
    }

    /**
     * 
     * @param tableCell
     * @param columnIndex 
     */
    @Override
    public void setTableCellValue(TableCell<Object, Object> tableCell, int columnIndex)
    {
        tableCellValues[columnIndex] = tableCell;
    }

    /**
     * Makes that the TableView control related with this table adapter display the specified data.
     */
    @Override
    public void adapt()
    {
        initColumns();
        createRows();
        createCellValues();
        bind();
    }
    
    /**
     * Initialize the columns hat will be displyed in the TabelView control.
     */
    private void initColumns()
    {
        Object colNameTemp = null;
        if (data == null || data.length < 1)
        {
            columns = null;
            return;
        }
        else
        {
            columns = new TableColumn[data[0].length];
            if (columnNames == null)
            {
                columnNames = new Object[columns.length];
                for (int i = 0; i < columnNames.length; i++)
                    columnNames[i] = "Column " + (i + 1);
            }
            tableCellValues = new TableCell[columns.length];
            for (int i = 0; i < columns.length; i++)
            {
                colNameTemp = columnNames[i];
                columns[i] = new TableColumn(colNameTemp.toString());                
            }
            
            autoRender = new boolean[columns.length];
            if (autoRenderAll)
                setAutoRenderAll(true);
            
        }
    }
    
    /**
     * Create a foo observable collection that will tell to the table how many rows must be displayed into the TableView control. 
     */
    private void createRows()
    {
        listRows = FXCollections.observableArrayList();
        if (data != null && data.length > 0)
            for (int i = 0; i < data.length; i++)
                listRows.add(new SimpleIntegerProperty(i));
    }
    
    /**
     * Build
     */
    private void createCellValues()
    {
        for (int i = 0; i < columns.length; i++)
        {
            final int columnIndex = i;
            columns[columnIndex].setCellValueFactory(new PropertyValueFactory<>(""));
            columns[columnIndex].setCellFactory(new Callback<TableColumn<Object, Object>, Object>()
            {
                @Override
                public TableCell call(TableColumn<Object, Object> param)
                {
                    return new BooleanTableCellRender<Object, Object>()
                    {                        
                        
                        @Override
                        protected void updateItem(Object item, boolean empty) 
                        {
                            Boolean value = null;
                            super.updateItem(item, empty);
                            if (getIndex() >= 0 && getIndex() < data.length)
                            {
                                value = data[getIndex()][columnIndex];                                
                                if (autoRender[getIndex()] && value != null)
                                {
                                    valueRender.setSelected(value);
                                    setGraphic(valueRender);
                                }
                                else if (value != null)
                                {
                                    setText(value.toString());
                                    setGraphic(null);
                                }
                                else
                                {
                                    setText("");
                                    setGraphic(null);
                                }
                            }
                        }
                    };
                }
            });
        }
    }

    private void bind()
    {
        tableView.getColumns().clear();
        tableView.getColumns().addAll(Arrays.asList(columns));
        tableView.getItems().clear();
        tableView.setItems(listRows);
    }
    
    
}
