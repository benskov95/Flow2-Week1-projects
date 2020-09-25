package facade;

import entity1.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class CustomerFacade {
    
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
    
    public Customer getCustomer(int id){
         EntityManager em = emf.createEntityManager();
        try{
            Customer customer = em.find(Customer.class,id);
            return customer;
        }finally {
            em.close();
        }
    }
     
     public List<Customer> getCustomers() {
        EntityManager em = emf.createEntityManager();
        try {
        Query q = em.createQuery("SELECT c FROM Customer c");
        List<Customer> customerList = q.getResultList();
        return customerList;
        } finally {
            em.close();
        }
     }
     
     public Customer addCustomer(Customer c){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            return c;
        }finally {
            em.close();
        }
    }
    
     public void deleteCustomer(int id) { 
         EntityManager em = emf.createEntityManager();
         try {
             em.getTransaction().begin();
             TypedQuery q = 
                     em.createQuery("DELETE FROM Customer c WHERE c.id = :id", Customer.class);
             q.setParameter("id", id);
             q.executeUpdate();
             em.getTransaction().commit();
         } finally {
             em.close();
         }    
     }
     
     // em.createQuery keeps complaining about syntax,
     // first time using update in JPQL so not sure how to fix it.
     public void editCustomer(Customer c) {
           EntityManager em = emf.createEntityManager();
         try {
             em.getTransaction().begin();
             TypedQuery q = 
                     em.createQuery("UPDATE Customer c SET c.firstName = :firstName, c.lastName ="
                             + " :lastName, c.addresses = :addresses WHERE c.id = :id", Customer.class);
             q.setParameter("firstName", c.getFirstName());
             q.setParameter("lastName", c.getLastName());
             q.setParameter("addresses", c.getAddresses());
             q.setParameter("id", c.getId());
             q.executeUpdate();
             em.getTransaction().commit();
         } finally {
             em.close();
         }
     }
    
}
