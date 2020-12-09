/*
 *  Name:           DefaultTableAdapter
 *  Version:        0.1
 *  Date:           05/10/2016
 *  Author:         Miguel Angel Gil rios
 *  Email:          angel.grios@gmail.com
 *  Description:
 *                  This class is the default implementation of the 
 *                  AbstractTableAdapter interface.
 *  Comments:       This is the first proposal.
 *  
 *  Version:        0.2
 *  Date:           05/07/2017
 *  Author:         Miguel Angel Gil Rios
 *  Email:          angel.grios@gmail.com
 *  Comments:
 *                  This version added static methods in order to build the
 *                  DefaultTableAdapter in a more natural "facade" way.
 * 
 */

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
 *
 * @author  Grios - angel.grios@gmail.com
 * @version 0.1
 * 
 * This class adapts any Object[][] matrix with a TableView.
 * 
 */
public final class DefaultTableAdapter extends AbstractTableAdapter
{    
    Object[][] data;
    boolean[] autoRender;
    boolean[] autoAlign;
    boolean autoAlignContentAll;
    boolean autoRenderAll;
   
    /**
     * An empty constructor.
     */
    public DefaultTableAdapter()
    {
        this(null, null);
    }
    
    /**
     * Constructs a new DefaultTableAdapter object by specifying the TableView object and the data in form of a Object[][] matrix.
     * 
     * Using this constructor the column names will apper as "Column 1", "Column 2", ..., and so on.
     * 
     * @param tableView The JavaFX TableView node.
     * @param data      The data that will be adapted into the specified TableView object.
     */
    public DefaultTableAdapter(TableView tableView, Object[][] data)
    {
        this(tableView, data, null);
    }
    
    /**
     * Constructs a new DefaultTableAdapter object by specifying the TableView object, the data in form of a Object[][] matrix and
     * the column names in the form of a Object[] array. 
     * 
     * @param tableView     The JavaFX TableView node.
     * @param data          The data that will be adapted into the specified TableView object.
     * @param columnNames   The column names
     */
    public DefaultTableAdapter(TableView tableView, Object[][] data, Object[] columnNames)
    {
        this(tableView, data, columnNames, false);
    }
    
    /**
     * Constructs a new DefaultTableAdapter object by specifying the TableView object, the data in form of a Object[][] matrix,
     * the column names in the form of a Object[] array and the auto align value.
     * @param tableView     The JavaFX TableView node.
     * @param data          The data that will be adapted into the specified TableView object.
     * @param columnNames   The column names
     * @param autoAlignContentAll  Specifies if the columns must be aligned depending on their content type.
     *                      The current content types accepted are: String, Number, Boolean and Image.
     */
    public DefaultTableAdapter(TableView tableView, Object[][] data, Object[] columnNames, boolean autoAlignContentAll)
    {
        this(tableView, data, columnNames, autoAlignContentAll, false);        
    }
    /**
     * Constructs a new DefaultTableAdapter object by specifying the TableView object, the data in form of a Object[][] matrix,
     * the column names in the form of a Object[] array, the auto align value and the auto render-all value.
     * @param tableView     The JavaFX TableView node.
     * @param data          The data that will be adapted into the specified TableView object.
     * @param columnNames   The column names
     * @param autoAlignContentAll  Specifies if the columns must be aligned depending on their content type.
     *                      The current content types accepted are: String, Number, Boolean and Image.
     * @param autoRenderAll Specifies if the column content must be rendered automatically. This is important for example because
     *                      boolean values are rendered as checkboxes and Image objects are drawed.
     */
    public DefaultTableAdapter(TableView tableView, Object[][] data, Object[] columnNames, boolean autoAlignContentAll, boolean autoRenderAll)
    {
        this.tableView = tableView;
        this.data = data;
        this.columnNames = columnNames;
        this.autoAlignContentAll = autoAlignContentAll;
        this.autoRenderAll = autoRenderAll;
        adapt();        
    }
    
    /**
     *  Set the auto-align content. If true, values inside cells are autoaligned
     *  depending of it's generic type:
     *      Numbers are aligned to RIGHT
     *      Strings and Objects are aligned to LEFT
     *      Booleans are aligned to CENTER.
     * @param value 
     */
    public void setAutoAlignContentAll(boolean value)
    {
        if (autoAlign == null) return;
        for (int i = 0; i < autoAlign.length; i++)
            autoAlign[i] = value;
    }

