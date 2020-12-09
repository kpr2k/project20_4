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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;

/**
 *
 * @author Grios angel.grios@gmail.com
 */
public class ResultSetTableAdapter extends AbstractTableAdapter
{
    ResultSet data;
    boolean[] autoRender;
    boolean[] autoAlign;
    boolean autoAlignContentAll;
    boolean autoRenderAll;
    int totalRows;
    
    public ResultSetTableAdapter()
    {
        this(null, null);
    }
    
    public ResultSetTableAdapter(TableView tableView, ResultSet data)
    {
        this.tableView = tableView;
        this.data = data;
        this.columnNames = getColumnNamesFromMetadata(data);
        this.autoAlignContentAll = false;
        this.autoRenderAll = false;
        adapt();    
    }
    
    public ResultSetTableAdapter(TableView tableView, ResultSet data, Object[] columnNames)
    {
        this(tableView, data, columnNames, false);
    }
    
    public ResultSetTableAdapter(TableView tableView, ResultSet data, Object[] columnNames, boolean autoalignAll)
    {
        this(tableView, data, columnNames, autoalignAll, false);        
    }
    
    public ResultSetTableAdapter(TableView tableView, ResultSet data, Object[] columnNames, boolean autoAlignContentAll, boolean autoRenderAll)
    {
        this.tableView = tableView;
        this.data = data;
        this.columnNames = columnNames;
        this.autoAlignContentAll = autoAlignContentAll;
        this.autoRenderAll = autoRenderAll;
        adapt();        
    }
    
    public void setAutoAlignContentAll(boolean value)
    {
        if (autoAlign == null) return;
        for (int i = 0; i < autoAlign.length; i++)
            autoAlign[i] = value;
    }

    public void setAutoAlignContent(int columnIndex, boolean value)
    {
        autoAlign[columnIndex] = value;
    }
    
    public boolean isAutoAlignContent(int columnIndex)
    {
        return autoAlign[columnIndex];
    }
    
    public void setAutoRenderAll(boolean value)
    {
        if (autoRender == null) return;
        for (int i = 0; i < autoRender.length; i++)
            autoRender[i] = value;
    }
    
    public void setAutoRender(int columnIndex, boolean value)
    {
        autoRender[columnIndex] = value;
    }
    
    public boolean isAutoRender(int columnIndex)
    {
        return autoRender[columnIndex];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        try
        {
            data.absolute(rowIndex);
            return data.getObject(columnIndex + 1);
        } 
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    
    
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
       throw new UnsupportedOperationException("ResultSetTableAdapter does not support setValueAt(Object value, int rowIndex, int columnIndex) method.");
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
    
    public void adapt(ResultSet rs)
    {
        this.data = rs;
        adapt();
    }
    
    public void adapt(ResultSet rs, String[] columnNames)
    {
        this.data = rs;
        this.columnNames = columnNames;
        adapt();
    }
    
    @Override
    public void adapt()
    {
        initColumns(autoAlignContentAll, autoRenderAll);
        createRows();
        createCellValues();
        bind();
    }
    
    private void initColumns(boolean autoAlignContentAll, boolean autoRenderAll)
    {
        Object colNameTemp = null;
        if (data == null || columns == null || columns.length < 1)
            return;
        else
        {           
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
        int counter = 0;
        listRows = FXCollections.observableArrayList();
       
        try
        {
            data.beforeFirst();
            while (data.next())
                listRows.add(new SimpleIntegerProperty(counter ++));
        } 
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        
    }
    
    protected void createCellValues()
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
                            if (getIndex() >= 0 && getIndex() < listRows.size())
                            {
                                try
                                {
                                    data.absolute(getIndex() + 1);
                                    value = data.getObject(columnIndex + 1);
                                } 
                                catch (Exception e)
                                {
                                    throw new RuntimeException(e);
                                }
                                //value = data[getIndex()][columnIndex];
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
    
    private String[] getColumnNamesFromMetadata(ResultSet rs)
    {
        ResultSetMetaData rsm = null;
        String[] columns = null;
        int tc = 0;
        try
        {
            rsm = rs.getMetaData();
            if ((tc = rsm.getColumnCount()) > 0)
            {
                columns = new String[tc];
                for (int i = 0; i < columns.length; i++)
                    columns[i] = rsm.getColumnName(i + 1);
                return columns;
            }
            return null;
        } 
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
}
