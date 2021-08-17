import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOfferFa, getOfferFaIdentifier } from '../offer-fa.model';

export type EntityResponseType = HttpResponse<IOfferFa>;
export type EntityArrayResponseType = HttpResponse<IOfferFa[]>;

@Injectable({ providedIn: 'root' })
export class OfferFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/offers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(offer: IOfferFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .post<IOfferFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(offer: IOfferFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .put<IOfferFa>(`${this.resourceUrl}/${getOfferFaIdentifier(offer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(offer: IOfferFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(offer);
    return this.http
      .patch<IOfferFa>(`${this.resourceUrl}/${getOfferFaIdentifier(offer) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOfferFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOfferFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOfferFaToCollectionIfMissing(offerCollection: IOfferFa[], ...offersToCheck: (IOfferFa | null | undefined)[]): IOfferFa[] {
    const offers: IOfferFa[] = offersToCheck.filter(isPresent);
    if (offers.length > 0) {
      const offerCollectionIdentifiers = offerCollection.map(offerItem => getOfferFaIdentifier(offerItem)!);
      const offersToAdd = offers.filter(offerItem => {
        const offerIdentifier = getOfferFaIdentifier(offerItem);
        if (offerIdentifier == null || offerCollectionIdentifiers.includes(offerIdentifier)) {
          return false;
        }
        offerCollectionIdentifiers.push(offerIdentifier);
        return true;
      });
      return [...offersToAdd, ...offerCollection];
    }
    return offerCollection;
  }

  protected convertDateFromClient(offer: IOfferFa): IOfferFa {
    return Object.assign({}, offer, {
      date: offer.date?.isValid() ? offer.date.format(DATE_FORMAT) : undefined,
      validUntil: offer.validUntil?.isValid() ? offer.validUntil.format(DATE_FORMAT) : undefined,
      created: offer.created?.isValid() ? offer.created.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.validUntil = res.body.validUntil ? dayjs(res.body.validUntil) : undefined;
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((offer: IOfferFa) => {
        offer.date = offer.date ? dayjs(offer.date) : undefined;
        offer.validUntil = offer.validUntil ? dayjs(offer.validUntil) : undefined;
        offer.created = offer.created ? dayjs(offer.created) : undefined;
      });
    }
    return res;
  }
}
