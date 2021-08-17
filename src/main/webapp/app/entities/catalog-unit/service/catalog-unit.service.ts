import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogUnit, getCatalogUnitIdentifier } from '../catalog-unit.model';

export type EntityResponseType = HttpResponse<ICatalogUnit>;
export type EntityArrayResponseType = HttpResponse<ICatalogUnit[]>;

@Injectable({ providedIn: 'root' })
export class CatalogUnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogUnit: ICatalogUnit): Observable<EntityResponseType> {
    return this.http.post<ICatalogUnit>(this.resourceUrl, catalogUnit, { observe: 'response' });
  }

  update(catalogUnit: ICatalogUnit): Observable<EntityResponseType> {
    return this.http.put<ICatalogUnit>(`${this.resourceUrl}/${getCatalogUnitIdentifier(catalogUnit) as number}`, catalogUnit, {
      observe: 'response',
    });
  }

  partialUpdate(catalogUnit: ICatalogUnit): Observable<EntityResponseType> {
    return this.http.patch<ICatalogUnit>(`${this.resourceUrl}/${getCatalogUnitIdentifier(catalogUnit) as number}`, catalogUnit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogUnitToCollectionIfMissing(
    catalogUnitCollection: ICatalogUnit[],
    ...catalogUnitsToCheck: (ICatalogUnit | null | undefined)[]
  ): ICatalogUnit[] {
    const catalogUnits: ICatalogUnit[] = catalogUnitsToCheck.filter(isPresent);
    if (catalogUnits.length > 0) {
      const catalogUnitCollectionIdentifiers = catalogUnitCollection.map(catalogUnitItem => getCatalogUnitIdentifier(catalogUnitItem)!);
      const catalogUnitsToAdd = catalogUnits.filter(catalogUnitItem => {
        const catalogUnitIdentifier = getCatalogUnitIdentifier(catalogUnitItem);
        if (catalogUnitIdentifier == null || catalogUnitCollectionIdentifiers.includes(catalogUnitIdentifier)) {
          return false;
        }
        catalogUnitCollectionIdentifiers.push(catalogUnitIdentifier);
        return true;
      });
      return [...catalogUnitsToAdd, ...catalogUnitCollection];
    }
    return catalogUnitCollection;
  }
}
