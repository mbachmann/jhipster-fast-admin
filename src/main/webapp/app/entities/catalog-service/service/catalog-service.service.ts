import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICatalogService, getCatalogServiceIdentifier } from '../catalog-service.model';

export type EntityResponseType = HttpResponse<ICatalogService>;
export type EntityArrayResponseType = HttpResponse<ICatalogService[]>;

@Injectable({ providedIn: 'root' })
export class CatalogServiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/catalog-services');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(catalogService: ICatalogService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogService);
    return this.http
      .post<ICatalogService>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(catalogService: ICatalogService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogService);
    return this.http
      .put<ICatalogService>(`${this.resourceUrl}/${getCatalogServiceIdentifier(catalogService) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(catalogService: ICatalogService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(catalogService);
    return this.http
      .patch<ICatalogService>(`${this.resourceUrl}/${getCatalogServiceIdentifier(catalogService) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICatalogService>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICatalogService[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCatalogServiceToCollectionIfMissing(
    catalogServiceCollection: ICatalogService[],
    ...catalogServicesToCheck: (ICatalogService | null | undefined)[]
  ): ICatalogService[] {
    const catalogServices: ICatalogService[] = catalogServicesToCheck.filter(isPresent);
    if (catalogServices.length > 0) {
      const catalogServiceCollectionIdentifiers = catalogServiceCollection.map(
        catalogServiceItem => getCatalogServiceIdentifier(catalogServiceItem)!
      );
      const catalogServicesToAdd = catalogServices.filter(catalogServiceItem => {
        const catalogServiceIdentifier = getCatalogServiceIdentifier(catalogServiceItem);
        if (catalogServiceIdentifier == null || catalogServiceCollectionIdentifiers.includes(catalogServiceIdentifier)) {
          return false;
        }
        catalogServiceCollectionIdentifiers.push(catalogServiceIdentifier);
        return true;
      });
      return [...catalogServicesToAdd, ...catalogServiceCollection];
    }
    return catalogServiceCollection;
  }

  protected convertDateFromClient(catalogService: ICatalogService): ICatalogService {
    return Object.assign({}, catalogService, {
      created: catalogService.created?.isValid() ? catalogService.created.toJSON() : undefined,
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
      res.body.forEach((catalogService: ICatalogService) => {
        catalogService.created = catalogService.created ? dayjs(catalogService.created) : undefined;
      });
    }
    return res;
  }
}
