package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.CustomFieldValue} entity.
 */
@ApiModel(description = "Additional information for a resource")
public class CustomFieldValueDTO implements Serializable {

    private Long id;

    /**
     * the key to identify the custom field
     */
    @NotNull
    @ApiModelProperty(value = "the key to identify the custom field", required = true)
    private String key;

    /**
     * a name which appears on the dialog
     */
    @NotNull
    @ApiModelProperty(value = "a name which appears on the dialog", required = true)
    private String name;

    /**
     * the value of the resource
     */
    @ApiModelProperty(value = "the value of the resource")
    private String value;

    private CustomFieldDTO customField;

    private ContactDTO contact;

    private ContactPersonDTO contactPerson;

    private ProjectDTO project;

    private CatalogProductDTO catalogProduct;

    private CatalogServiceDTO catalogService;

    private DocumentLetterDTO documentLetter;

    private DeliveryNoteDTO deliveryNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CustomFieldDTO getCustomField() {
        return customField;
    }

    public void setCustomField(CustomFieldDTO customField) {
        this.customField = customField;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public ContactPersonDTO getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPersonDTO contactPerson) {
        this.contactPerson = contactPerson;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public CatalogProductDTO getCatalogProduct() {
        return catalogProduct;
    }

    public void setCatalogProduct(CatalogProductDTO catalogProduct) {
        this.catalogProduct = catalogProduct;
    }

    public CatalogServiceDTO getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(CatalogServiceDTO catalogService) {
        this.catalogService = catalogService;
    }

    public DocumentLetterDTO getDocumentLetter() {
        return documentLetter;
    }

    public void setDocumentLetter(DocumentLetterDTO documentLetter) {
        this.documentLetter = documentLetter;
    }

    public DeliveryNoteDTO getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNoteDTO deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomFieldValueDTO)) {
            return false;
        }

        CustomFieldValueDTO customFieldValueDTO = (CustomFieldValueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customFieldValueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomFieldValueDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", value='" + getValue() + "'" +
            ", customField=" + getCustomField() +
            ", contact=" + getContact() +
            ", contactPerson=" + getContactPerson() +
            ", project=" + getProject() +
            ", catalogProduct=" + getCatalogProduct() +
            ", catalogService=" + getCatalogService() +
            ", documentLetter=" + getDocumentLetter() +
            ", deliveryNote=" + getDeliveryNote() +
            "}";
    }
}
