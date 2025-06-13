package com.examen.prog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HazavaoService {

    // Clé API OpenAI
    private final String openaiApiKey = " Nathaêl key-API";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public HazavaoService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getDefinition(String teny) {
        log.info("Recherche de définition pour le mot: {}", teny);

        try {
            // Configuration des headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + openaiApiKey);

            log.debug("Headers configurés: {}", headers);

            // Construction du corps de la requête
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("max_tokens", 200);
            requestBody.put("temperature", 0.7);

            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", "Manomeza ny hevitry ny teny \"" + teny + "\" amin'ny teny malagasy. Omeo famaritana fohy sy mazava.");

            requestBody.put("messages", List.of(message));

            log.debug("Corps de la requête: {}", requestBody);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            log.info("Envoi de la requête vers OpenAI API...");

            ResponseEntity<String> response = restTemplate.exchange(
                    OPENAI_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            log.info("Réponse reçue avec status: {}", response.getStatusCode());
            log.debug("Corps de la réponse: {}", response.getBody());

            return extractDefinitionFromResponse(response.getBody());

        } catch (HttpClientErrorException e) {
            log.error("Erreur client HTTP (4xx): Status={}, Body={}", e.getStatusCode(), e.getResponseBodyAsString());
            return "Erreur d'authentification ou de requête. Vérifiez la clé API.";
        } catch (HttpServerErrorException e) {
            log.error("Erreur serveur HTTP (5xx): Status={}, Body={}", e.getStatusCode(), e.getResponseBodyAsString());
            return "Erreur du serveur OpenAI. Réessayez plus tard.";
        } catch (Exception e) {
            log.error("Erreur générale lors de l'appel à l'API OpenAI: {}", e.getMessage(), e);
            return "Tsy afaka nahazo ny famaritana. Misy olana teo amin'ny serivisy: " + e.getMessage();
        }
    }

    private String extractDefinitionFromResponse(String responseBody) {
        try {
            log.debug("Extraction de la définition depuis: {}", responseBody);

            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode choices = jsonNode.get("choices");

            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode firstChoice = choices.get(0);
                JsonNode message = firstChoice.get("message");

                if (message != null) {
                    JsonNode content = message.get("content");
                    if (content != null) {
                        String definition = content.asText().trim();
                        log.info("Définition extraite avec succès: {}", definition);
                        return definition;
                    }
                }
            }

            log.warn("Aucune définition trouvée dans la réponse");
            return "Tsy afaka nahazo ny famaritana.";

        } catch (Exception e) {
            log.error("Erreur lors de l'extraction de la définition: {}", e.getMessage(), e);
            return "Tsy afaka nahazo ny famaritana.";
        }
    }
}