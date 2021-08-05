package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DomainArea;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * actions that can be performed on an item (possible values: view, edit)
 */
@Entity
@Table(name = "permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "permission")
public class Permission implements Serializable {

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

    @ManyToOne
    private Role role;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contactMainAddress", "permissions", "groups", "customFields", "relations" }, allowSetters = true)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permission id(Long id) {
        this.id = id;
        return this;
    }

    public PermissionType getAdd() {
        return this.add;
    }

    public Permission add(PermissionType add) {
        this.add = add;
        return this;
    }

    public void setAdd(PermissionType add) {
        this.add = add;
    }

    public PermissionType getEdit() {
        return this.edit;
    }

    public Permission edit(PermissionType edit) {
        this.edit = edit;
        return this;
    }

    public void setEdit(PermissionType edit) {
        this.edit = edit;
    }

    public PermissionType getManage() {
        return this.manage;
    }

    public Permission manage(PermissionType manage) {
        this.manage = manage;
        return this;
    }

    public void setManage(PermissionType manage) {
        this.manage = manage;
    }

    public DomainArea getDomainArea() {
        return this.domainArea;
    }

    public Permission domainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
        return this;
    }

    public void setDomainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
    }

    public Role getRole() {
        return this.role;
    }

    public Permission role(Role role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Contact getContact() {
        return this.contact;
    }

    public Permission contact(Contact contact) {
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
        if (!(o instanceof Permission)) {
            return false;
        }
        return id != null && id.equals(((Permission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Permission{" +
            "id=" + getId() +
            ", add='" + getAdd() + "'" +
            ", edit='" + getEdit() + "'" +
            ", manage='" + getManage() + "'" +
            ", domainArea='" + getDomainArea() + "'" +
            "}";
    }
}
