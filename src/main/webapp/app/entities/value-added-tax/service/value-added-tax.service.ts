import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IValueAddedTax, getValueAddedTaxIdentifier } from '../value-added-tax.model';

export type EntityResponseType = HttpResponse<IValueAddedTax>;
export type EntityArrayResponseType = HttpResponse<IValueAddedTax[]>;

@Injectable({ providedIn: 'root' })
export class ValueAddedTaxService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/value-added-taxes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(valueAddedTax: IValueAddedTax): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(valueAddedTax);
    return this.http
      .post<IValueAddedTax>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(valueAddedTax: IValueAddedTax): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(valueAddedTax);
    return this.http
      .put<IValueAddedTax>(`${this.resourceUrl}/${getValueAddedTaxIdentifier(valueAddedTax) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(valueAddedTax: IValueAddedTax): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(valueAddedTax);
    return this.http
      .patch<IValueAddedTax>(`${this.resourceUrl}/${getValueAddedTaxIdentifier(valueAddedTax) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IValueAddedTax>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IValueAddedTax[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addValueAddedTaxToCollectionIfMissing(
    valueAddedTaxCollection: IValueAddedTax[],
    ...valueAddedTaxesToCheck: (IValueAddedTax | null | undefined)[]
  ): IValueAddedTax[] {
    const valueAddedTaxes: IValueAddedTax[] = valueAddedTaxesToCheck.filter(isPresent);
    if (valueAddedTaxes.length > 0) {
      const valueAddedTaxCollectionIdentifiers = valueAddedTaxCollection.map(
        valueAddedTaxItem => getValueAddedTaxIdentifier(valueAddedTaxItem)!
      );
      const valueAddedTaxesToAdd = valueAddedTaxes.filter(valueAddedTaxItem => {
        const valueAddedTaxIdentifier = getValueAddedTaxIdentifier(valueAddedTaxItem);
        if (valueAddedTaxIdentifier == null || valueAddedTaxCollectionIdentifiers.includes(valueAddedTaxIdentifier)) {
          return false;
        }
        valueAddedTaxCollectionIdentifiers.push(valueAddedTaxIdentifier);
        return true;
      });
      return [...valueAddedTaxesToAdd, ...valueAddedTaxCollection];
    }
    return valueAddedTaxCollection;
  }

  protected convertDateFromClient(valueAddedTax: IValueAddedTax): IValueAddedTax {
    return Object.assign({}, valueAddedTax, {
      validFrom: valueAddedTax.validFrom?.isValid() ? valueAddedTax.validFrom.format(DATE_FORMAT) : undefined,
      validUntil: valueAddedTax.validUntil?.isValid() ? valueAddedTax.validUntil.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.validFrom = res.body.validFrom ? dayjs(res.body.validFrom) : undefined;
      res.body.validUntil = res.body.validUntil ? dayjs(res.body.validUntil) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((valueAddedTax: IValueAddedTax) => {
        valueAddedTax.validFrom = valueAddedTax.validFrom ? dayjs(valueAddedTax.validFrom) : undefined;
        valueAddedTax.validUntil = valueAddedTax.validUntil ? dayjs(valueAddedTax.validUntil) : undefined;
      });
    }
    return res;
  }
}
