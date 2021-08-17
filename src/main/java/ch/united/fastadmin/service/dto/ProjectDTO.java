package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.ProjectStatus;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Project} entity.
 */
public class ProjectDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * project's internal number
     */
    @ApiModelProperty(value = "project's internal number")
    private String number;

    /**
     * related contact's name (optional)
     */
    @ApiModelProperty(value = "related contact's name (optional)")
    private String contactName;

    /**
     * project's name
     */
    @NotNull
    @ApiModelProperty(value = "project's name", required = true)
    private String name;

    /**
     * project's description
     */
    @ApiModelProperty(value = "project's description")
    private String description;

    /**
     * project's start date
     */
    @NotNull
    @ApiModelProperty(value = "project's start date", required = true)
    private LocalDate startDate;

    /**
     * project's estimated maximum effort (in hours)
     */
    @ApiModelProperty(value = "project's estimated maximum effort (in hours)")
    private Double hoursEstimated;

    /**
     * project's hourly rate
     */
    @ApiModelProperty(value = "project's hourly rate")
    private Double hourlyRate;

    /**
     * status to be set, on of:  ''O'' - open, ''C'' - closed, ''B'' - billed, ''P'' - pending'
     */
    @NotNull
    @ApiModelProperty(value = "status to be set, on of:  ''O'' - open, ''C'' - closed, ''B'' - billed, ''P'' - pending'", required = true)
    private ProjectStatus status;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Double getHoursEstimated() {
        return hoursEstimated;
    }

    public void setHoursEstimated(Double hoursEstimated) {
        this.hoursEstimated = hoursEstimated;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
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
        if (!(o instanceof ProjectDTO)) {
            return false;
        }

        ProjectDTO projectDTO = (ProjectDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projectDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectDTO{" +
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
            ", contact=" + getContact() +
            "}";
    }
}
