package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.Currency;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ExchangeRate} entity.
 */
public class ExchangeRateDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * ISO currency name we are setting exchange from
     */
    @ApiModelProperty(value = "ISO currency name we are setting exchange from")
    private Currency currencyFrom;

    /**
     * ISO currency name we are setting exchange to
     */
    @ApiModelProperty(value = "ISO currency name we are setting exchange to")
    private Currency currencyTo;

    /**
     * exchange rate value
     */
    @ApiModelProperty(value = "exchange rate value")
    private Double rate;

    /**
     * when contact was created
     */
    @ApiModelProperty(value = "when contact was created")
    private ZonedDateTime created;

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

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
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
        if (!(o instanceof ExchangeRateDTO)) {
            return false;
        }

        ExchangeRateDTO exchangeRateDTO = (ExchangeRateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, exchangeRateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExchangeRateDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", currencyFrom='" + getCurrencyFrom() + "'" +
            ", currencyTo='" + getCurrencyTo() + "'" +
            ", rate=" + getRate() +
            ", created='" + getCreated() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
