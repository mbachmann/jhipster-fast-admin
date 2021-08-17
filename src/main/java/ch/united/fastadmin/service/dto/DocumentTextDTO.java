package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DocumentInvoiceTextStatus;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.DocumentTextDocumentType;
import ch.united.fastadmin.domain.enumeration.DocumentTextType;
import ch.united.fastadmin.domain.enumeration.DocumentTextUsage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.DocumentText} entity.
 */
@ApiModel(description = "title, introduction and condition text")
public class DocumentTextDTO implements Serializable {

    private Long id;

    /**
     * default text
     */
    @ApiModelProperty(value = "default text")
    private Boolean defaultText;

    /**
     * Document conditions text (z.B. Zahlungsbedingungen)
     */
    @ApiModelProperty(value = "Document conditions text (z.B. Zahlungsbedingungen)")
    private String text;

    /**
     * language; possible values: de, en, es, fr, it ,
     */
    @ApiModelProperty(value = "language; possible values: de, en, es, fr, it ,")
    private DocumentLanguage language;

    /**
     * At which document position shall the text be used; possible values; T - Title, I - Introduction, D - Condition
     */
    @ApiModelProperty(
        value = "At which document position shall the text be used; possible values; T - Title, I - Introduction, D - Condition"
    )
    private DocumentTextUsage usage;

    /**
     * for invoice only
     */
    @ApiModelProperty(value = "for invoice only")
    private DocumentInvoiceTextStatus status;

    /**
     * which document type is the condition for; possible values: D - Document, E- Email
     */
    @ApiModelProperty(value = "which document type is the condition for; possible values: D - Document, E- Email")
    private DocumentTextType type;

    /**
     * which Receivable document is the information for; possible values: O - Offer, C - Order Confirmation, D - Delivery Note, I - Invoice
     */
    @ApiModelProperty(
        value = "which Receivable document is the information for; possible values: O - Offer, C - Order Confirmation, D - Delivery Note, I - Invoice"
    )
    private DocumentTextDocumentType documentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(Boolean defaultText) {
        this.defaultText = defaultText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DocumentLanguage getLanguage() {
        return language;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public DocumentTextUsage getUsage() {
        return usage;
    }

    public void setUsage(DocumentTextUsage usage) {
        this.usage = usage;
    }

    public DocumentInvoiceTextStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentInvoiceTextStatus status) {
        this.status = status;
    }

    public DocumentTextType getType() {
        return type;
    }

    public void setType(DocumentTextType type) {
        this.type = type;
    }

    public DocumentTextDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTextDocumentType documentType) {
        this.documentType = documentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentTextDTO)) {
            return false;
        }

        DocumentTextDTO documentTextDTO = (DocumentTextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentTextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentTextDTO{" +
            "id=" + getId() +
            ", defaultText='" + getDefaultText() + "'" +
            ", text='" + getText() + "'" +
            ", language='" + getLanguage() + "'" +
            ", usage='" + getUsage() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            "}";
    }
}
