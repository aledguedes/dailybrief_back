package com.dailybrief.service;

import com.dailybrief.dto.MaterialProcessResponseDTO;
import com.dailybrief.dto.GenerateContentManualRequestDTO;
import com.dailybrief.dto.SubmitFinalPostRequestDTO;
import com.dailybrief.dto.AutomationResponseDTO;
import com.dailybrief.dto.ImageGenerationResponseDTO;

import java.util.List;
import java.util.Optional;

public interface MaterialProcessService {

    /**
     * Inicia o processo de automação no Python para um AutomationRequest,
     * cria um registro inicial de MaterialProcess no PostgreSQL e o atualiza
     * com o taskId retornado pelo Python.
     * 
     * @param userId               O ID do usuário que iniciou a requisição.
     * @param automationRequestDto O DTO do AutomationRequest.
     * @param jwtToken             O token JWT para autenticação na API Python.
     * @return O DTO do MaterialProcess salvo e atualizado com o taskId do Python.
     */
    MaterialProcessResponseDTO initiateAutomationRequest(String userId, AutomationResponseDTO automationRequestDto,
            String jwtToken);

    /**
     * Busca os detalhes de um MaterialProcess (incluindo rawMaterial e
     * generatedContent)
     * da aplicação Python e atualiza o registro correspondente no PostgreSQL.
     * 
     * @param userId O ID do usuário.
     * @param taskId O ID da tarefa.
     * @return O DTO do MaterialProcess com os detalhes atualizados.
     */
    MaterialProcessResponseDTO getMaterialProcessDetails(String userId, String taskId);

    /**
     * Aciona a geração manual de conteúdo na aplicação Python usando o material
     * bruto fornecido.
     * Atualiza o registro MaterialProcess correspondente no PostgreSQL com o
     * conteúdo gerado.
     * 
     * @param userId        O ID do usuário.
     * @param taskId        O ID da tarefa.
     * @param manualRequest DTO com todos os parâmetros para a geração Gemini.
     * @return O DTO do MaterialProcess com o conteúdo gerado atualizado.
     */
    MaterialProcessResponseDTO triggerManualContentGeneration(
            String userId,
            String taskId,
            GenerateContentManualRequestDTO manualRequest);

    /**
     * Submete o post final (aprovado pelo usuário) para a API principal do Spring
     * Boot (via Python).
     * Após o sucesso, solicita ao Python que delete o registro temporário do
     * SQLite.
     * 
     * @param userId        O ID do usuário.
     * @param taskId        O ID da tarefa no MaterialProcess (para o log e possível
     *                      deleção).
     * @param submitRequest DTO com os dados completos do post final.
     */
    void submitFinalPost(String userId, String taskId, SubmitFinalPostRequestDTO submitRequest);

    /**
     * Lista todos os MaterialProcess registrados no PostgreSQL, opcionalmente
     * filtrando por status.
     * 
     * @param status Opcional: O status das tarefas a serem filtradas (por exemplo,
     *               "PENDING", "GENERATED").
     * @return Uma lista de DTOs de MaterialProcess.
     */
    List<MaterialProcessResponseDTO> listMaterialProcesses(Optional<String> status);

    /**
     * Gera uma imagem usando a API Python (Imagen 3.0) com base em um prompt.
     * 
     * @param imagePrompt O prompt de texto para a geração da imagem.
     * @param jwtToken    O token JWT para autenticação na API Python.
     * @return O DTO contendo a imagem gerada em Base64.
     */
    ImageGenerationResponseDTO generateImage(String imagePrompt, String jwtToken);
}
