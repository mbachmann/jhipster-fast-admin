package ch.united.fastadmin.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Inherits from the User
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * short user name
     */
    @NotNull
    @Column(name = "short_cut_name", nullable = false)
    private String shortCutName;

    /**
     * member since
     */
    @Column(name = "member_since")
    private LocalDate memberSince;

    /**
     * image of contact
     */
    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;

    /**
     * image type
     */
    @Column(name = "image_type")
    private String imageType;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getShortCutName() {
        return this.shortCutName;
    }

    public ApplicationUser shortCutName(String shortCutName) {
        this.shortCutName = shortCutName;
        return this;
    }

    public void setShortCutName(String shortCutName) {
        this.shortCutName = shortCutName;
    }

    public LocalDate getMemberSince() {
        return this.memberSince;
    }

    public ApplicationUser memberSince(LocalDate memberSince) {
        this.memberSince = memberSince;
        return this;
    }

    public void setMemberSince(LocalDate memberSince) {
        this.memberSince = memberSince;
    }

    public byte[] getAvatar() {
        return this.avatar;
    }

    public ApplicationUser avatar(byte[] avatar) {
        this.avatar = avatar;
        return this;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return this.avatarContentType;
    }

    public ApplicationUser avatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
        return this;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public String getImageType() {
        return this.imageType;
    }

    public ApplicationUser imageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public ApplicationUser inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", shortCutName='" + getShortCutName() + "'" +
            ", memberSince='" + getMemberSince() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", avatarContentType='" + getAvatarContentType() + "'" +
            ", imageType='" + getImageType() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
