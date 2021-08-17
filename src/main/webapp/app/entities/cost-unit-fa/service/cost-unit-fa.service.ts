import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICostUnitFa, getCostUnitFaIdentifier } from '../cost-unit-fa.model';

export type EntityResponseType = HttpResponse<ICostUnitFa>;
export type EntityArrayResponseType = HttpResponse<ICostUnitFa[]>;

@Injectable({ providedIn: 'root' })
export class CostUnitFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cost-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(costUnit: ICostUnitFa): Observable<EntityResponseType> {
    return this.http.post<ICostUnitFa>(this.resourceUrl, costUnit, { observe: 'response' });
  }

  update(costUnit: ICostUnitFa): Observable<EntityResponseType> {
    return this.http.put<ICostUnitFa>(`${this.resourceUrl}/${getCostUnitFaIdentifier(costUnit) as number}`, costUnit, {
      observe: 'response',
    });
  }

  partialUpdate(costUnit: ICostUnitFa): Observable<EntityResponseType> {
    return this.http.patch<ICostUnitFa>(`${this.resourceUrl}/${getCostUnitFaIdentifier(costUnit) as number}`, costUnit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICostUnitFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICostUnitFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCostUnitFaToCollectionIfMissing(
    costUnitCollection: ICostUnitFa[],
    ...costUnitsToCheck: (ICostUnitFa | null | undefined)[]
  ): ICostUnitFa[] {
    const costUnits: ICostUnitFa[] = costUnitsToCheck.filter(isPresent);
    if (costUnits.length > 0) {
      const costUnitCollectionIdentifiers = costUnitCollection.map(costUnitItem => getCostUnitFaIdentifier(costUnitItem)!);
      const costUnitsToAdd = costUnits.filter(costUnitItem => {
        const costUnitIdentifier = getCostUnitFaIdentifier(costUnitItem);
        if (costUnitIdentifier == null || costUnitCollectionIdentifiers.includes(costUnitIdentifier)) {
          return false;
        }
        costUnitCollectionIdentifiers.push(costUnitIdentifier);
        return true;
      });
      return [...costUnitsToAdd, ...costUnitCollection];
    }
    return costUnitCollection;
  }
}
