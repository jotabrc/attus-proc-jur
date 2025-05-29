package attus.proc.proc_jur.service;

import attus.proc.proc_jur.dto.ContactDto;
import attus.proc.proc_jur.dto.PartyDto;
import attus.proc.proc_jur.enums.PartyType;
import attus.proc.proc_jur.model.Contact;
import attus.proc.proc_jur.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ContactServiceImplTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrCreateContact() {
        ContactDto contactDto = new ContactDto(
                "email@email.com",
                "(11)91234-5678"
        );
        PartyDto dto = new PartyDto(
                "John Doe",
                "123.456.789.11",
                PartyType.ATTORNEY,
                contactDto
        );
        when(contactRepository.findByEmailOrPhone(any(), any())).thenReturn(Optional.of(new Contact()));
        Contact contact = contactService.getOrCreateContact(dto);
        assert contact.getEmail() == null;
        assert contact.getPhone() == null;

        when(contactRepository.findByEmailOrPhone(any(), any())).thenReturn(Optional.empty());
        contact = contactService.getOrCreateContact(dto);
        assert contact.getEmail().equals(contactDto.getEmail());
        assert contact.getPhone().equals(contactDto.getPhone());
    }
}