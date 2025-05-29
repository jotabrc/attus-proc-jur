package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ActionDto;
import attus.proc.proc_jur.model.Action;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ActionServiceImpl implements ActionService {

    @Override
    public Action createAction(ActionDto dto) {
        return Action
                .builder()
                .type(dto.getType())
                .registrationDate(ZonedDateTime.now().withZoneSameLocal(ZoneId.of("America/Sao_Paulo")))
                .description(dto.getDescription())
                .build();
    }
}
