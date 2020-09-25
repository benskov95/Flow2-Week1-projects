package test;

import entity1.Customer;
import entity2.Address;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Tester {
    
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
    
    public static void main(String[] args) {
//        Persistence.generateSchema("pu", null);
        addCustomers();
    }
    
    public static void addCustomers() {
        EntityManager em = emf.createEntityManager();
        Customer c1 = new Customer("Ulrik", "Sværdgaard");
        Customer c2 = new Customer("Louise", "Svensen");
        
        Address a1 = new Address("Trægade 9", "Rønne");
        Address a2 = new Address("Månevej 93", "Hasle");
        Address a3 = new Address("Rabalderstræde 3", "København");
        Address a4 = new Address("Kogade 22", "Silkeborg");
        c1.addAddress(a1);
        c1.addAddress(a2);
        c2.addAddress(a3);
        c2.addAddress(a4);
        // This one is for exercise 5 specifically, to test if both can have the same address.
        c1.addAddress(a4);
        // These are for exercise 4
//        a1.setCustomer(c1);
//        a2.setCustomer(c1);
//        a3.setCustomer(c2);
//        a4.setCustomer(c2);
        
        try {
            em.getTransaction().begin();
            em.persist(c1);
            em.persist(c2);
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.persist(a4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
}
