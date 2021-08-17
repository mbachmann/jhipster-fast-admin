package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.WorkingHour} entity.
 */
@ApiModel(description = "Working Hours of an ApplicationUser")
public class WorkingHourDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * (optional): full name of user
     */
    @ApiModelProperty(value = "(optional): full name of user")
    private String userName;

    /**
     * date of working hours time span
     */
    @ApiModelProperty(value = "date of working hours time span")
    private LocalDate date;

    /**
     * start of working hours time span (in HH:MM format)
     */
    @ApiModelProperty(value = "start of working hours time span (in HH:MM format)")
    private ZonedDateTime timeStart;

    /**
     * end of working hours time span (in HH:MM format)
     */
    @ApiModelProperty(value = "end of working hours time span (in HH:MM format)")
    private ZonedDateTime timeEnd;

    /**
     * date when working hours entity was created
     */
    @ApiModelProperty(value = "date when working hours entity was created")
    private ZonedDateTime created;

    private ApplicationUserDTO applicationUser;

    private EffortDTO effort;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ZonedDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(ZonedDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public ZonedDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(ZonedDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ApplicationUserDTO getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUserDTO applicationUser) {
        this.applicationUser = applicationUser;
    }

    public EffortDTO getEffort() {
        return effort;
    }

    public void setEffort(EffortDTO effort) {
        this.effort = effort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkingHourDTO)) {
            return false;
        }

        WorkingHourDTO workingHourDTO = (WorkingHourDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, workingHourDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkingHourDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", userName='" + getUserName() + "'" +
            ", date='" + getDate() + "'" +
            ", timeStart='" + getTimeStart() + "'" +
            ", timeEnd='" + getTimeEnd() + "'" +
            ", created='" + getCreated() + "'" +
            ", applicationUser=" + getApplicationUser() +
            ", effort=" + getEffort() +
            "}";
    }
}
