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
public class DoubleTableAdapter extends AbstractTableAdapter
{
    Double[][] data;
    
    public DoubleTableAdapter()
    {
        this(null, null);
    }
    
    public DoubleTableAdapter(TableView tableView, Double[][] data)
    {
        this(tableView, data, null);
    }
    
    public DoubleTableAdapter(TableView tableView, Double[][] data, Object[] columnNames)
    {
        this.tableView = tableView;
        this.data = data;
        this.columnNames = columnNames;
    }
       
    
    @Override
    public Double getValueAt(int rowIndex, int columnIndex)
    {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        data[rowIndex][columnIndex] = (Double) value;
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
                    return new NumberTableCellRender<Object, Object>()
                    {                        
                        
                        @Override
                        protected void updateItem(Object item, boolean empty) 
                        {
                            Object value = null;
                            super.updateItem(item, empty);
                            if (getIndex() >= 0 && getIndex() < data.length)
                            {
                                value = data[getIndex()][columnIndex];
                                setText(value == null ? "" : value.toString());                               
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
