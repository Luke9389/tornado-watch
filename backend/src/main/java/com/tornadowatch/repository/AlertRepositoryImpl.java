package com.tornadowatch.repository;

import com.tornadowatch.entity.Alert;
import com.tornadowatch.entity.AlertSeverity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlertRepositoryImpl implements AlertRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Alert> findAlertsWithFilters(String region, AlertSeverity severity, 
                                           LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder jpql = new StringBuilder("SELECT a FROM Alert a WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        List<String> paramNames = new ArrayList<>();
        
        if (region != null) {
            jpql.append(" AND a.region = ?").append(parameters.size() + 1);
            parameters.add(region);
            paramNames.add("region");
        }
        
        if (severity != null) {
            jpql.append(" AND a.severity = ?").append(parameters.size() + 1);
            parameters.add(severity);
            paramNames.add("severity");
        }
        
        if (startDate != null) {
            jpql.append(" AND a.issuedAt >= ?").append(parameters.size() + 1);
            parameters.add(startDate);
            paramNames.add("startDate");
        }
        
        if (endDate != null) {
            jpql.append(" AND a.issuedAt <= ?").append(parameters.size() + 1);
            parameters.add(endDate);
            paramNames.add("endDate");
        }
        
        TypedQuery<Alert> query = entityManager.createQuery(jpql.toString(), Alert.class);
        
        for (int i = 0; i < parameters.size(); i++) {
            query.setParameter(i + 1, parameters.get(i));
        }
        
        return query.getResultList();
    }
}