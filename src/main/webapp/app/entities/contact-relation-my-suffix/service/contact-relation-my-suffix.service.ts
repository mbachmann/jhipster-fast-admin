import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IContactRelationMySuffix, getContactRelationMySuffixIdentifier } from '../contact-relation-my-suffix.model';

export type EntityResponseType = HttpResponse<IContactRelationMySuffix>;
export type EntityArrayResponseType = HttpResponse<IContactRelationMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class ContactRelationMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-relations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contact-relations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactRelation: IContactRelationMySuffix): Observable<EntityResponseType> {
    return this.http.post<IContactRelationMySuffix>(this.resourceUrl, contactRelation, { observe: 'response' });
  }

  update(contactRelation: IContactRelationMySuffix): Observable<EntityResponseType> {
    return this.http.put<IContactRelationMySuffix>(
      `${this.resourceUrl}/${getContactRelationMySuffixIdentifier(contactRelation) as number}`,
      contactRelation,
      { observe: 'response' }
    );
  }

  partialUpdate(contactRelation: IContactRelationMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IContactRelationMySuffix>(
      `${this.resourceUrl}/${getContactRelationMySuffixIdentifier(contactRelation) as number}`,
      contactRelation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactRelationMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactRelationMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactRelationMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addContactRelationMySuffixToCollectionIfMissing(
    contactRelationCollection: IContactRelationMySuffix[],
    ...contactRelationsToCheck: (IContactRelationMySuffix | null | undefined)[]
  ): IContactRelationMySuffix[] {
    const contactRelations: IContactRelationMySuffix[] = contactRelationsToCheck.filter(isPresent);
    if (contactRelations.length > 0) {
      const contactRelationCollectionIdentifiers = contactRelationCollection.map(
        contactRelationItem => getContactRelationMySuffixIdentifier(contactRelationItem)!
      );
      const contactRelationsToAdd = contactRelations.filter(contactRelationItem => {
        const contactRelationIdentifier = getContactRelationMySuffixIdentifier(contactRelationItem);
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
