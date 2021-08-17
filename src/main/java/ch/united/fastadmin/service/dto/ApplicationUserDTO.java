package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ApplicationUser} entity.
 */
@ApiModel(description = "Inherits from the User")
public class ApplicationUserDTO implements Serializable {

    private Long id;

    /**
     * short user name
     */
    @NotNull
    @ApiModelProperty(value = "short user name", required = true)
    private String shortCutName;

    /**
     * member since
     */
    @ApiModelProperty(value = "member since")
    private LocalDate memberSince;

    /**
     * image of contact
     */
    @ApiModelProperty(value = "image of contact")
    @Lob
    private byte[] avatar;

    private String avatarContentType;

    /**
     * image type
     */
    @ApiModelProperty(value = "image type")
    private String imageType;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortCutName() {
        return shortCutName;
    }

    public void setShortCutName(String shortCutName) {
        this.shortCutName = shortCutName;
    }

    public LocalDate getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(LocalDate memberSince) {
        this.memberSince = memberSince;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getAvatarContentType() {
        return avatarContentType;
    }

    public void setAvatarContentType(String avatarContentType) {
        this.avatarContentType = avatarContentType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Boolean getInactiv() {
        return inactiv;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUserDTO)) {
            return false;
        }

        ApplicationUserDTO applicationUserDTO = (ApplicationUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicationUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUserDTO{" +
            "id=" + getId() +
            ", shortCutName='" + getShortCutName() + "'" +
            ", memberSince='" + getMemberSince() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", imageType='" + getImageType() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
