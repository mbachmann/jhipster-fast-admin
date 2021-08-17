import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFreeText, getFreeTextIdentifier } from '../free-text.model';

export type EntityResponseType = HttpResponse<IFreeText>;
export type EntityArrayResponseType = HttpResponse<IFreeText[]>;

@Injectable({ providedIn: 'root' })
export class FreeTextService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/free-texts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(freeText: IFreeText): Observable<EntityResponseType> {
    return this.http.post<IFreeText>(this.resourceUrl, freeText, { observe: 'response' });
  }

  update(freeText: IFreeText): Observable<EntityResponseType> {
    return this.http.put<IFreeText>(`${this.resourceUrl}/${getFreeTextIdentifier(freeText) as number}`, freeText, { observe: 'response' });
  }

  partialUpdate(freeText: IFreeText): Observable<EntityResponseType> {
    return this.http.patch<IFreeText>(`${this.resourceUrl}/${getFreeTextIdentifier(freeText) as number}`, freeText, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFreeText>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFreeText[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFreeTextToCollectionIfMissing(freeTextCollection: IFreeText[], ...freeTextsToCheck: (IFreeText | null | undefined)[]): IFreeText[] {
    const freeTexts: IFreeText[] = freeTextsToCheck.filter(isPresent);
    if (freeTexts.length > 0) {
      const freeTextCollectionIdentifiers = freeTextCollection.map(freeTextItem => getFreeTextIdentifier(freeTextItem)!);
      const freeTextsToAdd = freeTexts.filter(freeTextItem => {
        const freeTextIdentifier = getFreeTextIdentifier(freeTextItem);
        if (freeTextIdentifier == null || freeTextCollectionIdentifiers.includes(freeTextIdentifier)) {
          return false;
        }
        freeTextCollectionIdentifiers.push(freeTextIdentifier);
        return true;
      });
      return [...freeTextsToAdd, ...freeTextCollection];
    }
    return freeTextCollection;
  }
}
