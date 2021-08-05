import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IOwnerMySuffix, getOwnerMySuffixIdentifier } from '../owner-my-suffix.model';

export type EntityResponseType = HttpResponse<IOwnerMySuffix>;
export type EntityArrayResponseType = HttpResponse<IOwnerMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class OwnerMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/owners');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/owners');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(owner: IOwnerMySuffix): Observable<EntityResponseType> {
    return this.http.post<IOwnerMySuffix>(this.resourceUrl, owner, { observe: 'response' });
  }

  update(owner: IOwnerMySuffix): Observable<EntityResponseType> {
    return this.http.put<IOwnerMySuffix>(`${this.resourceUrl}/${getOwnerMySuffixIdentifier(owner) as number}`, owner, {
      observe: 'response',
    });
  }

  partialUpdate(owner: IOwnerMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IOwnerMySuffix>(`${this.resourceUrl}/${getOwnerMySuffixIdentifier(owner) as number}`, owner, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOwnerMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOwnerMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOwnerMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addOwnerMySuffixToCollectionIfMissing(
    ownerCollection: IOwnerMySuffix[],
    ...ownersToCheck: (IOwnerMySuffix | null | undefined)[]
  ): IOwnerMySuffix[] {
    const owners: IOwnerMySuffix[] = ownersToCheck.filter(isPresent);
    if (owners.length > 0) {
      const ownerCollectionIdentifiers = ownerCollection.map(ownerItem => getOwnerMySuffixIdentifier(ownerItem)!);
      const ownersToAdd = owners.filter(ownerItem => {
        const ownerIdentifier = getOwnerMySuffixIdentifier(ownerItem);
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
