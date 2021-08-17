import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogProductFa, getCatalogProductFaIdentifier } from '../catalog-product-fa.model';

export type EntityResponseType = HttpResponse<ICatalogProductFa>;
export type EntityArrayResponseType = HttpResponse<ICatalogProductFa[]>;

@Injectable({ providedIn: 'root' })
export class CatalogProductFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogProduct: ICatalogProductFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduct);
    return this.http
      .post<ICatalogProductFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(catalogProduct: ICatalogProductFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduct);
    return this.http
      .put<ICatalogProductFa>(`${this.resourceUrl}/${getCatalogProductFaIdentifier(catalogProduct) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(catalogProduct: ICatalogProductFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduct);
    return this.http
      .patch<ICatalogProductFa>(`${this.resourceUrl}/${getCatalogProductFaIdentifier(catalogProduct) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICatalogProductFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICatalogProductFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogProductFaToCollectionIfMissing(
    catalogProductCollection: ICatalogProductFa[],
    ...catalogProductsToCheck: (ICatalogProductFa | null | undefined)[]
  ): ICatalogProductFa[] {
    const catalogProducts: ICatalogProductFa[] = catalogProductsToCheck.filter(isPresent);
    if (catalogProducts.length > 0) {
      const catalogProductCollectionIdentifiers = catalogProductCollection.map(
        catalogProductItem => getCatalogProductFaIdentifier(catalogProductItem)!
      );
      const catalogProductsToAdd = catalogProducts.filter(catalogProductItem => {
        const catalogProductIdentifier = getCatalogProductFaIdentifier(catalogProductItem);
        if (catalogProductIdentifier == null || catalogProductCollectionIdentifiers.includes(catalogProductIdentifier)) {
          return false;
        }
        catalogProductCollectionIdentifiers.push(catalogProductIdentifier);
        return true;
      });
      return [...catalogProductsToAdd, ...catalogProductCollection];
    }
    return catalogProductCollection;
  }

  protected convertDateFromClient(catalogProduct: ICatalogProductFa): ICatalogProductFa {
    return Object.assign({}, catalogProduct, {
      created: catalogProduct.created?.isValid() ? catalogProduct.created.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((catalogProduct: ICatalogProductFa) => {
        catalogProduct.created = catalogProduct.created ? dayjs(catalogProduct.created) : undefined;
      });
    }
    return res;
  }
}
