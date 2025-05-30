package attus.proc.proc_jur.controller;

import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.service.ProcessServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProcessControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProcessServiceImpl processService;

    @InjectMocks
    private ProcessController processController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(processController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void create() throws Exception {
        String json = """
                {
                    "status": "ACTIVE",
                    "description": "description process",
                    "parties": [
                        {
                            "fullName": "Joe Doe",
                            "legalEntityId": "12345678987",
                            "type": "AUTHOR",
                            "email": "email@email.com",
                            "phone": "14598742305"
                        }
                    ],
                    "actions": [
                        {
                            "type": "PETITION",
                            "description": "description action"
                        }
                    ]
                }
                """;

        String number = UUID.randomUUID().toString();
        when(processService.create(any())).thenReturn(number);
        mockMvc.perform(post("/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("message");
                    assert response.contains(number);
                })
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        String json = """
                {
                    "status": "ACTIVE",
                    "description": "description process",
                    "parties": [
                        {
                            "fullName": "Joe Doe",
                            "legalEntityId": "12345678987",
                            "type": "AUTHOR",
                            "email": "email@email.com",
                            "phone": "14598742305"
                        }
                    ],
                    "actions": [
                        {
                            "type": "PETITION",
                            "description": "description action"
                        }
                    ]
                }
                """;

        String number = UUID.randomUUID().toString();
        doNothing().when(processService).update(any(), any());
        mockMvc.perform(put("/process/{number}", number)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("message");
                    assert response.contains(number);
                })
                .andDo(print());
    }

    @Test
    void archive() throws Exception {
        String number1 = UUID.randomUUID().toString();
        String number2 = UUID.randomUUID().toString();
        String json = """
                [
                    "%s", "%s"
                ]
                """.formatted(number1, number2);
        doNothing().when(processService).update(any(), any());
        mockMvc.perform(patch("/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("message");
                    assert response.contains(number1);
                    assert response.contains(number2);
                })
                .andDo(print());
    }

    @Test
    void get() throws Exception {
        String number1 = UUID.randomUUID().toString();
        ProcessDto dto = new ProcessDto(
                LocalDateTime.now(),
                Status.ACTIVE,
                "Description",
                List.of(),
                List.of(),
                number1
        );
        Pageable pageable = PageRequest.of(0, 3);
        Page<ProcessDto> page = new PageImpl<>(List.of(dto), pageable, 1);
        when(processService.get(any(), eq(pageable))).thenReturn(page);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/process")
                        .param("status", "ACTIVE")
                        .param("openingDate", "2025-05-29")
                        .param("legalEntityId", "14568715683")
                        .param("page", "0")
                        .param("size", "3")
                )
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(dto.getNumber());
                })
                .andDo(print());
    }
}