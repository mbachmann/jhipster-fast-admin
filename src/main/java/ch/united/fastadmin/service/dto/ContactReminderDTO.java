package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.IntervalType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ContactReminder} entity.
 */
public class ContactReminderDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * optional: name of a contact
     */
    @ApiModelProperty(value = "optional: name of a contact")
    private String contactName;

    /**
     * date and time of event that should be reminded (format: IOS8601) ,
     */
    @ApiModelProperty(value = "date and time of event that should be reminded (format: IOS8601) ,")
    private ZonedDateTime dateTime;

    /**
     * title of reminder
     */
    @ApiModelProperty(value = "title of reminder")
    private String title;

    /**
     * description of reminder
     */
    @ApiModelProperty(value = "description of reminder")
    private String description;

    /**
     * value of interval in which reminder will be activated , /
     */
    @ApiModelProperty(value = "value of interval in which reminder will be activated , /")
    private Integer intervalValue;

    /**
     * one of: ['hour','day','month']
     */
    @ApiModelProperty(value = "one of: ['hour','day','month']")
    private IntervalType intervalType;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

    private ContactDTO contact;

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

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIntervalValue() {
        return intervalValue;
    }

    public void setIntervalValue(Integer intervalValue) {
        this.intervalValue = intervalValue;
    }

    public IntervalType getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(IntervalType intervalType) {
        this.intervalType = intervalType;
    }

    public Boolean getInactiv() {
        return inactiv;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
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
        if (!(o instanceof ContactReminderDTO)) {
            return false;
        }

        ContactReminderDTO contactReminderDTO = (ContactReminderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactReminderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactReminderDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", contactName='" + getContactName() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", intervalValue=" + getIntervalValue() +
            ", intervalType='" + getIntervalType() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
