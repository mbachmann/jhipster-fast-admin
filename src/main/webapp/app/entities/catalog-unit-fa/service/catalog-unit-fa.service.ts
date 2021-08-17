import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogUnitFa, getCatalogUnitFaIdentifier } from '../catalog-unit-fa.model';

export type EntityResponseType = HttpResponse<ICatalogUnitFa>;
export type EntityArrayResponseType = HttpResponse<ICatalogUnitFa[]>;

@Injectable({ providedIn: 'root' })
export class CatalogUnitFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogUnit: ICatalogUnitFa): Observable<EntityResponseType> {
    return this.http.post<ICatalogUnitFa>(this.resourceUrl, catalogUnit, { observe: 'response' });
  }

  update(catalogUnit: ICatalogUnitFa): Observable<EntityResponseType> {
    return this.http.put<ICatalogUnitFa>(`${this.resourceUrl}/${getCatalogUnitFaIdentifier(catalogUnit) as number}`, catalogUnit, {
      observe: 'response',
    });
  }

  partialUpdate(catalogUnit: ICatalogUnitFa): Observable<EntityResponseType> {
    return this.http.patch<ICatalogUnitFa>(`${this.resourceUrl}/${getCatalogUnitFaIdentifier(catalogUnit) as number}`, catalogUnit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogUnitFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogUnitFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogUnitFaToCollectionIfMissing(
    catalogUnitCollection: ICatalogUnitFa[],
    ...catalogUnitsToCheck: (ICatalogUnitFa | null | undefined)[]
  ): ICatalogUnitFa[] {
    const catalogUnits: ICatalogUnitFa[] = catalogUnitsToCheck.filter(isPresent);
    if (catalogUnits.length > 0) {
      const catalogUnitCollectionIdentifiers = catalogUnitCollection.map(catalogUnitItem => getCatalogUnitFaIdentifier(catalogUnitItem)!);
      const catalogUnitsToAdd = catalogUnits.filter(catalogUnitItem => {
        const catalogUnitIdentifier = getCatalogUnitFaIdentifier(catalogUnitItem);
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
