package com.klef.jfsd.exam;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;

public class ClientDemo {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Customer.class)
                .buildSessionFactory();

        Session session = sessionFactory.openSession();

        try {
            // Insert records
            session.beginTransaction();
            Customer customer1 = new Customer();
            customer1.setName("Alice");
            customer1.setEmail("alice@example.com");
            customer1.setAge(25);
            customer1.setLocation("New York");

            Customer customer2 = new Customer();
            customer2.setName("Bob");
            customer2.setEmail("bob@example.com");
            customer2.setAge(30);
            customer2.setLocation("California");

            session.persist(customer1);
            session.persist(customer2);
            session.getTransaction().commit();

            // Query using Criteria API
            session.beginTransaction();
            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            JpaRoot<Customer> root = criteriaQuery.from(Customer.class);

            // Example: Apply restrictions
            criteriaQuery.select(root).where(criteriaBuilder.greaterThan(root.get("age"), 25));

            List<Customer> customers = session.createQuery(criteriaQuery).getResultList();
            customers.forEach(System.out::println);

            session.getTransaction().commit();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}
