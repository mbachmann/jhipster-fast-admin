import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogCategoryFa, getCatalogCategoryFaIdentifier } from '../catalog-category-fa.model';

export type EntityResponseType = HttpResponse<ICatalogCategoryFa>;
export type EntityArrayResponseType = HttpResponse<ICatalogCategoryFa[]>;

@Injectable({ providedIn: 'root' })
export class CatalogCategoryFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogCategory: ICatalogCategoryFa): Observable<EntityResponseType> {
    return this.http.post<ICatalogCategoryFa>(this.resourceUrl, catalogCategory, { observe: 'response' });
  }

  update(catalogCategory: ICatalogCategoryFa): Observable<EntityResponseType> {
    return this.http.put<ICatalogCategoryFa>(
      `${this.resourceUrl}/${getCatalogCategoryFaIdentifier(catalogCategory) as number}`,
      catalogCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(catalogCategory: ICatalogCategoryFa): Observable<EntityResponseType> {
    return this.http.patch<ICatalogCategoryFa>(
      `${this.resourceUrl}/${getCatalogCategoryFaIdentifier(catalogCategory) as number}`,
      catalogCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogCategoryFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogCategoryFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogCategoryFaToCollectionIfMissing(
    catalogCategoryCollection: ICatalogCategoryFa[],
    ...catalogCategoriesToCheck: (ICatalogCategoryFa | null | undefined)[]
  ): ICatalogCategoryFa[] {
    const catalogCategories: ICatalogCategoryFa[] = catalogCategoriesToCheck.filter(isPresent);
    if (catalogCategories.length > 0) {
      const catalogCategoryCollectionIdentifiers = catalogCategoryCollection.map(
        catalogCategoryItem => getCatalogCategoryFaIdentifier(catalogCategoryItem)!
      );
      const catalogCategoriesToAdd = catalogCategories.filter(catalogCategoryItem => {
        const catalogCategoryIdentifier = getCatalogCategoryFaIdentifier(catalogCategoryItem);
        if (catalogCategoryIdentifier == null || catalogCategoryCollectionIdentifiers.includes(catalogCategoryIdentifier)) {
          return false;
        }
        catalogCategoryCollectionIdentifiers.push(catalogCategoryIdentifier);
        return true;
      });
      return [...catalogCategoriesToAdd, ...catalogCategoryCollection];
    }
    return catalogCategoryCollection;
  }
}
