package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.CatalogCategory} entity.
 */
@ApiModel(description = "Category for Products or Services")
public class CatalogCategoryDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * Category name
     */
    @NotNull
    @ApiModelProperty(value = "Category name", required = true)
    private String name;

    private String accountingAccountNumber;

    /**
     * how many catalog items are using this category
     */
    @ApiModelProperty(value = "how many catalog items are using this category")
    private Integer usage;

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

    public String getAccountingAccountNumber() {
        return accountingAccountNumber;
    }

    public void setAccountingAccountNumber(String accountingAccountNumber) {
        this.accountingAccountNumber = accountingAccountNumber;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
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
        if (!(o instanceof CatalogCategoryDTO)) {
            return false;
        }

        CatalogCategoryDTO catalogCategoryDTO = (CatalogCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogCategoryDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", accountingAccountNumber='" + getAccountingAccountNumber() + "'" +
            ", usage=" + getUsage() +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
