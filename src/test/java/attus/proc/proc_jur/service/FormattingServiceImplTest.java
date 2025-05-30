package attus.proc.proc_jur.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

class FormattingServiceImplTest {

    @InjectMocks
    private FormattingServiceImpl formattingService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void removeFormatting() throws NoSuchFieldException, IllegalAccessException {

        Field field = FormattingServiceImpl.class.getDeclaredField("REMOVE_PATTERN");
        field.setAccessible(true);
        field.set(formattingService, "[.()/-]");
        String result = formattingService.removeFormatting("(00) 96548 -0586 ");
        assert !result.contains((String) field.get(formattingService));

        result = formattingService.removeFormatting("12.345.678/0001-00");
        assert !result.contains((String) field.get(formattingService));
    }
}