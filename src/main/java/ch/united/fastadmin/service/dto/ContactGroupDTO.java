package ch.united.fastadmin.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ContactGroup} entity.
 */
public class ContactGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private ContactDTO contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactGroupDTO)) {
            return false;
        }

        ContactGroupDTO contactGroupDTO = (ContactGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
