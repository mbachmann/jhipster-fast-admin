import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFreeTextFa, getFreeTextFaIdentifier } from '../free-text-fa.model';

export type EntityResponseType = HttpResponse<IFreeTextFa>;
export type EntityArrayResponseType = HttpResponse<IFreeTextFa[]>;

@Injectable({ providedIn: 'root' })
export class FreeTextFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/free-texts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(freeText: IFreeTextFa): Observable<EntityResponseType> {
    return this.http.post<IFreeTextFa>(this.resourceUrl, freeText, { observe: 'response' });
  }

  update(freeText: IFreeTextFa): Observable<EntityResponseType> {
    return this.http.put<IFreeTextFa>(`${this.resourceUrl}/${getFreeTextFaIdentifier(freeText) as number}`, freeText, {
      observe: 'response',
    });
  }

  partialUpdate(freeText: IFreeTextFa): Observable<EntityResponseType> {
    return this.http.patch<IFreeTextFa>(`${this.resourceUrl}/${getFreeTextFaIdentifier(freeText) as number}`, freeText, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFreeTextFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFreeTextFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFreeTextFaToCollectionIfMissing(
    freeTextCollection: IFreeTextFa[],
    ...freeTextsToCheck: (IFreeTextFa | null | undefined)[]
  ): IFreeTextFa[] {
    const freeTexts: IFreeTextFa[] = freeTextsToCheck.filter(isPresent);
    if (freeTexts.length > 0) {
      const freeTextCollectionIdentifiers = freeTextCollection.map(freeTextItem => getFreeTextFaIdentifier(freeTextItem)!);
      const freeTextsToAdd = freeTexts.filter(freeTextItem => {
        const freeTextIdentifier = getFreeTextFaIdentifier(freeTextItem);
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
