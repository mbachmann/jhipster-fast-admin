package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.ReportingEntityType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Effort.
 */
@Entity
@Table(name = "effort")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Effort implements Serializable {

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
     * id of user ,
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * full name of user (optional)
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * type of assigned entity, one of: P - project, CU - cost unit, C - contact
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private ReportingEntityType entityType;

    /**
     * assigned entity id ,
     */
    @Column(name = "entity_id")
    private Integer entityId;

    /**
     * duration of effort
     */
    @Column(name = "duration")
    private Double duration;

    /**
     * date of effort
     */
    @Column(name = "date")
    private LocalDate date;

    /**
     * full activity name (optional)
     */
    @Column(name = "activity_name")
    private String activityName;

    /**
     * notes to this effort
     */
    @Column(name = "notes")
    private String notes;

    /**
     * if effort has been invoiced already
     */
    @Column(name = "is_invoiced")
    private Boolean isInvoiced;

    /**
     * update at
     */
    @Column(name = "updated")
    private ZonedDateTime updated;

    /**
     * hourly rate of this effort
     */
    @Column(name = "hourly_rate")
    private Double hourlyRate;

    /**
     * The related activity to this effort
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "activity" }, allowSetters = true)
    private Activity activity;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Effort id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public Effort remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Effort userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public Effort userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ReportingEntityType getEntityType() {
        return this.entityType;
    }

    public Effort entityType(ReportingEntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public void setEntityType(ReportingEntityType entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return this.entityId;
    }

    public Effort entityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Double getDuration() {
        return this.duration;
    }

    public Effort duration(Double duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Effort date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public Effort activityName(String activityName) {
        this.activityName = activityName;
        return this;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getNotes() {
        return this.notes;
    }

    public Effort notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsInvoiced() {
        return this.isInvoiced;
    }

    public Effort isInvoiced(Boolean isInvoiced) {
        this.isInvoiced = isInvoiced;
        return this;
    }

    public void setIsInvoiced(Boolean isInvoiced) {
        this.isInvoiced = isInvoiced;
    }

    public ZonedDateTime getUpdated() {
        return this.updated;
    }

    public Effort updated(ZonedDateTime updated) {
        this.updated = updated;
        return this;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Double getHourlyRate() {
        return this.hourlyRate;
    }

    public Effort hourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
        return this;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public Effort activity(Activity activity) {
        this.setActivity(activity);
        return this;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Effort)) {
            return false;
        }
        return id != null && id.equals(((Effort) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Effort{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", userId=" + getUserId() +
            ", userName='" + getUserName() + "'" +
            ", entityType='" + getEntityType() + "'" +
            ", entityId=" + getEntityId() +
            ", duration=" + getDuration() +
            ", date='" + getDate() + "'" +
            ", activityName='" + getActivityName() + "'" +
            ", notes='" + getNotes() + "'" +
            ", isInvoiced='" + getIsInvoiced() + "'" +
            ", updated='" + getUpdated() + "'" +
            ", hourlyRate=" + getHourlyRate() +
            "}";
    }
}
