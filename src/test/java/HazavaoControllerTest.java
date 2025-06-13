import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.examen.prog.endpoint.rest.controller.health.HazavaoController;
import com.examen.prog.service.HazavaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HazavaoController.class)
public class HazavaoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private HazavaoService hazavaoService;

  @Test
  public void testGetDefinition() throws Exception {
    // Given
    String teny = "fitiavana";
    String expectedDefinition =
        "Fitiavana dia ny fahatsapana lalina sy mahery vaika eo amin'ny olona iray na zavatra"
            + " iray.";

    when(hazavaoService.getDefinition(anyString())).thenReturn(expectedDefinition);

    // When & Then
    mockMvc
        .perform(get("/hazavao").param("teny", teny))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.teny").value(teny))
        .andExpect(jsonPath("$.definition").value(expectedDefinition));
  }

  @Test
  public void testGetDefinitionWithEmptyParam() throws Exception {
    mockMvc.perform(get("/hazavao").param("teny", "")).andExpect(status().isOk());
  }
}
