package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.CatalogScope;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.CatalogUnit} entity.
 */
@ApiModel(
    description = "System Units\n\n14  -         All\n17  Credit    All\n13  Flat      All\n7  Item      Product\n9  Litres    Product\n16  Metres    Product\n15  Minutes   All\n8  kg        Product\n12  km        All\n10  m2        Product\n11  m3        Product"
)
public class CatalogUnitDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * Name of the Unit
     */
    @NotNull
    @ApiModelProperty(value = "Name of the Unit", required = true)
    private String name;

    private String nameDe;

    private String nameEn;

    private String nameFr;

    private String nameIt;

    /**
     * scope, one of: s - service, p - product, a - all
     */
    @ApiModelProperty(value = "scope, one of: s - service, p - product, a - all")
    private CatalogScope scope;

    /**
     * is unit a custom company unit (if not, it's a system unit)
     */
    @ApiModelProperty(value = "is unit a custom company unit (if not, it's a system unit)")
    private Boolean custom;

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

    public Integer getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameIt() {
        return nameIt;
    }

    public void setNameIt(String nameIt) {
        this.nameIt = nameIt;
    }

    public CatalogScope getScope() {
        return scope;
    }

    public void setScope(CatalogScope scope) {
        this.scope = scope;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
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
        if (!(o instanceof CatalogUnitDTO)) {
            return false;
        }

        CatalogUnitDTO catalogUnitDTO = (CatalogUnitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogUnitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogUnitDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", nameDe='" + getNameDe() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameFr='" + getNameFr() + "'" +
            ", nameIt='" + getNameIt() + "'" +
            ", scope='" + getScope() + "'" +
            ", custom='" + getCustom() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
