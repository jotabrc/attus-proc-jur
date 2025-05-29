package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ActionDto;
import attus.proc.proc_jur.model.Action;

public interface ActionService {

    public Action createAction(ActionDto dto);
}
