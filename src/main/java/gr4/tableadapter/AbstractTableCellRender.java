/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr4.tableadapter;

import javafx.scene.control.TableCell;

/**
 * @author  Grios angel.grios@gmail.com
 * @version 0.1
 * 
 * This class defines a generic way wich the TableView's cells must be rendered.
 */
public abstract class AbstractTableCellRender<S, T> extends TableCell<S, T>
{
    boolean autoAlign;
    boolean autoRender;
    
    /**
     * The base constructor. This constructor must be called on each AbstractTableCellRender&lt;S, T&gt; subclass.
     * @param autoAlign
     * @param autoRender 
     */
    public AbstractTableCellRender(boolean autoAlign, boolean autoRender)
    {
        this.autoAlign = autoAlign;
        this.autoRender = autoRender;
    }
    
    /**
     * This method must be implemented on each AbstractTableCellRender&lt;S, T&gt; subclass.
     * It must specify the way wich each item in the TableView must be rendered or presented.
     * @param item 
     */
    public abstract void renderItem(T item);
}
