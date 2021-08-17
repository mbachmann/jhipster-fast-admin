import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogCategory, getCatalogCategoryIdentifier } from '../catalog-category.model';

export type EntityResponseType = HttpResponse<ICatalogCategory>;
export type EntityArrayResponseType = HttpResponse<ICatalogCategory[]>;

@Injectable({ providedIn: 'root' })
export class CatalogCategoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-categories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogCategory: ICatalogCategory): Observable<EntityResponseType> {
    return this.http.post<ICatalogCategory>(this.resourceUrl, catalogCategory, { observe: 'response' });
  }

  update(catalogCategory: ICatalogCategory): Observable<EntityResponseType> {
    return this.http.put<ICatalogCategory>(
      `${this.resourceUrl}/${getCatalogCategoryIdentifier(catalogCategory) as number}`,
      catalogCategory,
      { observe: 'response' }
    );
  }

  partialUpdate(catalogCategory: ICatalogCategory): Observable<EntityResponseType> {
    return this.http.patch<ICatalogCategory>(
      `${this.resourceUrl}/${getCatalogCategoryIdentifier(catalogCategory) as number}`,
      catalogCategory,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICatalogCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICatalogCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogCategoryToCollectionIfMissing(
    catalogCategoryCollection: ICatalogCategory[],
    ...catalogCategoriesToCheck: (ICatalogCategory | null | undefined)[]
  ): ICatalogCategory[] {
    const catalogCategories: ICatalogCategory[] = catalogCategoriesToCheck.filter(isPresent);
    if (catalogCategories.length > 0) {
      const catalogCategoryCollectionIdentifiers = catalogCategoryCollection.map(
        catalogCategoryItem => getCatalogCategoryIdentifier(catalogCategoryItem)!
      );
      const catalogCategoriesToAdd = catalogCategories.filter(catalogCategoryItem => {
        const catalogCategoryIdentifier = getCatalogCategoryIdentifier(catalogCategoryItem);
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
