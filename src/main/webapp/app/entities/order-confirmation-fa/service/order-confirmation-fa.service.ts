import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderConfirmationFa, getOrderConfirmationFaIdentifier } from '../order-confirmation-fa.model';

export type EntityResponseType = HttpResponse<IOrderConfirmationFa>;
export type EntityArrayResponseType = HttpResponse<IOrderConfirmationFa[]>;

@Injectable({ providedIn: 'root' })
export class OrderConfirmationFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-confirmations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderConfirmation: IOrderConfirmationFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderConfirmation);
    return this.http
      .post<IOrderConfirmationFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(orderConfirmation: IOrderConfirmationFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderConfirmation);
    return this.http
      .put<IOrderConfirmationFa>(`${this.resourceUrl}/${getOrderConfirmationFaIdentifier(orderConfirmation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(orderConfirmation: IOrderConfirmationFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderConfirmation);
    return this.http
      .patch<IOrderConfirmationFa>(`${this.resourceUrl}/${getOrderConfirmationFaIdentifier(orderConfirmation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrderConfirmationFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrderConfirmationFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrderConfirmationFaToCollectionIfMissing(
    orderConfirmationCollection: IOrderConfirmationFa[],
    ...orderConfirmationsToCheck: (IOrderConfirmationFa | null | undefined)[]
  ): IOrderConfirmationFa[] {
    const orderConfirmations: IOrderConfirmationFa[] = orderConfirmationsToCheck.filter(isPresent);
    if (orderConfirmations.length > 0) {
      const orderConfirmationCollectionIdentifiers = orderConfirmationCollection.map(
        orderConfirmationItem => getOrderConfirmationFaIdentifier(orderConfirmationItem)!
      );
      const orderConfirmationsToAdd = orderConfirmations.filter(orderConfirmationItem => {
        const orderConfirmationIdentifier = getOrderConfirmationFaIdentifier(orderConfirmationItem);
        if (orderConfirmationIdentifier == null || orderConfirmationCollectionIdentifiers.includes(orderConfirmationIdentifier)) {
          return false;
        }
        orderConfirmationCollectionIdentifiers.push(orderConfirmationIdentifier);
        return true;
      });
      return [...orderConfirmationsToAdd, ...orderConfirmationCollection];
    }
    return orderConfirmationCollection;
  }

  protected convertDateFromClient(orderConfirmation: IOrderConfirmationFa): IOrderConfirmationFa {
    return Object.assign({}, orderConfirmation, {
      date: orderConfirmation.date?.isValid() ? orderConfirmation.date.format(DATE_FORMAT) : undefined,
      created: orderConfirmation.created?.isValid() ? orderConfirmation.created.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((orderConfirmation: IOrderConfirmationFa) => {
        orderConfirmation.date = orderConfirmation.date ? dayjs(orderConfirmation.date) : undefined;
        orderConfirmation.created = orderConfirmation.created ? dayjs(orderConfirmation.created) : undefined;
      });
    }
    return res;
  }
}
