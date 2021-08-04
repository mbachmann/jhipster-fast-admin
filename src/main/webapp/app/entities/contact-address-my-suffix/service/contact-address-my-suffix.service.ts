import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IContactAddressMySuffix, getContactAddressMySuffixIdentifier } from '../contact-address-my-suffix.model';

export type EntityResponseType = HttpResponse<IContactAddressMySuffix>;
export type EntityArrayResponseType = HttpResponse<IContactAddressMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class ContactAddressMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-addresses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contact-addresses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactAddress: IContactAddressMySuffix): Observable<EntityResponseType> {
    return this.http.post<IContactAddressMySuffix>(this.resourceUrl, contactAddress, { observe: 'response' });
  }

  update(contactAddress: IContactAddressMySuffix): Observable<EntityResponseType> {
    return this.http.put<IContactAddressMySuffix>(
      `${this.resourceUrl}/${getContactAddressMySuffixIdentifier(contactAddress) as number}`,
      contactAddress,
      { observe: 'response' }
    );
  }

  partialUpdate(contactAddress: IContactAddressMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IContactAddressMySuffix>(
      `${this.resourceUrl}/${getContactAddressMySuffixIdentifier(contactAddress) as number}`,
      contactAddress,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactAddressMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactAddressMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactAddressMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addContactAddressMySuffixToCollectionIfMissing(
    contactAddressCollection: IContactAddressMySuffix[],
    ...contactAddressesToCheck: (IContactAddressMySuffix | null | undefined)[]
  ): IContactAddressMySuffix[] {
    const contactAddresses: IContactAddressMySuffix[] = contactAddressesToCheck.filter(isPresent);
    if (contactAddresses.length > 0) {
      const contactAddressCollectionIdentifiers = contactAddressCollection.map(
        contactAddressItem => getContactAddressMySuffixIdentifier(contactAddressItem)!
      );
      const contactAddressesToAdd = contactAddresses.filter(contactAddressItem => {
        const contactAddressIdentifier = getContactAddressMySuffixIdentifier(contactAddressItem);
        if (contactAddressIdentifier == null || contactAddressCollectionIdentifiers.includes(contactAddressIdentifier)) {
          return false;
        }
        contactAddressCollectionIdentifiers.push(contactAddressIdentifier);
        return true;
      });
      return [...contactAddressesToAdd, ...contactAddressCollection];
    }
    return contactAddressCollection;
  }
}
