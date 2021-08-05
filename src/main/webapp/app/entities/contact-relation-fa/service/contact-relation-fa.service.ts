import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IContactRelationFa, getContactRelationFaIdentifier } from '../contact-relation-fa.model';

export type EntityResponseType = HttpResponse<IContactRelationFa>;
export type EntityArrayResponseType = HttpResponse<IContactRelationFa[]>;

@Injectable({ providedIn: 'root' })
export class ContactRelationFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-relations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contact-relations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactRelation: IContactRelationFa): Observable<EntityResponseType> {
    return this.http.post<IContactRelationFa>(this.resourceUrl, contactRelation, { observe: 'response' });
  }

  update(contactRelation: IContactRelationFa): Observable<EntityResponseType> {
    return this.http.put<IContactRelationFa>(
      `${this.resourceUrl}/${getContactRelationFaIdentifier(contactRelation) as number}`,
      contactRelation,
      { observe: 'response' }
    );
  }

  partialUpdate(contactRelation: IContactRelationFa): Observable<EntityResponseType> {
    return this.http.patch<IContactRelationFa>(
      `${this.resourceUrl}/${getContactRelationFaIdentifier(contactRelation) as number}`,
      contactRelation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactRelationFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactRelationFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactRelationFa[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addContactRelationFaToCollectionIfMissing(
    contactRelationCollection: IContactRelationFa[],
    ...contactRelationsToCheck: (IContactRelationFa | null | undefined)[]
  ): IContactRelationFa[] {
    const contactRelations: IContactRelationFa[] = contactRelationsToCheck.filter(isPresent);
    if (contactRelations.length > 0) {
      const contactRelationCollectionIdentifiers = contactRelationCollection.map(
        contactRelationItem => getContactRelationFaIdentifier(contactRelationItem)!
      );
      const contactRelationsToAdd = contactRelations.filter(contactRelationItem => {
        const contactRelationIdentifier = getContactRelationFaIdentifier(contactRelationItem);
        if (contactRelationIdentifier == null || contactRelationCollectionIdentifiers.includes(contactRelationIdentifier)) {
          return false;
        }
        contactRelationCollectionIdentifiers.push(contactRelationIdentifier);
        return true;
      });
      return [...contactRelationsToAdd, ...contactRelationCollection];
    }
    return contactRelationCollection;
  }
}
