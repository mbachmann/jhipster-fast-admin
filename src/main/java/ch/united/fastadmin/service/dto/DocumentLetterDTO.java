package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.LetterStatus;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.DocumentLetter} entity.
 */
public class DocumentLetterDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * name of a contact
     */
    @ApiModelProperty(value = "name of a contact")
    private String contactName;

    /**
     * date of the letter
     */
    @ApiModelProperty(value = "date of the letter")
    private LocalDate date;

    /**
     * title of the document
     */
    @ApiModelProperty(value = "title of the document")
    private String title;

    /**
     * content of the document
     */
    @ApiModelProperty(value = "content of the document")
    private String content;

    /**
     * language; possible values: de, en, es, fr, it ,
     */
    @ApiModelProperty(value = "language; possible values: de, en, es, fr, it ,")
    private DocumentLanguage language;

    /**
     * how many pages the document has
     */
    @ApiModelProperty(value = "how many pages the document has")
    private Integer pageAmount;

    /**
     * optional notes
     */
    @ApiModelProperty(value = "optional notes")
    private String notes;

    /**
     * status of letter, possible values: DR - draft, S - sent, D - deleted (but still visible)
     */
    @ApiModelProperty(value = "status of letter, possible values: DR - draft, S - sent, D - deleted (but still visible)")
    private LetterStatus status;

    private ZonedDateTime created;

    private ContactDTO contact;

    private ContactAddressDTO contactAddress;

    private ContactPersonDTO contactPerson;

    private ContactAddressDTO contactPrePageAddress;

    private LayoutDTO layout;

    private SignatureDTO layout;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DocumentLanguage getLanguage() {
        return language;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public Integer getPageAmount() {
        return pageAmount;
    }

    public void setPageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LetterStatus getStatus() {
        return status;
    }

    public void setStatus(LetterStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public ContactAddressDTO getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(ContactAddressDTO contactAddress) {
        this.contactAddress = contactAddress;
    }

    public ContactPersonDTO getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPersonDTO contactPerson) {
        this.contactPerson = contactPerson;
    }

    public ContactAddressDTO getContactPrePageAddress() {
        return contactPrePageAddress;
    }

    public void setContactPrePageAddress(ContactAddressDTO contactPrePageAddress) {
        this.contactPrePageAddress = contactPrePageAddress;
    }

    public LayoutDTO getLayout() {
        return layout;
    }

    public void setLayout(LayoutDTO layout) {
        this.layout = layout;
    }

    public SignatureDTO getLayout() {
        return layout;
    }

    public void setLayout(SignatureDTO layout) {
        this.layout = layout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentLetterDTO)) {
            return false;
        }

        DocumentLetterDTO documentLetterDTO = (DocumentLetterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentLetterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentLetterDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", contactName='" + getContactName() + "'" +
            ", date='" + getDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pageAmount=" + getPageAmount() +
            ", notes='" + getNotes() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", contact=" + getContact() +
            ", contactAddress=" + getContactAddress() +
            ", contactPerson=" + getContactPerson() +
            ", contactPrePageAddress=" + getContactPrePageAddress() +
            ", layout=" + getLayout() +
            ", layout=" + getLayout() +
            "}";
    }
}
