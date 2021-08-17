import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkingHour, getWorkingHourIdentifier } from '../working-hour.model';

export type EntityResponseType = HttpResponse<IWorkingHour>;
export type EntityArrayResponseType = HttpResponse<IWorkingHour[]>;

@Injectable({ providedIn: 'root' })
export class WorkingHourService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/working-hours');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workingHour: IWorkingHour): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHour);
    return this.http
      .post<IWorkingHour>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(workingHour: IWorkingHour): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHour);
    return this.http
      .put<IWorkingHour>(`${this.resourceUrl}/${getWorkingHourIdentifier(workingHour) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(workingHour: IWorkingHour): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHour);
    return this.http
      .patch<IWorkingHour>(`${this.resourceUrl}/${getWorkingHourIdentifier(workingHour) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorkingHour>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkingHour[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkingHourToCollectionIfMissing(
    workingHourCollection: IWorkingHour[],
    ...workingHoursToCheck: (IWorkingHour | null | undefined)[]
  ): IWorkingHour[] {
    const workingHours: IWorkingHour[] = workingHoursToCheck.filter(isPresent);
    if (workingHours.length > 0) {
      const workingHourCollectionIdentifiers = workingHourCollection.map(workingHourItem => getWorkingHourIdentifier(workingHourItem)!);
      const workingHoursToAdd = workingHours.filter(workingHourItem => {
        const workingHourIdentifier = getWorkingHourIdentifier(workingHourItem);
        if (workingHourIdentifier == null || workingHourCollectionIdentifiers.includes(workingHourIdentifier)) {
          return false;
        }
        workingHourCollectionIdentifiers.push(workingHourIdentifier);
        return true;
      });
      return [...workingHoursToAdd, ...workingHourCollection];
    }
    return workingHourCollection;
  }

  protected convertDateFromClient(workingHour: IWorkingHour): IWorkingHour {
    return Object.assign({}, workingHour, {
      date: workingHour.date?.isValid() ? workingHour.date.format(DATE_FORMAT) : undefined,
      timeStart: workingHour.timeStart?.isValid() ? workingHour.timeStart.toJSON() : undefined,
      timeEnd: workingHour.timeEnd?.isValid() ? workingHour.timeEnd.toJSON() : undefined,
      created: workingHour.created?.isValid() ? workingHour.created.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.timeStart = res.body.timeStart ? dayjs(res.body.timeStart) : undefined;
      res.body.timeEnd = res.body.timeEnd ? dayjs(res.body.timeEnd) : undefined;
      res.body.created = res.body.created ? dayjs(res.body.created) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workingHour: IWorkingHour) => {
        workingHour.date = workingHour.date ? dayjs(workingHour.date) : undefined;
        workingHour.timeStart = workingHour.timeStart ? dayjs(workingHour.timeStart) : undefined;
        workingHour.timeEnd = workingHour.timeEnd ? dayjs(workingHour.timeEnd) : undefined;
        workingHour.created = workingHour.created ? dayjs(workingHour.created) : undefined;
      });
    }
    return res;
  }
}