    /**
     *  Set the auto-align content to a specific column.
     * 
     * @param columnIndex   The desired column to auto-align
     *                      specified by their index.
     * @param value         The auto-align property value. If true,
     *                      all values on that column will be autoaligned 
     *                      depending of it's generic type:
     *                          Numbers are aligned to RIGHT
     *                          Strings and Objects are aligned to LEFT
     *                          Booleans are aligned to CENTER.
     */
    public void setAutoAlignContent(int columnIndex, boolean value)
    {
        autoAlign[columnIndex] = value;
    }
    
    /**
     * Allow to know the auto-align property for a specific column.
     * @param columnIndex
     * @return 
     */
    public boolean isAutoAlignContent(int columnIndex)
    {
        return autoAlign[columnIndex];
    }
    
    /**
     * Set the auto-render property.
     * If <code>true</code>, all cells will be autorendered depending of their
     * generic value.
     *      Booleans are rendered as check boxes.
     *      javafx.image.Image   values type are drawed.
     *      Numbers, Strings and Objects are rendered as text.
     * @param value 
     */
    public void setAutoRenderAll(boolean value)
    {
        if (autoRender == null) return;
        for (int i = 0; i < autoRender.length; i++)
            autoRender[i] = value;
    }
    
    /**
     * Set the auto-render property for a specific column.
     * If <code>true</code>, all cells will be autorendered depending of their
     * generic value.
     *      Booleans are rendered as check boxes.
     *      Numbers, Strings and Objects are rendered as text.
     * @param value 
     */
    public void setAutoRender(int columnIndex, boolean value)
    {
        autoRender[columnIndex] = value;
    }
    
    /**
     * Return the auto-render property value for an specific column.
     * @param columnIndex
     * @return 
     */
    public boolean isAutoRender(int columnIndex)
    {
        return autoRender[columnIndex];
    }

    @Override
    public void adapt()
    {        
        if (tableView == null || data == null)
            return;        
        
        initColumns(autoAlignContentAll, autoRenderAll);
        createRows();
        createCellValues();
        bind();
    }
    
    /**
     * Init the underlying data structures that are necessary to hold and
     * render the values that will be displayed into the TableView.
     * @param autoAlignContentAll
     * @param autoRenderAll 
     */
    private void initColumns(boolean autoAlignContentAll, boolean autoRenderAll)
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
            
            
            autoAlign = new boolean[columns.length];
            setAutoAlignContentAll(autoAlignContentAll);
            
