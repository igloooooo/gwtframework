package com.iglooit.core.lib.server.hibernate;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

/**
 * this class is a workaround for the issue that find and getReference are not working as expected.
 * The behaviour we are seeing is that the initial reference is returned, but related collections cannot be
 * lazy loaded. This issue has been flagged to come back to.
 */
public class JpaTemplateWrapperFactory
{
    public static JpaTemplate getJpaTemplate(EntityManagerFactory emf)
    {
        //todo mg: figure out why hibernate is misbehaving on find() and getReference()
        //return new JpaTemplate(emf);

        return new JpaTemplate(emf)
        {
            @Override
            public <T> T getReference(final Class<T> entityClass, final Object id)
            {
                //return super.getReference(entityClass, id);
                List<T> hits = super.executeFind(new JpaCallback<List<T>>()
                {
                    public List<T> doInJpa(EntityManager entityManager)
                    {
                        Query q = entityManager.createQuery(
                            "select p from " + entityClass.getSimpleName() + " p " +
                                "where p.id = :id")
                            .setParameter("id", id);
                        return q.getResultList();
                    }
                });
                if (hits == null || hits.size() == 0)
                    throw new EmptyResultDataAccessException("unable to find reference for class='" +
                        entityClass.getSimpleName() + "', id='" + id + "'", 1);
                else if (hits.size() > 1)
                    throw new IncorrectResultSizeDataAccessException("found multiple references for class='" +
                        entityClass.getSimpleName() + "', id='" + id + "'", 1, hits.size());
                else
                    return hits.get(0);
            }

            @Override
            public <T> T find(final Class<T> entityClass, final Object id)
            {
                //return super.getReference(entityClass, id);
                List<T> hits = super.executeFind(new JpaCallback<List<T>>()
                {
                    public List<T> doInJpa(EntityManager entityManager)
                    {
                        Query q = entityManager.createQuery(
                            "select p from " + entityClass.getSimpleName() + " p " +
                                "where p.id = :id")
                            .setParameter("id", id);
                        return q.getResultList();
                    }
                });
                if (hits == null || hits.size() == 0)
                    return null;
                else
                    return hits.get(0);
            }
        };


    }
}
