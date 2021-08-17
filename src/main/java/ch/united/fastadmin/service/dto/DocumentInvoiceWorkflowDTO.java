package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.InvoiceWorkflowStatus;
import ch.united.fastadmin.domain.enumeration.PostSpeed;
import ch.united.fastadmin.domain.enumeration.WorkflowAction;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.DocumentInvoiceWorkflow} entity.
 */
@ApiModel(
    description = "Defining the workflow of the invoice with  R - payment reminder, R1 - 1st reminder, R2 - 2nd reminder, R3 - 3rd reminder"
)
public class DocumentInvoiceWorkflowDTO implements Serializable {

    private Long id;

    /**
     * whether workflow is active
     */
    @ApiModelProperty(value = "whether workflow is active")
    private Boolean active;

    /**
     * status of a document that the workflow is for; possible values: R - payment reminder, R1 - 1st reminder, R2 - 2nd reminder, R3 - 3rd reminder
     */
    @ApiModelProperty(
        value = "status of a document that the workflow is for; possible values: R - payment reminder, R1 - 1st reminder, R2 - 2nd reminder, R3 - 3rd reminder"
    )
    private InvoiceWorkflowStatus status;

    /**
     * how many days after due date action should be taken ,
     */
    @ApiModelProperty(value = "how many days after due date action should be taken ,")
    private Integer overdueDays;

    /**
     * action that should be taken; possible values: M - remind me, CE - remind contact by email, CP - remind contact by post
     */
    @ApiModelProperty(
        value = "action that should be taken; possible values: M - remind me, CE - remind contact by email, CP - remind contact by post"
    )
    private WorkflowAction action;

    /**
     * email address for reminding contact (valid only when action is CE)
     */
    @ApiModelProperty(value = "email address for reminding contact (valid only when action is CE)")
    private String contactEmail;

    /**
     * post speed for reminding contact (valid only when action is CP); possible values: P - priority, E - economy
     */
    @ApiModelProperty(value = "post speed for reminding contact (valid only when action is CP); possible values: P - priority, E - economy")
    private PostSpeed speed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public InvoiceWorkflowStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceWorkflowStatus status) {
        this.status = status;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public WorkflowAction getAction() {
        return action;
    }

    public void setAction(WorkflowAction action) {
        this.action = action;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public PostSpeed getSpeed() {
        return speed;
    }

    public void setSpeed(PostSpeed speed) {
        this.speed = speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentInvoiceWorkflowDTO)) {
            return false;
        }

        DocumentInvoiceWorkflowDTO documentInvoiceWorkflowDTO = (DocumentInvoiceWorkflowDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentInvoiceWorkflowDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentInvoiceWorkflowDTO{" +
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
