package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DocumentInvoiceTextStatus;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.DocumentTextDocumentType;
import ch.united.fastadmin.domain.enumeration.DocumentTextType;
import ch.united.fastadmin.domain.enumeration.DocumentTextUsage;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * title, introduction and condition text
 */
@Entity
@Table(name = "document_text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DocumentText implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * default text
     */
    @Column(name = "default_text")
    private Boolean defaultText;

    /**
     * Document conditions text (z.B. Zahlungsbedingungen)
     */
    @Column(name = "text")
    private String text;

    /**
     * language; possible values: de, en, es, fr, it ,
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private DocumentLanguage language;

    /**
     * At which document position shall the text be used; possible values; T - Title, I - Introduction, D - Condition
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "fa_usage")
    private DocumentTextUsage usage;

    /**
     * for invoice only
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocumentInvoiceTextStatus status;

    /**
     * which document type is the condition for; possible values: D - Document, E- Email
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DocumentTextType type;

    /**
     * which Receivable document is the information for; possible values: O - Offer, C - Order Confirmation, D - Delivery Note, I - Invoice
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentTextDocumentType documentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentText id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getDefaultText() {
        return this.defaultText;
    }

    public DocumentText defaultText(Boolean defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    public void setDefaultText(Boolean defaultText) {
        this.defaultText = defaultText;
    }

    public String getText() {
        return this.text;
    }

    public DocumentText text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DocumentLanguage getLanguage() {
        return this.language;
    }

    public DocumentText language(DocumentLanguage language) {
        this.language = language;
        return this;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public DocumentTextUsage getUsage() {
        return this.usage;
    }

    public DocumentText usage(DocumentTextUsage usage) {
        this.usage = usage;
        return this;
    }

    public void setUsage(DocumentTextUsage usage) {
        this.usage = usage;
    }

    public DocumentInvoiceTextStatus getStatus() {
        return this.status;
    }

    public DocumentText status(DocumentInvoiceTextStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(DocumentInvoiceTextStatus status) {
        this.status = status;
    }

    public DocumentTextType getType() {
        return this.type;
    }

    public DocumentText type(DocumentTextType type) {
        this.type = type;
        return this;
    }

    public void setType(DocumentTextType type) {
        this.type = type;
    }

    public DocumentTextDocumentType getDocumentType() {
        return this.documentType;
    }

    public DocumentText documentType(DocumentTextDocumentType documentType) {
        this.documentType = documentType;
        return this;
    }

    public void setDocumentType(DocumentTextDocumentType documentType) {
        this.documentType = documentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentText)) {
            return false;
        }
        return id != null && id.equals(((DocumentText) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentText{" +
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
