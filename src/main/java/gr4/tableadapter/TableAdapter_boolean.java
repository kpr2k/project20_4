/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Grios angel.grios@gmail.com
 */
public class TableAdapter_boolean extends AbstractTableAdapter
{
    boolean[][] data;
    boolean[] autoRender;
    boolean autoRenderAll;
    
    public TableAdapter_boolean()
    {
        this(null, null);
    }
    
    public TableAdapter_boolean(TableView tableView, boolean[][] data)
    {
        this(tableView, data, null);
    }
    
    public TableAdapter_boolean(TableView tableView, boolean[][] data, Object[] columnNames)
    {
        this(tableView, data, columnNames, true);
    }
    
    public TableAdapter_boolean(TableView tableView, boolean[][] data, Object[] columnNames, boolean autoRenderAll)
    {
        this.tableView = tableView;
        this.data = data;
        this.columnNames = columnNames;
        this.autoRenderAll = autoRenderAll;
    }
       
    
    @Override
    public Boolean getValueAt(int rowIndex, int columnIndex)
    {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        data[rowIndex][columnIndex] = (Boolean) value;
    }

    @Override
    public void setTableCellValue(TableCell<Object, Object> tableCell, int columnIndex)
    {
        tableCellValues[columnIndex] = tableCell;
    }

    @Override
    public void adapt()
    {
        initColumns();
        createRows();
        createCellValues();
        bind();
    }
    
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
                for (int i = 0; i < autoRender.length; i++)
                    autoRender[i] = true;
            
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
