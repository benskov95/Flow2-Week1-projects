package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.MissingInput;
import exceptions.PersonNotFound;
import interfaces.IPersonFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class PersonFacade implements IPersonFacade {
    
    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    
       public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    @Override
    public PersonDTO addPerson(PersonDTO pDTO) throws MissingInput {
        EntityManager em = getEntityManager();
        Address address = new Address(pDTO.getStreet(), pDTO.getZip(), pDTO.getCity());
        Person p = new Person(pDTO.getFirstName(), pDTO.getLastName(), pDTO.getPhone(), address);
        
        try {
            hasInput(pDTO, address);
            addAddress(em, address, p);
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            return new PersonDTO(p);
        } finally {
        em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFound {
        EntityManager em = getEntityManager();
        Person toBeDeleted = em.find(Person.class, id);
        List<Person> testHasSameAddress = new ArrayList();
        try {
            determineDeletionProcess(em, toBeDeleted, testHasSameAddress, id);
            return new PersonDTO(toBeDeleted);
        } catch (Exception e) {
             throw new PersonNotFound("Could not be deleted, person with the provided ID does not exist.");
        } finally {
            em.close();
        }
    }


    @Override
    public PersonDTO getPerson(int id) throws PersonNotFound {
        EntityManager em = getEntityManager();
        try {
            TypedQuery q = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class);
            q.setParameter("id", id);
            return new PersonDTO((Person) q.getSingleResult());
        } catch (Exception e) {
            throw new PersonNotFound("Person with the provided ID was not found.");
        } finally {
            em.close();
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery q = em.createQuery("SELECT p FROM Person p", Person.class);
            return new PersonsDTO(q.getResultList());
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO pDTO) throws PersonNotFound, MissingInput {
        EntityManager em = getEntityManager();
        Address diffAddress = new Address(pDTO.getStreet(), pDTO.getZip(), pDTO.getCity());
        try {
            hasInput(pDTO, diffAddress);
            Person p = createOrUseAddress(em, pDTO, diffAddress);
            deleteUnusedAddress(em);
            return new PersonDTO(p);
        } catch (Exception e) {
             throw new PersonNotFound("Person with the provided ID could not be updated because the person does not exist.");
        } finally {
            em.close();
        }
    }

    public long getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            long count = (long)em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return count;
        } finally {
            em.close();
        }
    }
    
    private void addAddress(EntityManager em, Address address, Person p) {
        Query q = em.createQuery("SELECT a FROM Address a");
        List<Address> addresses = q.getResultList();
        
        for (Address a : addresses) {
            if (a.getStreet().equals(address.getStreet()) 
                    && a.getZip() == address.getZip() 
                    && a.getCity().equals(address.getCity())) {
                p.setAddress(a);
            }
        }
    }

    private void hasInput(PersonDTO pDTO, Address address) throws MissingInput {
        if (pDTO.getFirstName() == null || pDTO.getLastName() == null 
                || pDTO.getPhone() == null || address.getStreet() == null
                || address.getZip() < 1000 || address.getCity() == null) {
            throw new MissingInput("First name, last name, phone or address info is missing.");
        }
    }
    
    private void determineDeletionProcess(EntityManager em, Person p, List<Person> sameAdr, int id) {
        em.getTransaction().begin();
        TypedQuery q1 = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = q1.getResultList();
        em.getTransaction().commit();
        
        for (Person person : persons) {
            if (person.getAddress().getId() == person.getAddress().getId()) {
                sameAdr.add(person);
            }
        }
        
        if (sameAdr.size() > 1) {
            em.getTransaction().begin();
            Query q2 = em.createQuery("DELETE FROM Person p WHERE p.id = :id");
            q2.setParameter("id", id);
            q2.executeUpdate();
            em.getTransaction().commit();
        } else {
            em.getTransaction().begin();
            Query q3 = em.createQuery("DELETE FROM Person p WHERE p.id = :id");
            Query q4 = em.createQuery("DELETE FROM Address a WHERE a.id = :a_id");
            q3.setParameter("id", id);
            q4.setParameter("a_id", p.getAddress().getId());
            q3.executeUpdate();
            q4.executeUpdate();
            em.getTransaction().commit();
        }
    }
    
    private void deleteUnusedAddress(EntityManager em) {
        Query q2 = em.createQuery("SELECT p.address.id FROM Person p");
        Query q3 = em.createQuery("SELECT a.id FROM Address a");
        
        List<Integer> pAddressIDs = q2.getResultList();
        List<Integer> addressIDs = q3.getResultList();
        
        for (Integer addressID : addressIDs) {
            if (!pAddressIDs.contains(addressID)) {
                em.getTransaction().begin();
                Query q4 = em.createQuery("DELETE FROM Address a WHERE a.id = :a_id")
                        .setParameter("a_id", addressID);
                q4.executeUpdate();
                em.getTransaction().commit();
                break;
            }
        }
    }

    private Person createOrUseAddress(EntityManager em, PersonDTO pDTO, Address diffAddress) {
        Query q1 = em.createQuery
                        ("SELECT a FROM Address a WHERE "
                                + "a.street = :street AND a.zip"
                                + " = :zip AND a.city = :city")
                .setParameter("street", pDTO.getStreet())
                .setParameter("zip", pDTO.getZip())
                .setParameter("city", pDTO.getCity());
        List<Address> existingAddresses = q1.getResultList();
        em.getTransaction().begin();
        Person p = em.find(Person.class, pDTO.getId());
        p.setFirstName(pDTO.getFirstName());
        p.setLastName(pDTO.getLastName());
        p.setPhone(pDTO.getPhone());
        p.setLastEdited(new Date());
        if (existingAddresses.isEmpty()) {
            p.setAddress(diffAddress);
        } else {
            p.setAddress(existingAddresses.get(0));
        }
        em.getTransaction().commit();
        return p;
    }
    
    
}
