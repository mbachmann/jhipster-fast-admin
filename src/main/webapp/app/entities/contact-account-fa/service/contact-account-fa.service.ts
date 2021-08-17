import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactAccountFa, getContactAccountFaIdentifier } from '../contact-account-fa.model';

export type EntityResponseType = HttpResponse<IContactAccountFa>;
export type EntityArrayResponseType = HttpResponse<IContactAccountFa[]>;

@Injectable({ providedIn: 'root' })
export class ContactAccountFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactAccount: IContactAccountFa): Observable<EntityResponseType> {
    return this.http.post<IContactAccountFa>(this.resourceUrl, contactAccount, { observe: 'response' });
  }

  update(contactAccount: IContactAccountFa): Observable<EntityResponseType> {
    return this.http.put<IContactAccountFa>(
      `${this.resourceUrl}/${getContactAccountFaIdentifier(contactAccount) as number}`,
      contactAccount,
      { observe: 'response' }
    );
  }

  partialUpdate(contactAccount: IContactAccountFa): Observable<EntityResponseType> {
    return this.http.patch<IContactAccountFa>(
      `${this.resourceUrl}/${getContactAccountFaIdentifier(contactAccount) as number}`,
      contactAccount,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactAccountFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactAccountFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactAccountFaToCollectionIfMissing(
    contactAccountCollection: IContactAccountFa[],
    ...contactAccountsToCheck: (IContactAccountFa | null | undefined)[]
  ): IContactAccountFa[] {
    const contactAccounts: IContactAccountFa[] = contactAccountsToCheck.filter(isPresent);
    if (contactAccounts.length > 0) {
      const contactAccountCollectionIdentifiers = contactAccountCollection.map(
        contactAccountItem => getContactAccountFaIdentifier(contactAccountItem)!
      );
      const contactAccountsToAdd = contactAccounts.filter(contactAccountItem => {
        const contactAccountIdentifier = getContactAccountFaIdentifier(contactAccountItem);
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
