/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr4.tableadapter;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

/**
 *
 * @author Grios angel.grios@gmail.com
 */
public class NumberTableCellRender<S, T> extends TableCell<S, T>
{

    public NumberTableCellRender()
    {
        super();
        setAlignment(Pos.CENTER_RIGHT);
    }
}
