package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DomainArea;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * actions that can be performed on an item (possible values: view, edit, manage) for own or all
 */
@Entity
@Table(name = "resource_permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResourcePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * the use can add a new resource
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fa_add", nullable = false)
    private PermissionType add;

    /**
     * the use can edit a resource
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "edit", nullable = false)
    private PermissionType edit;

    /**
     * the use can manage a resource
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "manage", nullable = false)
    private PermissionType manage;

    /**
     * the permission is for the domain resource
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "domain_area", nullable = false)
    private DomainArea domainArea;

    /**
     * The permission definition is for the application role
     */
    @ManyToOne
    private ApplicationRole role;

    /**
     * The permission definition is for the user
     */
    @ManyToOne
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResourcePermission id(Long id) {
        this.id = id;
        return this;
    }

    public PermissionType getAdd() {
        return this.add;
    }

    public ResourcePermission add(PermissionType add) {
        this.add = add;
        return this;
    }

    public void setAdd(PermissionType add) {
        this.add = add;
    }

    public PermissionType getEdit() {
        return this.edit;
    }

    public ResourcePermission edit(PermissionType edit) {
        this.edit = edit;
        return this;
    }

    public void setEdit(PermissionType edit) {
        this.edit = edit;
    }

    public PermissionType getManage() {
        return this.manage;
    }

    public ResourcePermission manage(PermissionType manage) {
        this.manage = manage;
        return this;
    }

    public void setManage(PermissionType manage) {
        this.manage = manage;
    }

    public DomainArea getDomainArea() {
        return this.domainArea;
    }

    public ResourcePermission domainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
        return this;
    }

    public void setDomainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
    }

    public ApplicationRole getRole() {
        return this.role;
    }

    public ResourcePermission role(ApplicationRole applicationRole) {
        this.setRole(applicationRole);
        return this;
    }

    public void setRole(ApplicationRole applicationRole) {
        this.role = applicationRole;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public ResourcePermission applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourcePermission)) {
            return false;
        }
        return id != null && id.equals(((ResourcePermission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourcePermission{" +
            "id=" + getId() +
            ", add='" + getAdd() + "'" +
            ", edit='" + getEdit() + "'" +
            ", manage='" + getManage() + "'" +
            ", domainArea='" + getDomainArea() + "'" +
            "}";
    }
}
