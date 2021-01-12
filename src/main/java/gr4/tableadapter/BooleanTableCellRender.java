/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr4.tableadapter;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

/**
 *
 * @author  Grios angel.grios@gmail.com
 * @version 0.1
 * 
 * This class render boolean items as a checkbox.
 */
public class BooleanTableCellRender<S, T> extends TableCell<S, T>
{   
    protected CheckBox valueRender;
    public BooleanTableCellRender()
    {
        super();
        valueRender = new CheckBox();
        valueRender.setDisable(true);
        valueRender.setOpacity(1);
        setAlignment(Pos.CENTER);
    }
}
