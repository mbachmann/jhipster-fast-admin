package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project implements Serializable {

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
     * project's internal number
     */
    @Column(name = "number")
    private String number;

    /**
     * related contact's name (optional)
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * project's name
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * project's description
     */
    @Column(name = "description")
    private String description;

    /**
     * project's start date
     */
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * project's estimated maximum effort (in hours)
     */
    @Column(name = "hours_estimated")
    private Double hoursEstimated;

    /**
     * project's hourly rate
     */
    @Column(name = "hourly_rate")
    private Double hourlyRate;

    /**
     * status to be set, on of:  ''O'' - open, ''C'' - closed, ''B'' - billed, ''P'' - pending'
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;

    /**
     * custom edit fields
     */
    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "customField", "contact", "contactPerson", "project", "catalogProduct", "catalogService", "documentLetter", "deliveryNote",
        },
        allowSetters = true
    )
    private Set<CustomFieldValue> customFields = new HashSet<>();

    /**
     * The related contact to this project
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "relations", "groups" }, allowSetters = true)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public Project remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getNumber() {
        return this.number;
    }

    public Project number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactName() {
        return this.contactName;
    }

    public Project contactName(String contactName) {
        this.contactName = contactName;
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getName() {
        return this.name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Project startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Double getHoursEstimated() {
        return this.hoursEstimated;
    }

    public Project hoursEstimated(Double hoursEstimated) {
        this.hoursEstimated = hoursEstimated;
        return this;
    }

    public void setHoursEstimated(Double hoursEstimated) {
        this.hoursEstimated = hoursEstimated;
    }

    public Double getHourlyRate() {
        return this.hourlyRate;
    }

    public Project hourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
        return this;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public ProjectStatus getStatus() {
        return this.status;
    }

    public Project status(ProjectStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Set<CustomFieldValue> getCustomFields() {
        return this.customFields;
    }

    public Project customFields(Set<CustomFieldValue> customFieldValues) {
        this.setCustomFields(customFieldValues);
        return this;
    }

    public Project addCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.add(customFieldValue);
        customFieldValue.setProject(this);
        return this;
    }

    public Project removeCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.remove(customFieldValue);
        customFieldValue.setProject(null);
        return this;
    }

    public void setCustomFields(Set<CustomFieldValue> customFieldValues) {
        if (this.customFields != null) {
            this.customFields.forEach(i -> i.setProject(null));
        }
        if (customFieldValues != null) {
            customFieldValues.forEach(i -> i.setProject(this));
        }
        this.customFields = customFieldValues;
    }

    public Contact getContact() {
        return this.contact;
    }

    public Project contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", hoursEstimated=" + getHoursEstimated() +
            ", hourlyRate=" + getHourlyRate() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
