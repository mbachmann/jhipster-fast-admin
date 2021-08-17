import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogProduct, getCatalogProductIdentifier } from '../catalog-product.model';

export type EntityResponseType = HttpResponse<ICatalogProduct>;
export type EntityArrayResponseType = HttpResponse<ICatalogProduct[]>;

@Injectable({ providedIn: 'root' })
export class CatalogProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogProduct: ICatalogProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduct);
    return this.http
      .post<ICatalogProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(catalogProduct: ICatalogProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduct);
    return this.http
      .put<ICatalogProduct>(`${this.resourceUrl}/${getCatalogProductIdentifier(catalogProduct) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(catalogProduct: ICatalogProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogProduct);
    return this.http
      .patch<ICatalogProduct>(`${this.resourceUrl}/${getCatalogProductIdentifier(catalogProduct) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICatalogProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICatalogProduct[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogProductToCollectionIfMissing(
    catalogProductCollection: ICatalogProduct[],
    ...catalogProductsToCheck: (ICatalogProduct | null | undefined)[]
  ): ICatalogProduct[] {
    const catalogProducts: ICatalogProduct[] = catalogProductsToCheck.filter(isPresent);
    if (catalogProducts.length > 0) {
      const catalogProductCollectionIdentifiers = catalogProductCollection.map(
        catalogProductItem => getCatalogProductIdentifier(catalogProductItem)!
      );
      const catalogProductsToAdd = catalogProducts.filter(catalogProductItem => {
        const catalogProductIdentifier = getCatalogProductIdentifier(catalogProductItem);
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

  protected convertDateFromClient(catalogProduct: ICatalogProduct): ICatalogProduct {
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
      res.body.forEach((catalogProduct: ICatalogProduct) => {
        catalogProduct.created = catalogProduct.created ? dayjs(catalogProduct.created) : undefined;
      });
    }
    return res;
  }
}
