package com.dailybrief.service.impl;

import com.dailybrief.dto.dashboard.*;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.mapper.LogMapper;
import com.dailybrief.mapper.PostMapper;
import com.dailybrief.repository.LogRepository;
import com.dailybrief.repository.PostRepository;
import com.dailybrief.model.PostStatus;
import com.dailybrief.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private PostMapper postMapper;

    @Override
    public DashboardResponseDTO getDashboardData() {

        DashboardAnalyticsDTO analytics = collectAnalyticsData();

        // Limitar recentPosts aos últimos 5 (já está assim)
        List<DashboardPostResponseDTO> recentPosts = postRepository
                .findAllByOrderByPublishedAtDesc(PageRequest.of(0, 5))
                .stream()
                .map(postMapper::toDashboardPostResponse)
                .collect(Collectors.toList());

        // Limitar recentLogs aos últimos 5
        List<LogResponseDTO> recentLogs = logRepository
                .findAllByOrderByTimestampDesc(PageRequest.of(0, 5)) // Ajustado para 5
                .stream()
                .map(logMapper::toResponse)
                .collect(Collectors.toList());

        return new DashboardResponseDTO(analytics, recentPosts, recentLogs);
    }

    private DashboardAnalyticsDTO collectAnalyticsData() {

        // 1. Contagens de Posts por Status (DINÂMICO do DB)
        Long totalPostsPublished = postRepository.count(); // Total geral
        Long totalPostsApproved = postRepository.countByStatus(PostStatus.APPROVED);
        Long totalPostsPending = postRepository.countByStatus(PostStatus.PENDING);
        Long totalPostsRejected = postRepository.countByStatus(PostStatus.REJECTED);

        // 2. Visualizações e Cliques de Afiliados (Simulando com dados fixos e % de mudança)
        // Valores atuais e percentagens (mockados)
        MetricDataDTO totalPageviews = new MetricDataDTO(12500L, 12.0); // 12500, +12%
        MetricDataDTO totalAffiliateClicks = new MetricDataDTO(3200L, 8.5); // 3200, +8.5%
        MetricDataDTO totalConversions = new MetricDataDTO(150L, 5.0); // Exemplo de conversões (150, +5%)

        Long activeUsers = 1870L; // Manter fixo por enquanto

        // Dados diários (manter mockados)
        List<DashboardDailyDataPoint> dailyPageviews = List.of(
                new DashboardDailyDataPoint(LocalDate.now().minusDays(4), 2000L),
                new DashboardDailyDataPoint(LocalDate.now().minusDays(3), 2500L),
                new DashboardDailyDataPoint(LocalDate.now().minusDays(2), 3000L),
                new DashboardDailyDataPoint(LocalDate.now().minusDays(1), 2800L),
                new DashboardDailyDataPoint(LocalDate.now(), 2700L));

        List<DashboardDailyDataPoint> dailyAffiliateClicks = List.of(
                new DashboardDailyDataPoint(LocalDate.now().minusDays(4), 500L),
                new DashboardDailyDataPoint(LocalDate.now().minusDays(3), 600L),
                new DashboardDailyDataPoint(LocalDate.now().minusDays(2), 700L),
                new DashboardDailyDataPoint(LocalDate.now().minusDays(1), 650L),
                new DashboardDailyDataPoint(LocalDate.now(), 680L));

        // Fontes de tráfego (manter mockadas)
        List<DashboardTrafficSource> trafficSources = List.of(
                new DashboardTrafficSource("Orgânico", 45.0),
                new DashboardTrafficSource("Social", 25.0),
                new DashboardTrafficSource("Referência", 20.0),
                new DashboardTrafficSource("Direto", 10.0));

        // Retornar o novo DashboardAnalyticsDTO
        return new DashboardAnalyticsDTO(
                totalPageviews,
                totalAffiliateClicks,
                totalConversions, // Adicionado
                totalPostsPublished,
                activeUsers,
                totalPostsApproved, // Adicionado
                totalPostsPending,  // Adicionado
                totalPostsRejected, // Adicionado
                dailyPageviews,
                dailyAffiliateClicks,
                trafficSources
        );
    }
}
