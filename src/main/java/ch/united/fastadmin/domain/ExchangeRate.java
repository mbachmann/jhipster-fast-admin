package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.Currency;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ExchangeRate.
 */
@Entity
@Table(name = "exchange_rate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ExchangeRate implements Serializable {

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
     * ISO currency name we are setting exchange from
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_from")
    private Currency currencyFrom;

    /**
     * ISO currency name we are setting exchange to
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "currency_to")
    private Currency currencyTo;

    /**
     * exchange rate value
     */
    @Column(name = "rate")
    private Double rate;

    /**
     * when contact was created
     */
    @Column(name = "created")
    private ZonedDateTime created;

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

    public ExchangeRate id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public ExchangeRate remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public Currency getCurrencyFrom() {
        return this.currencyFrom;
    }

    public ExchangeRate currencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
        return this;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public Currency getCurrencyTo() {
        return this.currencyTo;
    }

    public ExchangeRate currencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
        return this;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    public Double getRate() {
        return this.rate;
    }

    public ExchangeRate rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public ExchangeRate created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public ExchangeRate inactiv(Boolean inactiv) {
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
        if (!(o instanceof ExchangeRate)) {
            return false;
        }
        return id != null && id.equals(((ExchangeRate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExchangeRate{" +
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
