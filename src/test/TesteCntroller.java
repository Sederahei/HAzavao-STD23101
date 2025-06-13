package com.examen.prog.endpoint.rest.controller.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/test")
  public String test() {
    return "L'application fonctionne correctement !";
  }

  @GetMapping("/test-api-key")
  public String testApiKey() {
    String apiKey = "Nathanaêl key-API";
    return "Clé API: " + apiKey.substring(0, 20) + "..." + apiKey.substring(apiKey.length() - 10);
  }
}
