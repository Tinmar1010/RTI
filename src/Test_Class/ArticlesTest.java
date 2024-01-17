package Test_Class;

import Classe_Metier.Articles;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArticlesTest {

    private Articles articles;

    @Before
    public void setUp() {
        // Créez une instance d'Articles pour chaque test
        articles = new Articles(1, 10, 5.0f, "example.jpg");
    }

    @Test
    public void testGetters() {
        // Vérifiez que les getters renvoient les valeurs attendues
        assertEquals(1, articles.getId());
        assertEquals(10, articles.getStock());
        assertEquals(5.0f, articles.getPrix(), 0.001); // Utilisez un delta pour les comparaisons de valeurs flottantes
        assertEquals("example.jpg", articles.getImage());
    }

    @Test
    public void testSetters() {
        // Modifiez les valeurs avec les setters
        articles.setId(2);
        articles.setStock(20);
        articles.setPrix(10.0f);
        articles.setImage("new_image.jpg");

        // Vérifiez que les setters ont bien fonctionné
        assertEquals(2, articles.getId());
        assertEquals(20, articles.getStock());
        assertEquals(10.0f, articles.getPrix(), 0.001);
        assertEquals("new_image.jpg", articles.getImage());
    }
}
