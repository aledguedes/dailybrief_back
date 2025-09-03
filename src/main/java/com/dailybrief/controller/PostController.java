package com.dailybrief.controller;

import com.dailybrief.dto.HomepagePostResponseDTO;
import com.dailybrief.dto.LocalizedPostResponseDTO;
import com.dailybrief.dto.PostRequestDTO;
import com.dailybrief.dto.PostResponseDTO;
import com.dailybrief.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Cria um novo post.
     *
     * @param postRequest Dados do post a ser criado.
     * @return Detalhes do post criado.
     */
    @Operation(summary = "Criar um novo post", description = "Este serviço cria um novo post no sistema com base nos dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao criar o post.")
    })
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @Parameter(description = "Dados do post a ser criado.") @RequestBody PostRequestDTO postRequest) {

        PostResponseDTO response = postService.createPost(postRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna uma lista de todos os posts com paginação.
     *
     * @param pageable Informações de paginação (página, tamanho, etc).
     * @return Lista paginada de posts.
     */
    @Operation(summary = "Obter todos os posts", description = "Este serviço retorna uma lista de todos os posts, com suporte a paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts recuperada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao recuperar os posts.")
    })
    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getAllPosts(Pageable pageable) {
        Page<PostResponseDTO> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    /**
     * Retorna uma lista de posts localizados com base na páginação.
     *
     * @param pageable Informações de paginação.
     * @return Lista paginada de posts localizados.
     */
    @Operation(summary = "Obter todos os posts localizados", description = "Este serviço retorna uma lista de posts localizados, com suporte a paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts localizados recuperada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao recuperar os posts localizados.")
    })
    @GetMapping("/localized")
    public ResponseEntity<Page<PostResponseDTO>> getAllPostsLocalized(Pageable pageable) {
        Page<PostResponseDTO> posts = postService.getAllPostsLocalized(pageable);
        return ResponseEntity.ok(posts);
    }

    /**
     * Retorna um post específico pelo ID.
     *
     * @param id ID do post a ser recuperado.
     * @return Detalhes do post encontrado.
     */
    @Operation(summary = "Obter post por ID", description = "Este serviço retorna os detalhes de um post específico pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Post não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(
            @Parameter(description = "ID do post a ser recuperado.") @PathVariable Long id) {

        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    /**
     * Atualiza um post existente.
     *
     * @param id          ID do post a ser atualizado.
     * @param postRequest Novos dados para o post.
     * @return Detalhes do post atualizado.
     */
    @Operation(summary = "Atualizar um post", description = "Este serviço permite atualizar um post existente com novos dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Post não encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @Parameter(description = "ID do post a ser atualizado.") @PathVariable Long id,
            @Parameter(description = "Novos dados para o post.") @RequestBody PostRequestDTO postRequest) {

        PostResponseDTO response = postService.updatePost(id, postRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Aprova um post específico.
     *
     * @param id ID do post a ser aprovado.
     * @return Detalhes do post aprovado.
     */
    @Operation(summary = "Aprovar post", description = "Este serviço aprova um post específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post aprovado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Post não encontrado.")
    })
    @PatchMapping("/{id}/approve")
    public ResponseEntity<PostResponseDTO> approvePost(
            @Parameter(description = "ID do post a ser aprovado.") @PathVariable Long id) {

        PostResponseDTO response = postService.approvePost(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Rejeita um post específico.
     *
     * @param id ID do post a ser rejeitado.
     * @return Detalhes do post rejeitado.
     */
    @Operation(summary = "Rejeitar post", description = "Este serviço rejeita um post específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post rejeitado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Post não encontrado.")
    })
    @PatchMapping("/{id}/reject")
    public ResponseEntity<PostResponseDTO> rejectPost(
            @Parameter(description = "ID do post a ser rejeitado.") @PathVariable Long id) {

        PostResponseDTO response = postService.rejectPost(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Deleta um post específico.
     *
     * @param id ID do post a ser deletado.
     */
    @Operation(summary = "Deletar post", description = "Este serviço permite deletar um post específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Post não encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtém os posts da homepage com base no limite e idioma fornecido.
     *
     * @param limit Limite de posts a serem retornados.
     * @param lang  Idioma dos posts.
     * @return Posts da homepage.
     */
    @Operation(summary = "Obter posts da homepage", description = "Este serviço retorna os posts mais recentes da homepage, com base no limite e idioma fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts da homepage recuperados com sucesso."),
            @ApiResponse(responseCode = "400", description = "Erro ao recuperar os posts da homepage.")
    })
    @GetMapping("/homepage")
    public ResponseEntity<HomepagePostResponseDTO> getHomepagePosts(
            @Parameter(description = "Limite de posts da homepage.") @RequestParam(defaultValue = "5") int limit,
            @Parameter(description = "Idioma dos posts.") @RequestParam(required = false) String lang) {

        HomepagePostResponseDTO response = postService.getHomepagePosts(limit, lang);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtém um post público específico pelo ID.
     *
     * @param id   ID do post público.
     * @param lang Idioma do post.
     * @return Detalhes do post público.
     */
    @Operation(summary = "Obter post público por ID", description = "Este serviço retorna os detalhes de um post público específico pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post público encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Post não encontrado.")
    })
    @GetMapping("/public/{id}")
    public ResponseEntity<LocalizedPostResponseDTO> getPublicPostById(
            @Parameter(description = "ID do post público a ser recuperado.") @PathVariable Long id,
            @Parameter(description = "Idioma do post.") @RequestParam(required = false) String lang) {

        LocalizedPostResponseDTO response = postService.getPublicPostById(id, lang);
        return ResponseEntity.ok(response);
    }
}
