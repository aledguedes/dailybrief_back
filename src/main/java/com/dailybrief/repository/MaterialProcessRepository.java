package com.dailybrief.repository;

import com.dailybrief.model.MaterialProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialProcessRepository extends JpaRepository<MaterialProcess, Long> {

    /**
     * Encontra um registro de MaterialProcess pelo userId e taskId.
     * @param userId O ID do usuário.
     * @param taskId O ID da tarefa.
     * @return Um Optional contendo o MaterialProcess encontrado, ou vazio se não existir.
     */
    Optional<MaterialProcess> findByUserIdAndTaskId(String userId, String taskId);

    /**
     * Encontra um registro de MaterialProcess pelo taskId.
     * @param taskId O ID da tarefa.
     * @return Um Optional contendo o MaterialProcess encontrado, ou vazio se não existir.
     */
    Optional<MaterialProcess> findByTaskId(String taskId);

    /**
     * Lista todos os registros de MaterialProcess para um determinado userId.
     * @param userId O ID do usuário.
     * @return Uma lista de MaterialProcess.
     */
    List<MaterialProcess> findByUserId(String userId);

    /**
     * Lista todos os registros de MaterialProcess com base no status fornecido.
     * @param status O status das tarefas a serem filtradas.
     * @return Uma lista de MaterialProcess.
     */
    List<MaterialProcess> findByStatus(String status);
}
