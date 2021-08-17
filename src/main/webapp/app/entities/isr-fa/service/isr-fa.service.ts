import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIsrFa, getIsrFaIdentifier } from '../isr-fa.model';

export type EntityResponseType = HttpResponse<IIsrFa>;
export type EntityArrayResponseType = HttpResponse<IIsrFa[]>;

@Injectable({ providedIn: 'root' })
export class IsrFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/isrs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(isr: IIsrFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isr);
    return this.http
      .post<IIsrFa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(isr: IIsrFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isr);
    return this.http
      .put<IIsrFa>(`${this.resourceUrl}/${getIsrFaIdentifier(isr) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(isr: IIsrFa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(isr);
    return this.http
      .patch<IIsrFa>(`${this.resourceUrl}/${getIsrFaIdentifier(isr) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIsrFa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIsrFa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIsrFaToCollectionIfMissing(isrCollection: IIsrFa[], ...isrsToCheck: (IIsrFa | null | undefined)[]): IIsrFa[] {
    const isrs: IIsrFa[] = isrsToCheck.filter(isPresent);
    if (isrs.length > 0) {
      const isrCollectionIdentifiers = isrCollection.map(isrItem => getIsrFaIdentifier(isrItem)!);
      const isrsToAdd = isrs.filter(isrItem => {
        const isrIdentifier = getIsrFaIdentifier(isrItem);
        if (isrIdentifier == null || isrCollectionIdentifiers.includes(isrIdentifier)) {
          return false;
        }
        isrCollectionIdentifiers.push(isrIdentifier);
        return true;
      });
      return [...isrsToAdd, ...isrCollection];
    }
    return isrCollection;
  }

  protected convertDateFromClient(isr: IIsrFa): IIsrFa {
    return Object.assign({}, isr, {
      created: isr.created?.isValid() ? isr.created.toJSON() : undefined,
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
      res.body.forEach((isr: IIsrFa) => {
        isr.created = isr.created ? dayjs(isr.created) : undefined;
      });
    }
    return res;
  }
}
