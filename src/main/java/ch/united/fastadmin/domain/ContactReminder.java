package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.IntervalType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A ContactReminder.
 */
@Entity
@Table(name = "contact_reminder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contactreminder")
public class ContactReminder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * id of a remote system
     */

    @Column(name = "remote_id", unique = true)
    private Integer remoteId;

    /**
     * id of a contact
     */
    @Column(name = "contact_id")
    private Integer contactId;

    /**
     * optional: name of a contact
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * date and time of event that should be reminded (format: IOS8601) ,
     */
    @Column(name = "date_time")
    private ZonedDateTime dateTime;

    /**
     * title of reminder
     */
    @Column(name = "title")
    private String title;

    /**
     * description of reminder
     */
    @Column(name = "description")
    private String description;

    /**
     * value of interval in which reminder will be activated , /
     */
    @Column(name = "interval_value")
    private Integer intervalValue;

    /**
     * one of: ['hour','day','month']
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "interval_type")
    private IntervalType intervalType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactReminder id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public ContactReminder remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public Integer getContactId() {
        return this.contactId;
    }

    public ContactReminder contactId(Integer contactId) {
        this.contactId = contactId;
        return this;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return this.contactName;
    }

    public ContactReminder contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public ZonedDateTime getDateTime() {
        return this.dateTime;
    }

    public ContactReminder dateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return this.title;
    }

    public ContactReminder title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public ContactReminder description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIntervalValue() {
        return this.intervalValue;
    }

    public ContactReminder intervalValue(Integer intervalValue) {
        this.intervalValue = intervalValue;
        return this;
    }

    public void setIntervalValue(Integer intervalValue) {
        this.intervalValue = intervalValue;
    }

    public IntervalType getIntervalType() {
        return this.intervalType;
    }

    public ContactReminder intervalType(IntervalType intervalType) {
        this.intervalType = intervalType;
        return this;
    }

    public void setIntervalType(IntervalType intervalType) {
        this.intervalType = intervalType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactReminder)) {
            return false;
        }
        return id != null && id.equals(((ContactReminder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactReminder{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", contactId=" + getContactId() +
            ", contactName='" + getContactName() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", intervalValue=" + getIntervalValue() +
            ", intervalType='" + getIntervalType() + "'" +
            "}";
    }
}
