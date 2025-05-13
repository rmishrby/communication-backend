package com.example.distribution.service;

import com.example.distribution.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchService {

    private static final Logger logger = LoggerFactory.getLogger(UserSearchService.class);

    private final EntityManager entityManager;

    public List<String> searchUsernames(String query, int pageNumber, int pageSize) {
        logger.info("Starting user search with query='{}', pageNumber={}, pageSize={}", query, pageNumber, pageSize);
        SearchSession searchSession = Search.session((Session) entityManager);
        int offset = (pageNumber - 1) * pageSize;
        List<String> results = searchSession.search(User.class)
                .select(f -> f.field("username", String.class))
                .where(f -> f.bool(b -> {
                    b.should(f.wildcard().field("username").matching("*" + query + "*"));
                    b.should(f.wildcard().field("email").matching("*" + query + "*"));
                }))
                .fetchHits(offset, pageSize);
        logger.info("Search returned {} usernames", results.size());
        return results;
    }
}
