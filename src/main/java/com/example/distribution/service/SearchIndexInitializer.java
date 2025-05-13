package com.example.distribution.service;

import com.example.distribution.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class SearchIndexInitializer {


    @PersistenceContext
    private EntityManager entityManager;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeHibernateSearch() throws InterruptedException {
        getSearchSession(entityManager)
                .massIndexer()
                .startAndWait();
    }

    protected org.hibernate.search.mapper.orm.session.SearchSession getSearchSession(EntityManager entityManager) {
        return Search.session(entityManager);
    }
}