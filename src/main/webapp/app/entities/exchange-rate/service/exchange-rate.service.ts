import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExchangeRate, getExchangeRateIdentifier } from '../exchange-rate.model';

export type EntityResponseType = HttpResponse<IExchangeRate>;
export type EntityArrayResponseType = HttpResponse<IExchangeRate[]>;

@Injectable({ providedIn: 'root' })
export class ExchangeRateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exchange-rates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(exchangeRate: IExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .post<IExchangeRate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(exchangeRate: IExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .put<IExchangeRate>(`${this.resourceUrl}/${getExchangeRateIdentifier(exchangeRate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(exchangeRate: IExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .patch<IExchangeRate>(`${this.resourceUrl}/${getExchangeRateIdentifier(exchangeRate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExchangeRate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExchangeRate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExchangeRateToCollectionIfMissing(
    exchangeRateCollection: IExchangeRate[],
    ...exchangeRatesToCheck: (IExchangeRate | null | undefined)[]
  ): IExchangeRate[] {
    const exchangeRates: IExchangeRate[] = exchangeRatesToCheck.filter(isPresent);
    if (exchangeRates.length > 0) {
      const exchangeRateCollectionIdentifiers = exchangeRateCollection.map(
        exchangeRateItem => getExchangeRateIdentifier(exchangeRateItem)!
      );
      const exchangeRatesToAdd = exchangeRates.filter(exchangeRateItem => {
        const exchangeRateIdentifier = getExchangeRateIdentifier(exchangeRateItem);
        if (exchangeRateIdentifier == null || exchangeRateCollectionIdentifiers.includes(exchangeRateIdentifier)) {
          return false;
        }
        exchangeRateCollectionIdentifiers.push(exchangeRateIdentifier);
        return true;
      });
      return [...exchangeRatesToAdd, ...exchangeRateCollection];
    }
    return exchangeRateCollection;
  }

  protected convertDateFromClient(exchangeRate: IExchangeRate): IExchangeRate {
    return Object.assign({}, exchangeRate, {
      created: exchangeRate.created?.isValid() ? exchangeRate.created.toJSON() : undefined,
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
      res.body.forEach((exchangeRate: IExchangeRate) => {
        exchangeRate.created = exchangeRate.created ? dayjs(exchangeRate.created) : undefined;
      });
    }
    return res;
  }
}
