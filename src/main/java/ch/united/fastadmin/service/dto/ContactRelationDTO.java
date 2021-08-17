package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.ContactRelationType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ContactRelation} entity.
 */
public class ContactRelationDTO implements Serializable {

    private Long id;

    /**
     * type of contact's relation; possible values: type of contact's relation; possible values: CL - Customer, CR - Creditor, TE - TEAM, OF - Authorities (Behörden), ME - Medical, OT - Others
     */
    @ApiModelProperty(
        value = "type of contact's relation; possible values: type of contact's relation; possible values: CL - Customer, CR - Creditor, TE - TEAM, OF - Authorities (Behörden), ME - Medical, OT - Others"
    )
    private ContactRelationType contactRelationType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactRelationType getContactRelationType() {
        return contactRelationType;
    }

    public void setContactRelationType(ContactRelationType contactRelationType) {
        this.contactRelationType = contactRelationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactRelationDTO)) {
            return false;
        }

        ContactRelationDTO contactRelationDTO = (ContactRelationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactRelationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactRelationDTO{" +
            "id=" + getId() +
            ", contactRelationType='" + getContactRelationType() + "'" +
            "}";
    }
}
