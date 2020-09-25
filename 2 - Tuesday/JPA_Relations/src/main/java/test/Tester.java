package test;

import entity.Customer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


public class Tester {
    
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
    
    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        Customer c1 = new Customer("Hans", "Larsen");
        Customer c2 = new Customer("Rikke", "Mogensen");
        c1.addHobby("Playing football");
        c1.addHobby("Fixing cars");
        c1.addHobby("Bowling");
        c2.addHobby("Swimming");
        c2.addHobby("Playing guitar");
        c1.addPhone("81273121", "work");
        c1.addPhone("17324512", "home");
        c2.addPhone("73482312", "home");
        c2.addPhone("99283712", "work");
        
        try {
        em.getTransaction().begin();
        em.persist(c1);
        em.persist(c2);
        em.getTransaction().commit();
        } finally {
            em.close();
        }   
    }
}
