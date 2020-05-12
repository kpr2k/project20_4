
import gr4.dane;
import org.junit.Assert;
import org.junit.Test;

public class sampleTest {

    @Test
    public void testLength() {
        dane dane = new dane();
        dane.odczytajPlik("src/test/resources/przyklad1.csv");
        Assert.assertEquals(25,dane.daneOdczytane.length);
    }
    @Test
    public  void testNoNull() {
        dane dane = new dane();
        dane.odczytajPlik("src/test/resources/przyklad1.csv");
        for(int i = 0; i<dane.daneOdczytane.length; i++)
        {
            for(int j = 0; j<dane.daneOdczytane[i].length; j++){
               Assert.assertFalse(dane.daneOdczytane[i][j]==null);
            }
        }
    }

}
