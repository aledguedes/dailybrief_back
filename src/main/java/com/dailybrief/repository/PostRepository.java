package com.dailybrief.repository;

import com.dailybrief.model.Post;
import com.dailybrief.model.PostStatus;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    long count();
 // long countAll();
    long countByStatus(PostStatus status);
    List<Post> findAllByOrderByPublishedAtDesc(Pageable pageable);

    // Se no futuro você precisar de contagens por status, pode adicionar:
    // /**
    // * Conta o número de posts para cada status.
    // * Exemplo de retorno: [["APROVADO", 10], ["PENDENTE", 5]]
    // * @return Uma lista de arrays de objetos, onde cada array contém o status
    // (String) e a contagem (Long).
    // */
    // @Query("SELECT p.status, COUNT(p) FROM Post p GROUP BY p.status")
    // List<Object[]> countPostsByStatus();
}
