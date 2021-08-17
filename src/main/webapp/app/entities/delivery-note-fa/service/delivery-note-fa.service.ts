import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliveryNoteFa, getDeliveryNoteFaIdentifier } from '../delivery-note-fa.model';

export type EntityResponseType = HttpResponse<IDeliveryNoteFa>;
export type EntityArrayResponseType = HttpResponse<IDeliveryNoteFa[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryNoteFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery-notes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deliveryNote: IDeliveryNoteFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryNote);
    return this.http
      .post<IDeliveryNoteFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(deliveryNote: IDeliveryNoteFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryNote);
    return this.http
      .put<IDeliveryNoteFa>(`${this.resourceUrl}/${getDeliveryNoteFaIdentifier(deliveryNote) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(deliveryNote: IDeliveryNoteFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryNote);
    return this.http
      .patch<IDeliveryNoteFa>(`${this.resourceUrl}/${getDeliveryNoteFaIdentifier(deliveryNote) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDeliveryNoteFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeliveryNoteFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeliveryNoteFaToCollectionIfMissing(
    deliveryNoteCollection: IDeliveryNoteFa[],
    ...deliveryNotesToCheck: (IDeliveryNoteFa | null | undefined)[]
  ): IDeliveryNoteFa[] {
    const deliveryNotes: IDeliveryNoteFa[] = deliveryNotesToCheck.filter(isPresent);
    if (deliveryNotes.length > 0) {
      const deliveryNoteCollectionIdentifiers = deliveryNoteCollection.map(
        deliveryNoteItem => getDeliveryNoteFaIdentifier(deliveryNoteItem)!
      );
      const deliveryNotesToAdd = deliveryNotes.filter(deliveryNoteItem => {
        const deliveryNoteIdentifier = getDeliveryNoteFaIdentifier(deliveryNoteItem);
        if (deliveryNoteIdentifier == null || deliveryNoteCollectionIdentifiers.includes(deliveryNoteIdentifier)) {
          return false;
        }
        deliveryNoteCollectionIdentifiers.push(deliveryNoteIdentifier);
        return true;
      });
      return [...deliveryNotesToAdd, ...deliveryNoteCollection];
    }
    return deliveryNoteCollection;
  }

  protected convertDateFromClient(deliveryNote: IDeliveryNoteFa): IDeliveryNoteFa {
    return Object.assign({}, deliveryNote, {
      date: deliveryNote.date?.isValid() ? deliveryNote.date.format(DATE_FORMAT) : undefined,
      created: deliveryNote.created?.isValid() ? deliveryNote.created.toJSON() : undefined,
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
      res.body.forEach((deliveryNote: IDeliveryNoteFa) => {
        deliveryNote.date = deliveryNote.date ? dayjs(deliveryNote.date) : undefined;
        deliveryNote.created = deliveryNote.created ? dayjs(deliveryNote.created) : undefined;
      });
    }
    return res;
  }
}
