package ch.united.fastadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Working Hours of an ApplicationUser
 */
@Entity
@Table(name = "working_hour")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WorkingHour implements Serializable {

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
     * (optional): full name of user
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * date of working hours time span
     */
    @Column(name = "date")
    private LocalDate date;

    /**
     * start of working hours time span (in HH:MM format)
     */
    @Column(name = "time_start")
    private ZonedDateTime timeStart;

    /**
     * end of working hours time span (in HH:MM format)
     */
    @Column(name = "time_end")
    private ZonedDateTime timeEnd;

    /**
     * date when working hours entity was created
     */
    @Column(name = "created")
    private ZonedDateTime created;

    /**
     * The working hour booked from user
     */
    @ManyToOne
    private ApplicationUser applicationUser;

    /**
     * The working hour has been booked to this effort
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "activity" }, allowSetters = true)
    private Effort effort;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkingHour id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public WorkingHour remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getUserName() {
        return this.userName;
    }

    public WorkingHour userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public WorkingHour date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ZonedDateTime getTimeStart() {
        return this.timeStart;
    }

    public WorkingHour timeStart(ZonedDateTime timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public void setTimeStart(ZonedDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public ZonedDateTime getTimeEnd() {
        return this.timeEnd;
    }

    public WorkingHour timeEnd(ZonedDateTime timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }

    public void setTimeEnd(ZonedDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public WorkingHour created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public WorkingHour applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Effort getEffort() {
        return this.effort;
    }

    public WorkingHour effort(Effort effort) {
        this.setEffort(effort);
        return this;
    }

    public void setEffort(Effort effort) {
        this.effort = effort;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkingHour)) {
            return false;
        }
        return id != null && id.equals(((WorkingHour) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingHour{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", userName='" + getUserName() + "'" +
            ", date='" + getDate() + "'" +
            ", timeStart='" + getTimeStart() + "'" +
            ", timeEnd='" + getTimeEnd() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
