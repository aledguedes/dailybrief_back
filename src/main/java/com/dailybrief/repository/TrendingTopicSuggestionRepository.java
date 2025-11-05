package com.dailybrief.repository;

import com.dailybrief.model.TrendingTopicSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrendingTopicSuggestionRepository extends JpaRepository<TrendingTopicSuggestion, Long> {
    Optional<TrendingTopicSuggestion> findByTopicName(String topicName);

    List<TrendingTopicSuggestion> findByStatus(String status);
}