            autoRender = new boolean[columns.length];
            setAutoRenderAll(autoRenderAll);
        }
    }
    
    private void createRows()
    {
        listRows = FXCollections.observableArrayList();
        if (data != null && data.length > 0)
            for (int i = 0; i < data.length; i++)
                listRows.add(new SimpleIntegerProperty(i));
    }
    
    private void createCellValues()
    {
        for (int i = 0; i < columns.length; i++)
        {
            final int columnIndex = i;
            columns[columnIndex].setCellValueFactory(new PropertyValueFactory<Object, Object>(""));
            columns[columnIndex].setCellFactory(new Callback<TableColumn<Object, Object>, Object>()
            {
                @Override
                public TableCell call(TableColumn<Object, Object> param)
                {
                    return new DefaultTableCellRender<Object, Object>(autoAlign[columnIndex], autoRender[columnIndex])
                    {                                                
                        @Override
                        protected void updateItem(Object item, boolean empty) 
                        {
                            Object value = null;
                            super.updateItem(item, empty);
                            if (getIndex() >= 0 && getIndex() < data.length)
                            {
                                value = data[getIndex()][columnIndex];
                                //setText(value == null ? "" : value.toString());
                                renderItem(value);
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
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        data[rowIndex][columnIndex] = value;
    }

    @Override
    public void setTableCellValue(TableCell<Object, Object> tableCell, int columnIndex)
    {        
        columns[columnIndex].setCellFactory(new Callback<TableColumn<Object, Object>, Object>()
        {

            @Override
            public TableCell call(TableColumn<Object, Object> param)
            {
                return tableCell;
            }            
        });
    }
    
    /**
    * Adds a new row to the TableView.
     * @param row
    */
   public void addRow(Object[] row)
   {
       Object[][] tmp = null;

       if (row == null)
           return;

      if (data == null)
           data = new Object[][]{{row}};
       else
       {
           if (row.length != columns.length)
              throw new RuntimeException("New row does not contains same columns number.");
           tmp = new Object[data.length + 1][columns.length];
          for (int i = 0; i < data.length; i++)
              for (int j = 0; j < data[i].length; j++)
                   tmp[i][j] = tmp[i][j];
           tmp[tmp.length - 1] = row;
            data = tmp;

       }
      adapt();
  }
    
    /**
     * Removes a row from the TableView.
     * @param rowIndex  The row number to be removed.
     */
    public void removeRow(int rowIndex)
    {
        Object[][] tmp = new Object[data.length - 1][columnNames.length];
        int rowNum = 0; 
        for (int i = 0; i < data.length; i++)
            if (i != rowNum)
                tmp[rowNum ++] = data[i];        
        data = tmp;
        adapt();
    }
    
    
    /**
     * Build and adapt a new DefaultTableAdapter object by specifying the TableView object and the data in form of a Object[][] matrix.
     * 
     * Using this method the column names will apper as "Column 1", "Column 2", ..., and so on.
     * 
     * @param tableView The JavaFX TableView node.
     * @param data      The data that will be adapted into the specified TableView object.
     */
    public static DefaultTableAdapter build(TableView tableView, Object[][] data)
    {
        return new DefaultTableAdapter(tableView, data, null);
    }
    
    /**
     * Build and adapt a new DefaultTableAdapter object by specifying the TableView object, the data in form of a Object[][] matrix and
     * the column names in the form of a Object[] array. 
     * 
     * @param tableView     The JavaFX TableView node.
     * @param data          The data that will be adapted into the specified TableView object.
     * @param columnNames   The column names
     */
    public static DefaultTableAdapter build(TableView tableView, Object[][] data, Object[] columnNames)
    {
        return new DefaultTableAdapter(tableView, data, columnNames, false);
    }
    
    /**
     * Build and adapt a <code>TableView</code> with a data source represented as
     * an objects matrix.
     * @param tableView             The <code>TableView</code> that will display
     *                              the data.
     * @param data                  The data to be displayed into the <code>TableView</code>.
     * @param columnNames           The values that will be displayed as the <code>TableView</code> headers.
     * @param autoAlignContentAll   The property to setup the auto-align property. 
     *                              If true, values inside cells are autoaligned
     *                              depending of it's generic type:
     *                                  Numbers are aligned to RIGHT
     *                                  Strings and Objects are aligned to LEFT
     *                                  Booleans are aligned to CENTER.
     *                              
     * @return  The <code>DefaultTableAdapter</code> object created.
     */
    public static DefaultTableAdapter build(TableView tableView, Object[][] data, Object[] columnNames, boolean autoAlignContentAll)
    {
        return new DefaultTableAdapter(tableView, data, columnNames, autoAlignContentAll, false);
    }
    
    /**
     * Build and adapt a <code>TableView</code> with a data source represented as
     * an objects matrix.
     * @param tableView             The <code>TableView</code> that will display
     * @param data                  The data to be displayed into the <code>TableView</code>.
     * @param columnNames           The values that will be displayed as the <code>TableView</code> headers.
     * @param autoAlignContentAll   The property to setup the auto-align property. 
     *                              If true, values inside cells are autoaligned
     *                              depending of it's generic type:
     *                                  Numbers are aligned to RIGHT
     *                                  Strings and Objects are aligned to LEFT
     *                                  Booleans are aligned to CENTER.
     * @param autoRenderAll         Specifies if the column content must be rendered automatically. 
     *                              This is important for example because boolean values 
     *                              are rendered as checkboxes and Image objects are drawed.      
     * @return 
     */
    public static DefaultTableAdapter build(TableView tableView, Object[][] data, Object[] columnNames, boolean autoAlignContentAll, boolean autoRenderAll)
    {
        return new DefaultTableAdapter(tableView, data, columnNames, autoAlignContentAll, autoRenderAll);
    }
}
