package org.example.Database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class GenericHibernate<T> {

    private final EntityManagerFactory entityManagerFactory;
    private final Class<T> entityClass;
    public GenericHibernate(EntityManagerFactory entityManagerFactory, Class<T> entityClass){
        this.entityClass = entityClass;
        this.entityManagerFactory = entityManagerFactory;
    }

    public void create(T entity){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try{
            transaction.begin();
            entityManager.persist(entity);
            transaction.commit();
        }catch (RuntimeException exception){
            if (transaction.isActive()){
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }

    }

    public T findById(int id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return  entityManager.find(entityClass, id);
        } finally {
            entityManager.close();
        }
    }

    public T update(T entity){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            T updatedEntity = entityManager.merge(entity);
            transaction.commit();
            return updatedEntity;
        } catch (RuntimeException exception) {
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    public void delete(int id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            T entity = entityManager.find(entityClass, id);
            if(entity != null){
                entityManager.remove(entity);
            }
            transaction.commit();
        } catch (RuntimeException exception){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }


}
