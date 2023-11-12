package Test_Class;

import Classe_Metier.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        // Créez une instance de User pour chaque test
        user = new User();
    }

    @Test
    public void testGetters() {
        // Vérifiez que les getters renvoient les valeurs attendues
        assertEquals("", user.getLogin());
        assertEquals("", user.getMdp());
    }

    @Test
    public void testSetters() {
        // Modifiez les valeurs avec les setters
        user.setLogin("utilisateur123");
        user.setMdp("motdepasse123");

        // Vérifiez que les setters ont bien fonctionné
        assertEquals("utilisateur123", user.getLogin());
        assertEquals("motdepasse123", user.getMdp());
    }
}
