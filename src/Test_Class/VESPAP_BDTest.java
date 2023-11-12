package Test_Class;

import BDaccess.VESPAP_BD;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VESPAP_BDTest {

    private VESPAP_BD vespapBd;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        // Assurez-vous de configurer la base de données de test
        vespapBd = new VESPAP_BD("MySql", "localhost", "PourStudent", "Student", "PassStudent1_");
    }

    @Test
    public void testLogin() {
        try {
            // Testez la création d'un nouvel utilisateur
            int createResult = vespapBd.Login("nouvelutilisateur",  "motdepasse", true);
            assertTrue(createResult >= 0); // Assurez-vous que la création a réussi

            // Testez la connexion d'un utilisateur existant
            int loginResult = vespapBd.Login("martin", "test123", false);
            assertEquals(1, loginResult); // Assurez-vous que la connexion a réussi
        } catch (SQLException ex) {
            fail("Erreur SQL : " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
/*
    @Test
    public void testGetFactures() {
        try {
            // Testez la récupération des factures pour un client donné
            ArrayList<Factures> factures = vespapBd.Get_Factures(1, beanAccesBD);
            assertNotNull(factures); // Assurez-vous que la liste des factures n'est pas nulle
            assertFalse(factures.isEmpty()); // Assurez-vous que la liste des factures n'est pas vide
        } catch (SQLException ex) {
            fail("Erreur SQL : " + ex.getMessage());
        }
    }

    @Test
    public void testPayFacture() {
        try {
            // Testez le paiement d'une facture
            int paymentResult = vespapBd.Pay_Facture(1, "nom_client", "num_visa", beanAccesBD);
            assertEquals(0, paymentResult); // Dans votre implémentation actuelle, cela retourne toujours 0
        } catch (SQLException ex) {
            fail("Erreur SQL : " + ex.getMessage());
        }
    }*/

    @After
    public void tearDown() throws SQLException {
        // Fermez la connexion à la base de données de test
        vespapBd.bdacces.close();
    }
}
