/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr4.tableadapter;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

/**
 *
 * @author Grios angel.grios@gmail.com
 */
public class DefaultTableCellRender<S, T> extends AbstractTableCellRender<S, T>
{
    
    private BufferedImage bimage = null;
    CheckBox checkBox;

    public DefaultTableCellRender(boolean autoAlign, boolean autoRender)
    {
        super(autoAlign, autoRender);
        
        checkBox = new CheckBox();
        checkBox.setStyle("-fx-opacity: 1");
        checkBox.setDisable(true);
    }
    
    @Override
    public void renderItem(T item)
    {
        if (item instanceof String)
            renderAsString((String) item);
        else if (item instanceof Number)
            renderAsNumber((Number) item);
        else if (item instanceof Boolean)
            renderAsBoolean((Boolean) item);
        else if (item instanceof Image)
            renderAsImage((Image) item);
        else if (item instanceof java.awt.Image)
            renderAsImage((java.awt.Image) item);
        else if (item instanceof Node)
            renderAsNode((Node) item);
        else
            renderAsObject(item);
    }
    
    private void renderAsString(String item)
    {
        setAlignment(Pos.CENTER_LEFT);
        setText(item);
    }
    
    private void renderAsNumber(Number item)
    {
        setAlignment(autoAlign ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        setText(item == null ? "" : item.toString());
    }
    
    private void renderAsBoolean(Boolean item)
    {
        //CheckBox checkBox = null;
        if (autoRender && item != null)
        {   
            setText("");
            setAlignment(Pos.CENTER);
            //checkBox = new CheckBox();            
            checkBox.setSelected(item);
            setGraphic(checkBox);
        }
        else if (item != null && autoAlign)
        {
            setAlignment(Pos.CENTER);
            setText(item.toString());
        }
        else if (item != null)
        {
            setAlignment(Pos.CENTER_LEFT);
            setText(item.toString());
        }
    }
    
    
    private void renderAsImage(Image item)
    {
        if (autoRender)
        {
            setText("");
            setAlignment(Pos.CENTER);
            setGraphic(new ImageView(item));            
        }
        else
        {
            setAlignment(Pos.CENTER_LEFT);
            setText(item.toString());
        }
    }
    
    private void renderAsImage(java.awt.Image item)
    {
        WritableImage wimage = null;
        
        if (autoRender && item != null)
        {
            if (item instanceof BufferedImage)
                bimage = (BufferedImage) item;
            else
            {
                try
                {
                    javax.swing.SwingUtilities.invokeAndWait(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            try
                            {
                                bimage = createBImage(item);
                            } 
                            catch (Exception e)
                            {
                                e.toString();
                                System.out.println("Program continues normally but image was not rendered...");
                            }
                        }
                    });
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    System.out.println("Program continues normally but image was not rendered...");
                }
                
            }
            if (bimage != null)
            {
                wimage = new WritableImage((int) wimage.getWidth(), (int) wimage.getHeight());
                SwingFXUtils.toFXImage(bimage, wimage);
                setGraphic(new ImageView(wimage));
                setText("");
            }
        }
        else
        {
            setAlignment(Pos.CENTER_LEFT);
            setText(item.toString());
        }
    }
    
    private void renderAsNode(Node item)
    {
        if (autoRender)
        {
            setAlignment(Pos.CENTER);
            setText("");
            setGraphic(item);
        }
        else if (item != null)
        {
            setAlignment(Pos.CENTER_LEFT);
            setText(item.toString());
        }
        else
            setText("");
    }
    
    private void renderAsObject(Object item)
    {
        setAlignment(Pos.CENTER_LEFT);
        setText(item == null ? "" : item.toString());
    }
    
    private BufferedImage createBImage(java.awt.Image image) throws Exception
    {
        BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
    
}
