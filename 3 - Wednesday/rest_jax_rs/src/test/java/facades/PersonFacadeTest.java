package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import utils.EMF_Creator;
import entities.Person;
import exceptions.MissingInput;
import exceptions.PersonNotFound;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2;
    private static Address a1, a2;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        a1 = new Address("Gamlegade 3", 3822, "Guldborg");
        a2 = new Address("Sejvej 15", 3700, "Rønne");
        p1 = new Person("Erik", "Andersen", "83271281", a1);
        p2 = new Person("Maja", "Svensen", "66378212", a2);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAll").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    // Test if person actually exists (e.g. check for name)
    public void testAddPerson() throws MissingInput, PersonNotFound {
        long before = facade.getPersonCount();
        PersonDTO test = new PersonDTO("Test", "Testsen", "1234578", a1.getStreet(), a1.getZip(), a1.getCity());
        facade.addPerson(test);
        long after = facade.getPersonCount();
        assertTrue(before < after);
    }

    @Test
    public void testDeletePerson() throws PersonNotFound {
        PersonDTO deleted = facade.deletePerson(p1.getId());
        PersonNotFound thrown
                = assertThrows(PersonNotFound.class, () -> {
                    facade.getPerson(deleted.getId());
                });
        assertTrue(thrown.getMessage().equals("Person with the provided ID was not found."));
    }

    @Test
    public void testGetPerson() throws PersonNotFound {
        PersonDTO pTest = facade.getPerson(p2.getId());
        assertEquals(pTest.getFirstName(), p2.getFirstName());
    }

    @Test
    public void testGetAllPersons() {
        int expectedSize = 2;
        PersonsDTO persons = facade.getAllPersons();
        assertEquals(expectedSize, persons.getAll().size());
    }

    @Test
    public void testEditPerson() throws PersonNotFound, MissingInput {
        assertTrue(p1.getFirstName().equals("Erik"));
        p1.setFirstName("Anton");
        p1.setLastName("Jones");
        facade.editPerson(new PersonDTO(p1));
        assertTrue(facade.getPerson(p1.getId()).getFirstName().equals("Anton"));
    }

    @Test
    public void testGetPersonCount() {
        long count = facade.getPersonCount();
        assertTrue(count == 2);
    }

    @Test
    public void testPersonNotFoundException() {
        PersonNotFound thrown
                = assertThrows(PersonNotFound.class, () -> {
                    facade.getPerson(500);
                });
        assertTrue(thrown.getMessage().equals("Person with the provided ID was not found."));
    }
    
    @Test
    public void testMissingInputException() {
        PersonDTO pTest = new PersonDTO();
        pTest.setFirstName("Testerman");
        MissingInput thrown
                = assertThrows(MissingInput.class, () -> {
                    facade.addPerson(pTest);
                });
        assertTrue(thrown.getMessage().equals("First name, last name, phone or address info is missing."));
        
    }

}
