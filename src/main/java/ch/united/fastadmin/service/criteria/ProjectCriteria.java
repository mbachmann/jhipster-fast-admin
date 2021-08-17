package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.ProjectStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.Project} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.ProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ProjectStatus
     */
    public static class ProjectStatusFilter extends Filter<ProjectStatus> {

        public ProjectStatusFilter() {}

        public ProjectStatusFilter(ProjectStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProjectStatusFilter copy() {
            return new ProjectStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private StringFilter number;

    private StringFilter contactName;

    private StringFilter name;

    private StringFilter description;

    private LocalDateFilter startDate;

    private DoubleFilter hoursEstimated;

    private DoubleFilter hourlyRate;

    private ProjectStatusFilter status;

    private LongFilter customFieldsId;

    private LongFilter contactId;

    public ProjectCriteria() {}

    public ProjectCriteria(ProjectCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.contactName = other.contactName == null ? null : other.contactName.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.hoursEstimated = other.hoursEstimated == null ? null : other.hoursEstimated.copy();
        this.hourlyRate = other.hourlyRate == null ? null : other.hourlyRate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.customFieldsId = other.customFieldsId == null ? null : other.customFieldsId.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
    }

    @Override
    public ProjectCriteria copy() {
        return new ProjectCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getRemoteId() {
        return remoteId;
    }

    public IntegerFilter remoteId() {
        if (remoteId == null) {
            remoteId = new IntegerFilter();
        }
        return remoteId;
    }

    public void setRemoteId(IntegerFilter remoteId) {
        this.remoteId = remoteId;
    }

    public StringFilter getNumber() {
        return number;
    }

    public StringFilter number() {
        if (number == null) {
            number = new StringFilter();
        }
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public StringFilter getContactName() {
        return contactName;
    }

    public StringFilter contactName() {
        if (contactName == null) {
            contactName = new StringFilter();
        }
        return contactName;
    }

    public void setContactName(StringFilter contactName) {
        this.contactName = contactName;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public DoubleFilter getHoursEstimated() {
        return hoursEstimated;
    }

    public DoubleFilter hoursEstimated() {
        if (hoursEstimated == null) {
            hoursEstimated = new DoubleFilter();
        }
        return hoursEstimated;
    }

    public void setHoursEstimated(DoubleFilter hoursEstimated) {
        this.hoursEstimated = hoursEstimated;
    }

    public DoubleFilter getHourlyRate() {
        return hourlyRate;
    }

    public DoubleFilter hourlyRate() {
        if (hourlyRate == null) {
            hourlyRate = new DoubleFilter();
        }
        return hourlyRate;
    }

    public void setHourlyRate(DoubleFilter hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public ProjectStatusFilter getStatus() {
        return status;
    }

    public ProjectStatusFilter status() {
        if (status == null) {
            status = new ProjectStatusFilter();
        }
        return status;
    }

    public void setStatus(ProjectStatusFilter status) {
        this.status = status;
    }

    public LongFilter getCustomFieldsId() {
        return customFieldsId;
    }

    public LongFilter customFieldsId() {
        if (customFieldsId == null) {
            customFieldsId = new LongFilter();
        }
        return customFieldsId;
    }

    public void setCustomFieldsId(LongFilter customFieldsId) {
        this.customFieldsId = customFieldsId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public LongFilter contactId() {
        if (contactId == null) {
            contactId = new LongFilter();
        }
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjectCriteria that = (ProjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(number, that.number) &&
            Objects.equals(contactName, that.contactName) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(hoursEstimated, that.hoursEstimated) &&
            Objects.equals(hourlyRate, that.hourlyRate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(customFieldsId, that.customFieldsId) &&
            Objects.equals(contactId, that.contactId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remoteId,
            number,
            contactName,
            name,
            description,
            startDate,
            hoursEstimated,
            hourlyRate,
            status,
            customFieldsId,
            contactId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjectCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (contactName != null ? "contactName=" + contactName + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (hoursEstimated != null ? "hoursEstimated=" + hoursEstimated + ", " : "") +
            (hourlyRate != null ? "hourlyRate=" + hourlyRate + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (customFieldsId != null ? "customFieldsId=" + customFieldsId + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            "}";
    }
}
