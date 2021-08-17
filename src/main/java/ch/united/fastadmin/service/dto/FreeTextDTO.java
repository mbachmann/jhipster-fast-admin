package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.FreeText} entity.
 */
@ApiModel(description = "free texts objects")
public class FreeTextDTO implements Serializable {

    private Long id;

    /**
     * displayed text
     */
    @ApiModelProperty(value = "displayed text")
    private String text;

    /**
     * font size of displayed text (min: 1; max: 50)
     */
    @ApiModelProperty(value = "font size of displayed text (min: 1; max: 50)")
    private Integer fontSize;

    /**
     * The x position of the free text
     */
    @ApiModelProperty(value = "The x position of the free text")
    private Double positionX;

    /**
     * The y position of the free text
     */
    @ApiModelProperty(value = "The y position of the free text")
    private Double positionY;

    /**
     * which page text is displayed on
     */
    @ApiModelProperty(value = "which page text is displayed on")
    private Integer pageNo;

    /**
     * language; possible values: de, en, es, fr, it ,
     */
    @ApiModelProperty(value = "language; possible values: de, en, es, fr, it ,")
    private DocumentLanguage language;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Double getPositionX() {
        return positionX;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public DocumentLanguage getLanguage() {
        return language;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreeTextDTO)) {
            return false;
        }

        FreeTextDTO freeTextDTO = (FreeTextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, freeTextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FreeTextDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", fontSize=" + getFontSize() +
            ", positionX=" + getPositionX() +
            ", positionY=" + getPositionY() +
            ", pageNo=" + getPageNo() +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
