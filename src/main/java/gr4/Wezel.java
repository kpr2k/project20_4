package gr4;

import javafx.scene.chart.XYChart;

public class Wezel {
    public XYChart.Data data;
    public int indeks;
    public int uczacy;

    public Wezel(double x, double y, int i){
        data = new XYChart.Data(x, y);
        indeks = i;
        uczacy = 0;
    }

    public Object getXValue(){
        return data.getXValue();
    }

    public Object getYValue(){
        return data.getYValue();
    }

    public XYChart.Data getData() {
        return data;
    }


}
