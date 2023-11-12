package Test_Class;

import Classe_Metier.Factures;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FacturesTest {

    private Factures factures;

    @Before
    public void setUp() {
        // Créez une instance de Factures pour chaque test
        factures = new Factures();
    }

    @Test
    public void testGetters() {
        // Vérifiez que les getters renvoient les valeurs attendues
        assertEquals(0, factures.getIdFactures());
        assertEquals(new Date(), factures.getDate());
        assertEquals(0.0f, factures.getMontant(), 0.001); // Utilisez un delta pour les comparaisons de valeurs flottantes
        assertFalse(factures.isPaye());
    }

    @Test
    public void testSetters() {
        // Modifiez les valeurs avec les setters
        factures.setIdFactures(1);
        factures.setDate(new Date(2023, 11, 2)); // Remplacez cette date par une date appropriée
        factures.setMontant(100.0f);
        factures.setPaye(true);

        // Vérifiez que les setters ont bien fonctionné
        assertEquals(1, factures.getIdFactures());
        // Ajoutez des assertions pour vérifier les autres valeurs modifiées
    }
}
