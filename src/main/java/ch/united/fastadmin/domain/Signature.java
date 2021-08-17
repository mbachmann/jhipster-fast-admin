package ch.united.fastadmin.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * User Signatures for documents
 */
@Entity
@Table(name = "signature")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Signature implements Serializable {

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
     * the image of the signature
     */
    @Lob
    @Column(name = "signature_image")
    private byte[] signatureImage;

    @Column(name = "signature_image_content_type")
    private String signatureImageContentType;

    /**
     * the width of the signature
     */
    @Column(name = "width")
    private Integer width;

    /**
     * the height of the signature
     */
    @Column(name = "heigth")
    private Integer heigth;

    /**
     * the user name of this signature
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * the signature belongs to the application user
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

    public Signature id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public Signature remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public byte[] getSignatureImage() {
        return this.signatureImage;
    }

    public Signature signatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
        return this;
    }

    public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }

    public String getSignatureImageContentType() {
        return this.signatureImageContentType;
    }

    public Signature signatureImageContentType(String signatureImageContentType) {
        this.signatureImageContentType = signatureImageContentType;
        return this;
    }

    public void setSignatureImageContentType(String signatureImageContentType) {
        this.signatureImageContentType = signatureImageContentType;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Signature width(Integer width) {
        this.width = width;
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeigth() {
        return this.heigth;
    }

    public Signature heigth(Integer heigth) {
        this.heigth = heigth;
        return this;
    }

    public void setHeigth(Integer heigth) {
        this.heigth = heigth;
    }

    public String getUserName() {
        return this.userName;
    }

    public Signature userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public Signature applicationUser(ApplicationUser applicationUser) {
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
        if (!(o instanceof Signature)) {
            return false;
        }
        return id != null && id.equals(((Signature) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Signature{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", signatureImage='" + getSignatureImage() + "'" +
            ", signatureImageContentType='" + getSignatureImageContentType() + "'" +
            ", width=" + getWidth() +
            ", heigth=" + getHeigth() +
            ", userName='" + getUserName() + "'" +
            "}";
    }
}
