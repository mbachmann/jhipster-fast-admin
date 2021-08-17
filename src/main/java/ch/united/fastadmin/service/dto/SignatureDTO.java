package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Signature} entity.
 */
@ApiModel(description = "User Signatures for documents")
public class SignatureDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * the image of the signature
     */
    @ApiModelProperty(value = "the image of the signature")
    @Lob
    private byte[] signatureImage;

    private String signatureImageContentType;

    /**
     * the width of the signature
     */
    @ApiModelProperty(value = "the width of the signature")
    private Integer width;

    /**
     * the height of the signature
     */
    @ApiModelProperty(value = "the height of the signature")
    private Integer heigth;

    /**
     * the user name of this signature
     */
    @ApiModelProperty(value = "the user name of this signature")
    private String userName;

    private ApplicationUserDTO applicationUser;

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

    public byte[] getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(byte[] signatureImage) {
        this.signatureImage = signatureImage;
    }

    public String getSignatureImageContentType() {
        return signatureImageContentType;
    }

    public void setSignatureImageContentType(String signatureImageContentType) {
        this.signatureImageContentType = signatureImageContentType;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeigth() {
        return heigth;
    }

    public void setHeigth(Integer heigth) {
        this.heigth = heigth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ApplicationUserDTO getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUserDTO applicationUser) {
        this.applicationUser = applicationUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignatureDTO)) {
            return false;
        }

        SignatureDTO signatureDTO = (SignatureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, signatureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignatureDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", signatureImage='" + getSignatureImage() + "'" +
            ", width=" + getWidth() +
            ", heigth=" + getHeigth() +
            ", userName='" + getUserName() + "'" +
            ", applicationUser=" + getApplicationUser() +
            "}";
    }
}
