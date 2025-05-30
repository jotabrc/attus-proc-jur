package attus.proc.proc_jur.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class FormattingServiceImplTest {

    @InjectMocks
    private FormattingServiceImpl formattingService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void removeFormatting() {
        formattingService.REMOVE_PATTERN = "[.()/-]";
        String result = formattingService.removeFormatting("(00) 96548 -0586 ");
        assert !result.contains(formattingService.REMOVE_PATTERN);

        result = formattingService.removeFormatting("12.345.678/0001-00");
        assert !result.contains(formattingService.REMOVE_PATTERN);
    }
}