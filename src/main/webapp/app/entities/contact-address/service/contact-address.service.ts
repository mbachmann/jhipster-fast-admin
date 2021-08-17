import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactAddress, getContactAddressIdentifier } from '../contact-address.model';

export type EntityResponseType = HttpResponse<IContactAddress>;
export type EntityArrayResponseType = HttpResponse<IContactAddress[]>;

@Injectable({ providedIn: 'root' })
export class ContactAddressService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-addresses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactAddress: IContactAddress): Observable<EntityResponseType> {
    return this.http.post<IContactAddress>(this.resourceUrl, contactAddress, { observe: 'response' });
  }

  update(contactAddress: IContactAddress): Observable<EntityResponseType> {
    return this.http.put<IContactAddress>(`${this.resourceUrl}/${getContactAddressIdentifier(contactAddress) as number}`, contactAddress, {
      observe: 'response',
    });
  }

  partialUpdate(contactAddress: IContactAddress): Observable<EntityResponseType> {
    return this.http.patch<IContactAddress>(
      `${this.resourceUrl}/${getContactAddressIdentifier(contactAddress) as number}`,
      contactAddress,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactAddress>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactAddress[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactAddressToCollectionIfMissing(
    contactAddressCollection: IContactAddress[],
    ...contactAddressesToCheck: (IContactAddress | null | undefined)[]
  ): IContactAddress[] {
    const contactAddresses: IContactAddress[] = contactAddressesToCheck.filter(isPresent);
    if (contactAddresses.length > 0) {
      const contactAddressCollectionIdentifiers = contactAddressCollection.map(
        contactAddressItem => getContactAddressIdentifier(contactAddressItem)!
      );
      const contactAddressesToAdd = contactAddresses.filter(contactAddressItem => {
        const contactAddressIdentifier = getContactAddressIdentifier(contactAddressItem);
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
