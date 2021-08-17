import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBankAccountFa, getBankAccountFaIdentifier } from '../bank-account-fa.model';

export type EntityResponseType = HttpResponse<IBankAccountFa>;
export type EntityArrayResponseType = HttpResponse<IBankAccountFa[]>;

@Injectable({ providedIn: 'root' })
export class BankAccountFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bankAccount: IBankAccountFa): Observable<EntityResponseType> {
    return this.http.post<IBankAccountFa>(this.resourceUrl, bankAccount, { observe: 'response' });
  }

  update(bankAccount: IBankAccountFa): Observable<EntityResponseType> {
    return this.http.put<IBankAccountFa>(`${this.resourceUrl}/${getBankAccountFaIdentifier(bankAccount) as number}`, bankAccount, {
      observe: 'response',
    });
  }

  partialUpdate(bankAccount: IBankAccountFa): Observable<EntityResponseType> {
    return this.http.patch<IBankAccountFa>(`${this.resourceUrl}/${getBankAccountFaIdentifier(bankAccount) as number}`, bankAccount, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBankAccountFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBankAccountFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBankAccountFaToCollectionIfMissing(
    bankAccountCollection: IBankAccountFa[],
    ...bankAccountsToCheck: (IBankAccountFa | null | undefined)[]
  ): IBankAccountFa[] {
    const bankAccounts: IBankAccountFa[] = bankAccountsToCheck.filter(isPresent);
    if (bankAccounts.length > 0) {
      const bankAccountCollectionIdentifiers = bankAccountCollection.map(bankAccountItem => getBankAccountFaIdentifier(bankAccountItem)!);
      const bankAccountsToAdd = bankAccounts.filter(bankAccountItem => {
        const bankAccountIdentifier = getBankAccountFaIdentifier(bankAccountItem);
        if (bankAccountIdentifier == null || bankAccountCollectionIdentifiers.includes(bankAccountIdentifier)) {
          return false;
        }
        bankAccountCollectionIdentifiers.push(bankAccountIdentifier);
        return true;
      });
      return [...bankAccountsToAdd, ...bankAccountCollection];
    }
    return bankAccountCollection;
  }
}
