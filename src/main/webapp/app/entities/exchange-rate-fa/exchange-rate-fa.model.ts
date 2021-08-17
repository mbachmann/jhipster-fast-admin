import * as dayjs from 'dayjs';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IExchangeRateFa {
  id?: number;
  remoteId?: number | null;
  currencyFrom?: Currency | null;
  currencyTo?: Currency | null;
  rate?: number | null;
  created?: dayjs.Dayjs | null;
  inactiv?: boolean | null;
}

export class ExchangeRateFa implements IExchangeRateFa {
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

export function getExchangeRateFaIdentifier(exchangeRate: IExchangeRateFa): number | undefined {
  return exchangeRate.id;
}
