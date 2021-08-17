package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DomainArea;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.CustomField} entity.
 */
@ApiModel(description = "Additional information for a resource")
public class CustomFieldDTO implements Serializable {

    private Long id;

    /**
     * the custom field if for the domain resource
     */
    @ApiModelProperty(value = "the custom field if for the domain resource")
    private DomainArea domainArea;

    /**
     * the key to identify the custom field
     */
    @NotNull
    @ApiModelProperty(value = "the key to identify the custom field", required = true)
    private String key;

    /**
     * a name which appears on the dialog
     */
    @NotNull
    @ApiModelProperty(value = "a name which appears on the dialog", required = true)
    private String name;

    /**
     * the value of the resource
     */
    @ApiModelProperty(value = "the value of the resource")
    private String defaultValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DomainArea getDomainArea() {
        return domainArea;
    }

    public void setDomainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomFieldDTO)) {
            return false;
        }

        CustomFieldDTO customFieldDTO = (CustomFieldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, customFieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomFieldDTO{" +
            "id=" + getId() +
            ", domainArea='" + getDomainArea() + "'" +
            ", key='" + getKey() + "'" +
            ", name='" + getName() + "'" +
            ", defaultValue='" + getDefaultValue() + "'" +
            "}";
    }
}
