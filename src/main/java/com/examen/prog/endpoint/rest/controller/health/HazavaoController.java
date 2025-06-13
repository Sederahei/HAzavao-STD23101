package com.examen.prog.endpoint.rest.controller.health;


import com.examen.prog.service.HazavaoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HazavaoController {

    private final HazavaoService hazavaoService;

    @GetMapping("/hazavao")
    public HazavaoResponse getDefinition(@RequestParam("teny") String teny) {
        String definition = hazavaoService.getDefinition(teny);
        return new HazavaoResponse(teny, definition);
    }

    public static class HazavaoResponse {
        private String teny;
        private String definition;

        public HazavaoResponse(String teny, String definition) {
            this.teny = teny;
            this.definition = definition;
        }

        public String getTeny() {
            return teny;
        }

        public void setTeny(String teny) {
            this.teny = teny;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }
    }
}