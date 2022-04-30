package com.projeto.starwars.controller;

import com.projeto.starwars.model.Jedi;
import com.projeto.starwars.service.JediService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JediControllerTest {

    @MockBean
    private JediService jediService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET/jedi/1 - SUCCESS")
    public void testGetJediByIdWithSuccess() throws Exception {

        Jedi mockJedi = new Jedi(1, "Han Solo", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);
    
        mockMvc.perform(get("/jedi/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Han Solo")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("GET /jedi/1 - Not Found")
    public void testGetJediByIdNotFound() throws Exception {

        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        mockMvc.perform(get("/jedi/{1}", 1))
                .andExpect(status().isNotFound());

    }

    //TODO: Teste do POST com sucesso
    //TODO: Teste do PUT com sucesso
    //TODO: Teste do PUT com uma versão igual da já existen - deve retornar um conflito
    //TODO: Teste do PUT com erro - Not Found
    //TODO: Teste do DELETE com sucesso
    //TODO: Teste do DELETE com erro - deletar um id já deletado
    //TODO: Teste do DELETE com erro - internal server error

    static String asJsonString(final Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
