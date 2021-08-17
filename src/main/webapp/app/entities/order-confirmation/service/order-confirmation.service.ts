import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderConfirmation, getOrderConfirmationIdentifier } from '../order-confirmation.model';

export type EntityResponseType = HttpResponse<IOrderConfirmation>;
export type EntityArrayResponseType = HttpResponse<IOrderConfirmation[]>;

@Injectable({ providedIn: 'root' })
export class OrderConfirmationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-confirmations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderConfirmation: IOrderConfirmation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderConfirmation);
    return this.http
      .post<IOrderConfirmation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(orderConfirmation: IOrderConfirmation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderConfirmation);
    return this.http
      .put<IOrderConfirmation>(`${this.resourceUrl}/${getOrderConfirmationIdentifier(orderConfirmation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(orderConfirmation: IOrderConfirmation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderConfirmation);
    return this.http
      .patch<IOrderConfirmation>(`${this.resourceUrl}/${getOrderConfirmationIdentifier(orderConfirmation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrderConfirmation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrderConfirmation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrderConfirmationToCollectionIfMissing(
    orderConfirmationCollection: IOrderConfirmation[],
    ...orderConfirmationsToCheck: (IOrderConfirmation | null | undefined)[]
  ): IOrderConfirmation[] {
    const orderConfirmations: IOrderConfirmation[] = orderConfirmationsToCheck.filter(isPresent);
    if (orderConfirmations.length > 0) {
      const orderConfirmationCollectionIdentifiers = orderConfirmationCollection.map(
        orderConfirmationItem => getOrderConfirmationIdentifier(orderConfirmationItem)!
      );
      const orderConfirmationsToAdd = orderConfirmations.filter(orderConfirmationItem => {
        const orderConfirmationIdentifier = getOrderConfirmationIdentifier(orderConfirmationItem);
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

  protected convertDateFromClient(orderConfirmation: IOrderConfirmation): IOrderConfirmation {
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
      res.body.forEach((orderConfirmation: IOrderConfirmation) => {
        orderConfirmation.date = orderConfirmation.date ? dayjs(orderConfirmation.date) : undefined;
        orderConfirmation.created = orderConfirmation.created ? dayjs(orderConfirmation.created) : undefined;
      });
    }
    return res;
  }
}
