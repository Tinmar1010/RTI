package Test_Class;

import BDaccess.BeanAccesBD;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class BeanAccesBDTest {

    private BeanAccesBD beanAccesBD;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        // Assurez-vous de configurer la base de données de test
        beanAccesBD = new BeanAccesBD("MySql", "localhost", "PourStudent", "Student", "PassStudent1_");
    }

    @Test
    public void testExecuteQuery() {
        try {
            // Testez l'exécution d'une requête SELECT
            ResultSet resultSet = beanAccesBD.executeQuery("SELECT * FROM clients where login = 'martin';");
            assertNotNull(resultSet); // Assurez-vous que le résultat de la requête n'est pas nul
        } catch (SQLException ex) {
            fail("Erreur SQL : " + ex.getMessage());
        }


    }

    @Test
    public void testExecuteUpdate() {
        try {
            // Testez l'exécution d'une requête UPDATE, INSERT, DELETE, etc.
            int updateResult = beanAccesBD.executeUpdate("Insert into clients value (null, 'test', 'test123');");
            assertTrue(updateResult >= 0); // Assurez-vous que la mise à jour a réussi
        } catch (SQLException ex) {
            fail("Erreur SQL : " + ex.getMessage());
        }
    }

    @After
    public void tearDown() throws SQLException {
        // Fermez la connexion à la base de données de test
        beanAccesBD.close();
    }
}
