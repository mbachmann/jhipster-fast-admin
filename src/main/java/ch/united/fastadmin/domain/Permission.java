package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DomainResource;
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

    @NotNull
    @Column(name = "new_all", nullable = false)
    private Boolean newAll;

    @NotNull
    @Column(name = "edit_own", nullable = false)
    private Boolean editOwn;

    @NotNull
    @Column(name = "edit_all", nullable = false)
    private Boolean editAll;

    @NotNull
    @Column(name = "view_own", nullable = false)
    private Boolean viewOwn;

    @NotNull
    @Column(name = "view_all", nullable = false)
    private Boolean viewAll;

    @NotNull
    @Column(name = "manage_own", nullable = false)
    private Boolean manageOwn;

    @NotNull
    @Column(name = "manage_all", nullable = false)
    private Boolean manageAll;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "domain_resource", nullable = false)
    private DomainResource domainResource;

    @ManyToOne
    private Role role;

    @ManyToOne
    @JsonIgnoreProperties(value = { "mainAddress", "permissions", "groups", "customFields" }, allowSetters = true)
    private Contact contact;

    @ManyToOne
    @JsonIgnoreProperties(value = { "permissions" }, allowSetters = true)
    private ContactAddress contactAddress;

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

    public Boolean getNewAll() {
        return this.newAll;
    }

    public Permission newAll(Boolean newAll) {
        this.newAll = newAll;
        return this;
    }

    public void setNewAll(Boolean newAll) {
        this.newAll = newAll;
    }

    public Boolean getEditOwn() {
        return this.editOwn;
    }

    public Permission editOwn(Boolean editOwn) {
        this.editOwn = editOwn;
        return this;
    }

    public void setEditOwn(Boolean editOwn) {
        this.editOwn = editOwn;
    }

    public Boolean getEditAll() {
        return this.editAll;
    }

    public Permission editAll(Boolean editAll) {
        this.editAll = editAll;
        return this;
    }

    public void setEditAll(Boolean editAll) {
        this.editAll = editAll;
    }

    public Boolean getViewOwn() {
        return this.viewOwn;
    }

    public Permission viewOwn(Boolean viewOwn) {
        this.viewOwn = viewOwn;
        return this;
    }

    public void setViewOwn(Boolean viewOwn) {
        this.viewOwn = viewOwn;
    }

    public Boolean getViewAll() {
        return this.viewAll;
    }

    public Permission viewAll(Boolean viewAll) {
        this.viewAll = viewAll;
        return this;
    }

    public void setViewAll(Boolean viewAll) {
        this.viewAll = viewAll;
    }

    public Boolean getManageOwn() {
        return this.manageOwn;
    }

    public Permission manageOwn(Boolean manageOwn) {
        this.manageOwn = manageOwn;
        return this;
    }

    public void setManageOwn(Boolean manageOwn) {
        this.manageOwn = manageOwn;
    }

    public Boolean getManageAll() {
        return this.manageAll;
    }

    public Permission manageAll(Boolean manageAll) {
        this.manageAll = manageAll;
        return this;
    }

    public void setManageAll(Boolean manageAll) {
        this.manageAll = manageAll;
    }

    public DomainResource getDomainResource() {
        return this.domainResource;
    }

    public Permission domainResource(DomainResource domainResource) {
        this.domainResource = domainResource;
        return this;
    }

    public void setDomainResource(DomainResource domainResource) {
        this.domainResource = domainResource;
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

    public ContactAddress getContactAddress() {
        return this.contactAddress;
    }

    public Permission contactAddress(ContactAddress contactAddress) {
        this.setContactAddress(contactAddress);
        return this;
    }

    public void setContactAddress(ContactAddress contactAddress) {
        this.contactAddress = contactAddress;
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
            ", newAll='" + getNewAll() + "'" +
            ", editOwn='" + getEditOwn() + "'" +
            ", editAll='" + getEditAll() + "'" +
            ", viewOwn='" + getViewOwn() + "'" +
            ", viewAll='" + getViewAll() + "'" +
            ", manageOwn='" + getManageOwn() + "'" +
            ", manageAll='" + getManageAll() + "'" +
            ", domainResource='" + getDomainResource() + "'" +
            "}";
    }
}
