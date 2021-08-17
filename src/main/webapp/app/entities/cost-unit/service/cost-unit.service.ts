import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICostUnit, getCostUnitIdentifier } from '../cost-unit.model';

export type EntityResponseType = HttpResponse<ICostUnit>;
export type EntityArrayResponseType = HttpResponse<ICostUnit[]>;

@Injectable({ providedIn: 'root' })
export class CostUnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cost-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(costUnit: ICostUnit): Observable<EntityResponseType> {
    return this.http.post<ICostUnit>(this.resourceUrl, costUnit, { observe: 'response' });
  }

  update(costUnit: ICostUnit): Observable<EntityResponseType> {
    return this.http.put<ICostUnit>(`${this.resourceUrl}/${getCostUnitIdentifier(costUnit) as number}`, costUnit, { observe: 'response' });
  }

  partialUpdate(costUnit: ICostUnit): Observable<EntityResponseType> {
    return this.http.patch<ICostUnit>(`${this.resourceUrl}/${getCostUnitIdentifier(costUnit) as number}`, costUnit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICostUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICostUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCostUnitToCollectionIfMissing(costUnitCollection: ICostUnit[], ...costUnitsToCheck: (ICostUnit | null | undefined)[]): ICostUnit[] {
    const costUnits: ICostUnit[] = costUnitsToCheck.filter(isPresent);
    if (costUnits.length > 0) {
      const costUnitCollectionIdentifiers = costUnitCollection.map(costUnitItem => getCostUnitIdentifier(costUnitItem)!);
      const costUnitsToAdd = costUnits.filter(costUnitItem => {
        const costUnitIdentifier = getCostUnitIdentifier(costUnitItem);
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
