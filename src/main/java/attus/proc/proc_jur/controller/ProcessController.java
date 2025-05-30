package attus.proc.proc_jur.controller;

import attus.proc.proc_jur.dto.ProcessDto;
import attus.proc.proc_jur.dto.ProcessFilter;
import attus.proc.proc_jur.dto.RequestProcessDto;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.service.ProcessService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/")
public class ProcessController {

    private final ProcessService processService;

    public ProcessController(ProcessService processService) {
        this.processService = processService;
    }

    @Tag(name = "Create new Process")
    @PostMapping("/process")
    public ResponseEntity<String> create(@Valid @RequestBody RequestProcessDto dto) {
        String processNumber = processService.create(dto);
        URI location = ServletUriComponentsBuilder
                .fromPath("/process/{number}")
                .buildAndExpand(processNumber)
                .toUri();
        return ResponseEntity
                .created(location)
                .body("""
                        {
                            "message": Process created,
                            "number": %s
                        }
                        """.formatted(processNumber)
                );
    }

    @Tag(name = "Update existing Process")
    @PutMapping("/process/{number}")
    public ResponseEntity<String> update(@PathVariable("number") String processNumber, @Valid @RequestBody RequestProcessDto dto) {
        processService.update(processNumber, dto);
        return ResponseEntity.ok("""
                {
                    "message": Process updated,
                    "number": %s
                }
                """.formatted(processNumber)
        );
    }

    @Tag(name = "Archive existing Process")
    @PatchMapping("/process")
    public ResponseEntity<String> archive(@RequestBody Set<String> processesNumbers) {
        processService.archive(processesNumbers);
        return ResponseEntity.ok("""
                {
                    "message": Process(es) archived,
                    "number": %s
                }
                """.formatted(String.join(", ", processesNumbers))
        );
    }

    @Tag(name = "Get Process(es) by Status, Opening Date or Party Legal Entity Id")
    @GetMapping("/process")
    public ResponseEntity<Page<ProcessDto>> get(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate openingDate,
            @RequestParam(required = false) String legalEntityId,
            Pageable pageable) {
        ProcessFilter filter = new ProcessFilter(status, openingDate, legalEntityId);
        Page<ProcessDto> page = processService.get(filter, pageable);
        return ResponseEntity.ok(page);
    }
}
