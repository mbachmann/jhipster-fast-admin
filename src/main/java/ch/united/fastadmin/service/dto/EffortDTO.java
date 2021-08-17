package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.ReportingEntityType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Effort} entity.
 */
public class EffortDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * id of user ,
     */
    @ApiModelProperty(value = "id of user ,")
    private Integer userId;

    /**
     * full name of user (optional)
     */
    @ApiModelProperty(value = "full name of user (optional)")
    private String userName;

    /**
     * type of assigned entity, one of: P - project, CU - cost unit, C - contact
     */
    @ApiModelProperty(value = "type of assigned entity, one of: P - project, CU - cost unit, C - contact")
    private ReportingEntityType entityType;

    /**
     * assigned entity id ,
     */
    @ApiModelProperty(value = "assigned entity id ,")
    private Integer entityId;

    /**
     * duration of effort
     */
    @ApiModelProperty(value = "duration of effort")
    private Double duration;

    /**
     * date of effort
     */
    @ApiModelProperty(value = "date of effort")
    private LocalDate date;

    /**
     * full activity name (optional)
     */
    @ApiModelProperty(value = "full activity name (optional)")
    private String activityName;

    /**
     * notes to this effort
     */
    @ApiModelProperty(value = "notes to this effort")
    private String notes;

    /**
     * if effort has been invoiced already
     */
    @ApiModelProperty(value = "if effort has been invoiced already")
    private Boolean isInvoiced;

    /**
     * update at
     */
    @ApiModelProperty(value = "update at")
    private ZonedDateTime updated;

    /**
     * hourly rate of this effort
     */
    @ApiModelProperty(value = "hourly rate of this effort")
    private Double hourlyRate;

    private ReportingActivityDTO activity;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ReportingEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(ReportingEntityType entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsInvoiced() {
        return isInvoiced;
    }

    public void setIsInvoiced(Boolean isInvoiced) {
        this.isInvoiced = isInvoiced;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public ReportingActivityDTO getActivity() {
        return activity;
    }

    public void setActivity(ReportingActivityDTO activity) {
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EffortDTO)) {
            return false;
        }

        EffortDTO effortDTO = (EffortDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, effortDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EffortDTO{" +
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
            ", activity=" + getActivity() +
            "}";
    }
}
