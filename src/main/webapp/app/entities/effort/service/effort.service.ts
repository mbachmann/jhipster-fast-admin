import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEffort, getEffortIdentifier } from '../effort.model';

export type EntityResponseType = HttpResponse<IEffort>;
export type EntityArrayResponseType = HttpResponse<IEffort[]>;

@Injectable({ providedIn: 'root' })
export class EffortService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/efforts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(effort: IEffort): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(effort);
    return this.http
      .post<IEffort>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(effort: IEffort): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(effort);
    return this.http
      .put<IEffort>(`${this.resourceUrl}/${getEffortIdentifier(effort) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(effort: IEffort): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(effort);
    return this.http
      .patch<IEffort>(`${this.resourceUrl}/${getEffortIdentifier(effort) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEffort>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEffort[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEffortToCollectionIfMissing(effortCollection: IEffort[], ...effortsToCheck: (IEffort | null | undefined)[]): IEffort[] {
    const efforts: IEffort[] = effortsToCheck.filter(isPresent);
    if (efforts.length > 0) {
      const effortCollectionIdentifiers = effortCollection.map(effortItem => getEffortIdentifier(effortItem)!);
      const effortsToAdd = efforts.filter(effortItem => {
        const effortIdentifier = getEffortIdentifier(effortItem);
        if (effortIdentifier == null || effortCollectionIdentifiers.includes(effortIdentifier)) {
          return false;
        }
        effortCollectionIdentifiers.push(effortIdentifier);
        return true;
      });
      return [...effortsToAdd, ...effortCollection];
    }
    return effortCollection;
  }

  protected convertDateFromClient(effort: IEffort): IEffort {
    return Object.assign({}, effort, {
      date: effort.date?.isValid() ? effort.date.format(DATE_FORMAT) : undefined,
      updated: effort.updated?.isValid() ? effort.updated.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.updated = res.body.updated ? dayjs(res.body.updated) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((effort: IEffort) => {
        effort.date = effort.date ? dayjs(effort.date) : undefined;
        effort.updated = effort.updated ? dayjs(effort.updated) : undefined;
      });
    }
    return res;
  }
}
