package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.DomainArea;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.Permission} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.PermissionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /permissions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PermissionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PermissionType
     */
    public static class PermissionTypeFilter extends Filter<PermissionType> {

        public PermissionTypeFilter() {}

        public PermissionTypeFilter(PermissionTypeFilter filter) {
            super(filter);
        }

        @Override
        public PermissionTypeFilter copy() {
            return new PermissionTypeFilter(this);
        }
    }

    /**
     * Class for filtering DomainArea
     */
    public static class DomainAreaFilter extends Filter<DomainArea> {

        public DomainAreaFilter() {}

        public DomainAreaFilter(DomainAreaFilter filter) {
            super(filter);
        }

        @Override
        public DomainAreaFilter copy() {
            return new DomainAreaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private PermissionTypeFilter add;

    private PermissionTypeFilter edit;

    private PermissionTypeFilter manage;

    private DomainAreaFilter domainArea;

    private LongFilter roleId;

    private LongFilter contactId;

    public PermissionCriteria() {}

    public PermissionCriteria(PermissionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.add = other.add == null ? null : other.add.copy();
        this.edit = other.edit == null ? null : other.edit.copy();
        this.manage = other.manage == null ? null : other.manage.copy();
        this.domainArea = other.domainArea == null ? null : other.domainArea.copy();
        this.roleId = other.roleId == null ? null : other.roleId.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
    }

    @Override
    public PermissionCriteria copy() {
        return new PermissionCriteria(this);
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

    public PermissionTypeFilter getAdd() {
        return add;
    }

    public PermissionTypeFilter add() {
        if (add == null) {
            add = new PermissionTypeFilter();
        }
        return add;
    }

    public void setAdd(PermissionTypeFilter add) {
        this.add = add;
    }

    public PermissionTypeFilter getEdit() {
        return edit;
    }

    public PermissionTypeFilter edit() {
        if (edit == null) {
            edit = new PermissionTypeFilter();
        }
        return edit;
    }

    public void setEdit(PermissionTypeFilter edit) {
        this.edit = edit;
    }

    public PermissionTypeFilter getManage() {
        return manage;
    }

    public PermissionTypeFilter manage() {
        if (manage == null) {
            manage = new PermissionTypeFilter();
        }
        return manage;
    }

    public void setManage(PermissionTypeFilter manage) {
        this.manage = manage;
    }

    public DomainAreaFilter getDomainArea() {
        return domainArea;
    }

    public DomainAreaFilter domainArea() {
        if (domainArea == null) {
            domainArea = new DomainAreaFilter();
        }
        return domainArea;
    }

    public void setDomainArea(DomainAreaFilter domainArea) {
        this.domainArea = domainArea;
    }

    public LongFilter getRoleId() {
        return roleId;
    }

    public LongFilter roleId() {
        if (roleId == null) {
            roleId = new LongFilter();
        }
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
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
        final PermissionCriteria that = (PermissionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(add, that.add) &&
            Objects.equals(edit, that.edit) &&
            Objects.equals(manage, that.manage) &&
            Objects.equals(domainArea, that.domainArea) &&
            Objects.equals(roleId, that.roleId) &&
            Objects.equals(contactId, that.contactId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, add, edit, manage, domainArea, roleId, contactId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (add != null ? "add=" + add + ", " : "") +
            (edit != null ? "edit=" + edit + ", " : "") +
            (manage != null ? "manage=" + manage + ", " : "") +
            (domainArea != null ? "domainArea=" + domainArea + ", " : "") +
            (roleId != null ? "roleId=" + roleId + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            "}";
    }
}
