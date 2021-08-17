import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactAccount, getContactAccountIdentifier } from '../contact-account.model';

export type EntityResponseType = HttpResponse<IContactAccount>;
export type EntityArrayResponseType = HttpResponse<IContactAccount[]>;

@Injectable({ providedIn: 'root' })
export class ContactAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactAccount: IContactAccount): Observable<EntityResponseType> {
    return this.http.post<IContactAccount>(this.resourceUrl, contactAccount, { observe: 'response' });
  }

  update(contactAccount: IContactAccount): Observable<EntityResponseType> {
    return this.http.put<IContactAccount>(`${this.resourceUrl}/${getContactAccountIdentifier(contactAccount) as number}`, contactAccount, {
      observe: 'response',
    });
  }

  partialUpdate(contactAccount: IContactAccount): Observable<EntityResponseType> {
    return this.http.patch<IContactAccount>(
      `${this.resourceUrl}/${getContactAccountIdentifier(contactAccount) as number}`,
      contactAccount,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactAccountToCollectionIfMissing(
    contactAccountCollection: IContactAccount[],
    ...contactAccountsToCheck: (IContactAccount | null | undefined)[]
  ): IContactAccount[] {
    const contactAccounts: IContactAccount[] = contactAccountsToCheck.filter(isPresent);
    if (contactAccounts.length > 0) {
      const contactAccountCollectionIdentifiers = contactAccountCollection.map(
        contactAccountItem => getContactAccountIdentifier(contactAccountItem)!
      );
      const contactAccountsToAdd = contactAccounts.filter(contactAccountItem => {
        const contactAccountIdentifier = getContactAccountIdentifier(contactAccountItem);
        if (contactAccountIdentifier == null || contactAccountCollectionIdentifiers.includes(contactAccountIdentifier)) {
          return false;
        }
        contactAccountCollectionIdentifiers.push(contactAccountIdentifier);
        return true;
      });
      return [...contactAccountsToAdd, ...contactAccountCollection];
    }
    return contactAccountCollection;
  }
}
