package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.InvoiceWorkflowStatus;
import ch.united.fastadmin.domain.enumeration.PostSpeed;
import ch.united.fastadmin.domain.enumeration.WorkflowAction;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Defining the workflow of the invoice with  R - payment reminder, R1 - 1st reminder, R2 - 2nd reminder, R3 - 3rd reminder
 */
@Entity
@Table(name = "document_invoice_workflow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DocumentInvoiceWorkflow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * whether workflow is active
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * status of a document that the workflow is for; possible values: R - payment reminder, R1 - 1st reminder, R2 - 2nd reminder, R3 - 3rd reminder
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceWorkflowStatus status;

    /**
     * how many days after due date action should be taken ,
     */
    @Column(name = "overdue_days")
    private Integer overdueDays;

    /**
     * action that should be taken; possible values: M - remind me, CE - remind contact by email, CP - remind contact by post
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private WorkflowAction action;

    /**
     * email address for reminding contact (valid only when action is CE)
     */
    @Column(name = "contact_email")
    private String contactEmail;

    /**
     * post speed for reminding contact (valid only when action is CP); possible values: P - priority, E - economy
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "speed")
    private PostSpeed speed;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentInvoiceWorkflow id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public DocumentInvoiceWorkflow active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public InvoiceWorkflowStatus getStatus() {
        return this.status;
    }

    public DocumentInvoiceWorkflow status(InvoiceWorkflowStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(InvoiceWorkflowStatus status) {
        this.status = status;
    }

    public Integer getOverdueDays() {
        return this.overdueDays;
    }

    public DocumentInvoiceWorkflow overdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
        return this;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public WorkflowAction getAction() {
        return this.action;
    }

    public DocumentInvoiceWorkflow action(WorkflowAction action) {
        this.action = action;
        return this;
    }

    public void setAction(WorkflowAction action) {
        this.action = action;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public DocumentInvoiceWorkflow contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public PostSpeed getSpeed() {
        return this.speed;
    }

    public DocumentInvoiceWorkflow speed(PostSpeed speed) {
        this.speed = speed;
        return this;
    }

    public void setSpeed(PostSpeed speed) {
        this.speed = speed;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentInvoiceWorkflow)) {
            return false;
        }
        return id != null && id.equals(((DocumentInvoiceWorkflow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentInvoiceWorkflow{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", status='" + getStatus() + "'" +
            ", overdueDays=" + getOverdueDays() +
            ", action='" + getAction() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", speed='" + getSpeed() + "'" +
            "}";
    }
}
