import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IOwnerFa, getOwnerFaIdentifier } from '../owner-fa.model';

export type EntityResponseType = HttpResponse<IOwnerFa>;
export type EntityArrayResponseType = HttpResponse<IOwnerFa[]>;

@Injectable({ providedIn: 'root' })
export class OwnerFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/owners');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/owners');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(owner: IOwnerFa): Observable<EntityResponseType> {
    return this.http.post<IOwnerFa>(this.resourceUrl, owner, { observe: 'response' });
  }

  update(owner: IOwnerFa): Observable<EntityResponseType> {
    return this.http.put<IOwnerFa>(`${this.resourceUrl}/${getOwnerFaIdentifier(owner) as number}`, owner, { observe: 'response' });
  }

  partialUpdate(owner: IOwnerFa): Observable<EntityResponseType> {
    return this.http.patch<IOwnerFa>(`${this.resourceUrl}/${getOwnerFaIdentifier(owner) as number}`, owner, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOwnerFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOwnerFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOwnerFa[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addOwnerFaToCollectionIfMissing(ownerCollection: IOwnerFa[], ...ownersToCheck: (IOwnerFa | null | undefined)[]): IOwnerFa[] {
    const owners: IOwnerFa[] = ownersToCheck.filter(isPresent);
    if (owners.length > 0) {
      const ownerCollectionIdentifiers = ownerCollection.map(ownerItem => getOwnerFaIdentifier(ownerItem)!);
      const ownersToAdd = owners.filter(ownerItem => {
        const ownerIdentifier = getOwnerFaIdentifier(ownerItem);
        if (ownerIdentifier == null || ownerCollectionIdentifiers.includes(ownerIdentifier)) {
          return false;
        }
        ownerCollectionIdentifiers.push(ownerIdentifier);
        return true;
      });
      return [...ownersToAdd, ...ownerCollection];
    }
    return ownerCollection;
  }
}
