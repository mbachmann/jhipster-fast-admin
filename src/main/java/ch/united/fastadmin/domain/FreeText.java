package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * free texts objects
 */
@Entity
@Table(name = "free_text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FreeText implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * displayed text
     */
    @Column(name = "text")
    private String text;

    /**
     * font size of displayed text (min: 1; max: 50)
     */
    @Column(name = "font_size")
    private Integer fontSize;

    /**
     * The x position of the free text
     */
    @Column(name = "position_x")
    private Double positionX;

    /**
     * The y position of the free text
     */
    @Column(name = "position_y")
    private Double positionY;

    /**
     * which page text is displayed on
     */
    @Column(name = "page_no")
    private Integer pageNo;

    /**
     * language; possible values: de, en, es, fr, it ,
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private DocumentLanguage language;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FreeText id(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public FreeText text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getFontSize() {
        return this.fontSize;
    }

    public FreeText fontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Double getPositionX() {
        return this.positionX;
    }

    public FreeText positionX(Double positionX) {
        this.positionX = positionX;
        return this;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return this.positionY;
    }

    public FreeText positionY(Double positionY) {
        this.positionY = positionY;
        return this;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public FreeText pageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public DocumentLanguage getLanguage() {
        return this.language;
    }

    public FreeText language(DocumentLanguage language) {
        this.language = language;
        return this;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FreeText)) {
            return false;
        }
        return id != null && id.equals(((FreeText) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FreeText{" +
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
