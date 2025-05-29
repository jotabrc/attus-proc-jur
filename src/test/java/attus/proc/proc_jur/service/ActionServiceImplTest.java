package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ActionDto;
import attus.proc.proc_jur.enums.ActionType;
import attus.proc.proc_jur.model.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class ActionServiceImplTest {

    @InjectMocks
    private ActionServiceImpl actionService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAction() {
        ActionDto dto = new ActionDto(
                ActionType.HEARING,
                "Hearing short description"
        );
        Action action = actionService.createAction(dto);
        assert action.getType().equals(dto.getType());
        assert action.getRegistrationDate() != null;
        assert action.getDescription().equals(dto.getDescription());
    }
}