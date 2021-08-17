import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContactRelation, getContactRelationIdentifier } from '../contact-relation.model';

export type EntityResponseType = HttpResponse<IContactRelation>;
export type EntityArrayResponseType = HttpResponse<IContactRelation[]>;

@Injectable({ providedIn: 'root' })
export class ContactRelationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contact-relations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contactRelation: IContactRelation): Observable<EntityResponseType> {
    return this.http.post<IContactRelation>(this.resourceUrl, contactRelation, { observe: 'response' });
  }

  update(contactRelation: IContactRelation): Observable<EntityResponseType> {
    return this.http.put<IContactRelation>(
      `${this.resourceUrl}/${getContactRelationIdentifier(contactRelation) as number}`,
      contactRelation,
      { observe: 'response' }
    );
  }

  partialUpdate(contactRelation: IContactRelation): Observable<EntityResponseType> {
    return this.http.patch<IContactRelation>(
      `${this.resourceUrl}/${getContactRelationIdentifier(contactRelation) as number}`,
      contactRelation,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContactRelation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContactRelation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContactRelationToCollectionIfMissing(
    contactRelationCollection: IContactRelation[],
    ...contactRelationsToCheck: (IContactRelation | null | undefined)[]
  ): IContactRelation[] {
    const contactRelations: IContactRelation[] = contactRelationsToCheck.filter(isPresent);
    if (contactRelations.length > 0) {
      const contactRelationCollectionIdentifiers = contactRelationCollection.map(
        contactRelationItem => getContactRelationIdentifier(contactRelationItem)!
      );
      const contactRelationsToAdd = contactRelations.filter(contactRelationItem => {
        const contactRelationIdentifier = getContactRelationIdentifier(contactRelationItem);
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
