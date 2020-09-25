package facade;

import entities.Customer;
import entities.ItemType;
import entities.OrderLine;
import entities.Ordre;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class EntityFacade {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");

    public static void main(String[] args) {

        EntityFacade facade = new EntityFacade();

        Customer c1 = facade.createCustomer("Hans", "hans@coolmail.dk");
        Customer c2 = facade.createCustomer("Josefine", "jojo@yeehaw.com");

        Customer findC1 = facade.findCustomer(c1.getCustomer_id());
        System.out.println("findC1 name: " + findC1.getName());

        List<Customer> all = facade.getAllCustomers();
        for (Customer customer : all) {
            System.out.println("From getAllCustomers: " + customer.getName() + ", " + customer.getEmail());
        }

        ItemType i1 = facade.createItemType("Bread", "Very tasty", 10);
        ItemType i2 = facade.createItemType("Shoes", "Nice for walking outside", 500);
        ItemType i3 = facade.createItemType("Car", "Get to places fast", 50000);
        ItemType i4 = facade.createItemType("Window", "See right through it", 1500);

        System.out.println("Items: " + i1.getName() + ", "
                + i2.getName() + ", " + i3.getName() + ", " + i4.getName());

        ItemType foundItem = facade.findItemType(i3.getItem_id());
        System.out.println("This is a " + foundItem.getName() + " so it can " + foundItem.getDescription());

        Customer hasOrder = facade.addOrderToCustomer(c1.getCustomer_id());
        System.out.println("Customer: " + hasOrder.getName()
                + " now has an order with ID: " + hasOrder.getOrders().get(0).getOrder_id());

        facade.addOrderLineToOrder(1, i4.getItem_id(), 4);
        facade.addOrderLineToOrder(1, i2.getItem_id(), 8);
        facade.addOrderLineToOrder(1, i3.getItem_id(), 2);
        
        List<OrderLine> orderLines = facade.getAllCustomerOrders(c1.getCustomer_id()).get(0).getOrderLines();
        System.out.println("\nAll order lines for " + c1.getName() + ":\n");
        for (OrderLine ol : orderLines) {
            System.out.println(ol.getItem().getName() + ", " + ol.getQuantity() + " stk.");
        }
        
        facade.addOrderToCustomer(c2.getCustomer_id());
        facade.addOrderToCustomer(c2.getCustomer_id());
        
        facade.addOrderLineToOrder(2, i1.getItem_id(), 15);
        
        List<Ordre> orders = facade.getAllCustomerOrders(c2.getCustomer_id());
        System.out.println("\nAll orders for " + c2.getName() + ":\n");
        for (Ordre o : orders) {
            System.out.println(o.getCustomer().getName() + "'s order with ID: " + o.getOrder_id());
        }
        
        System.out.println("\nTotal price of " + c1.getName() + "'s order:\n");
        int total = facade.getTotalPriceOfOrder(1);
        System.out.println(total + " kr.");
    }

    public Customer createCustomer(String name, String email) {
        EntityManager em = emf.createEntityManager();
        Customer c = new Customer(name, email);
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            return c;
        } finally {
            em.close();
        }
    }

    public Customer findCustomer(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Customer c = em.find(Customer.class, id);
            return c;
        } finally {
            em.close();
        }
    }

    public List<Customer> getAllCustomers() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery q = em.createQuery("SELECT c FROM Customer c", Customer.class);
            List<Customer> customers = q.getResultList();
            return customers;
        } finally {
            em.close();
        }
    }

    public ItemType createItemType(String name, String description, int price) {
        EntityManager em = emf.createEntityManager();
        ItemType item = new ItemType(name, description, price);
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
            return item;
        } finally {
            em.close();
        }
    }

    public ItemType findItemType(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            ItemType item = em.find(ItemType.class, id);
            return item;
        } finally {
            em.close();
        }
    }

    public Customer addOrderToCustomer(int customerId) {
        EntityManager em = emf.createEntityManager();
        Customer c = em.find(Customer.class, customerId);
        Ordre order = new Ordre();
        try {
            em.getTransaction().begin();
            order.setCustomer(c);
            c.getOrders().add(order);
            em.persist(c);
            em.getTransaction().commit();
            return c;
        } finally {
            em.close();
        }
    }

    public Ordre addOrderLineToOrder(int orderId, int itemId, int quantity) {
        EntityManager em = emf.createEntityManager();
        Ordre o = em.find(Ordre.class, orderId);
        ItemType item = em.find(ItemType.class, itemId);
        OrderLine ol = new OrderLine(item, quantity);
        try {
            em.getTransaction().begin();
            o.getOrderLines().add(ol);
            ol.setOrdre(o);
            em.persist(o);
            em.getTransaction().commit();
            return o;
        } finally {
            em.close();
        }
    }

    public List<Ordre> getAllCustomerOrders(int customerId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery q = em.createQuery("SELECT o FROM Ordre o WHERE o.customer.customer_id = :id", Ordre.class);
            q.setParameter("id", customerId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getTotalPriceOfOrder(int orderId) {
        EntityManager em = emf.createEntityManager();
        int sum = 0;
        try {
            Query q = em.createQuery("SELECT ol FROM OrderLine ol WHERE ol.ordre.order_id = :id")
            .setParameter("id", orderId);
            List<OrderLine> orderLineItems = q.getResultList();
            for (OrderLine ol : orderLineItems) {
                int totalItemPrice = ol.getItem().getPrice() * ol.getQuantity();
                sum += totalItemPrice;
            }
            return sum;
        } finally {
            em.close();
        }
    }

}
