import { AutoSynch } from 'app/entities/enumerations/auto-synch.model';
import { AutoSynchDirection } from 'app/entities/enumerations/auto-synch-direction.model';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IBankAccountFa {
  id?: number;
  remoteId?: number | null;
  defaultBankAccount?: boolean | null;
  description?: string | null;
  bankName?: string | null;
  number?: string | null;
  iban?: string | null;
  bic?: string | null;
  postAccount?: string | null;
  autoSync?: AutoSynch | null;
  autoSyncDirection?: AutoSynchDirection | null;
  currency?: Currency | null;
  inactiv?: boolean | null;
}

export class BankAccountFa implements IBankAccountFa {
  constructor(
    public id?: number,
    public remoteId?: number | null,
    public defaultBankAccount?: boolean | null,
    public description?: string | null,
    public bankName?: string | null,
    public number?: string | null,
    public iban?: string | null,
    public bic?: string | null,
    public postAccount?: string | null,
    public autoSync?: AutoSynch | null,
    public autoSyncDirection?: AutoSynchDirection | null,
    public currency?: Currency | null,
    public inactiv?: boolean | null
  ) {
    this.defaultBankAccount = this.defaultBankAccount ?? false;
    this.inactiv = this.inactiv ?? false;
  }
}

export function getBankAccountFaIdentifier(bankAccount: IBankAccountFa): number | undefined {
  return bankAccount.id;
}
