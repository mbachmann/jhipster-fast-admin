import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactAddressFa, getContactAddressFaIdentifier } from '../contact-address-fa.model';

export type EntityResponseType = HttpResponse<IContactAddressFa>;
export type EntityArrayResponseType = HttpResponse<IContactAddressFa[]>;

@Injectable({ providedIn: 'root' })
export class ContactAddressFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-addresses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactAddress: IContactAddressFa): Observable<EntityResponseType> {
    return this.http.post<IContactAddressFa>(this.resourceUrl, contactAddress, { observe: 'response' });
  }

  update(contactAddress: IContactAddressFa): Observable<EntityResponseType> {
    return this.http.put<IContactAddressFa>(
      `${this.resourceUrl}/${getContactAddressFaIdentifier(contactAddress) as number}`,
      contactAddress,
      { observe: 'response' }
    );
  }

  partialUpdate(contactAddress: IContactAddressFa): Observable<EntityResponseType> {
    return this.http.patch<IContactAddressFa>(
      `${this.resourceUrl}/${getContactAddressFaIdentifier(contactAddress) as number}`,
      contactAddress,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactAddressFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactAddressFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactAddressFaToCollectionIfMissing(
    contactAddressCollection: IContactAddressFa[],
    ...contactAddressesToCheck: (IContactAddressFa | null | undefined)[]
  ): IContactAddressFa[] {
    const contactAddresses: IContactAddressFa[] = contactAddressesToCheck.filter(isPresent);
    if (contactAddresses.length > 0) {
      const contactAddressCollectionIdentifiers = contactAddressCollection.map(
        contactAddressItem => getContactAddressFaIdentifier(contactAddressItem)!
      );
      const contactAddressesToAdd = contactAddresses.filter(contactAddressItem => {
        const contactAddressIdentifier = getContactAddressFaIdentifier(contactAddressItem);
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
