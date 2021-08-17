import * as dayjs from 'dayjs';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IExchangeRate {
  id?: number;
  remoteId?: number | null;
  currencyFrom?: Currency | null;
  currencyTo?: Currency | null;
  rate?: number | null;
  created?: dayjs.Dayjs | null;
  inactiv?: boolean | null;
}

export class ExchangeRate implements IExchangeRate {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public currencyFrom?: Currency | null,
    public currencyTo?: Currency | null,
    public rate?: number | null,
    public created?: dayjs.Dayjs | null,
    public inactiv?: boolean | null
  ) {
    this.inactiv = this.inactiv ?? false;
  }
}

export function getExchangeRateIdentifier(exchangeRate: IExchangeRate): number | undefined {
  return exchangeRate.id;
}
