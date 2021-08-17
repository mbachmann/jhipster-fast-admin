import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActivityFa, getActivityFaIdentifier } from '../activity-fa.model';

export type EntityResponseType = HttpResponse<IActivityFa>;
export type EntityArrayResponseType = HttpResponse<IActivityFa[]>;

@Injectable({ providedIn: 'root' })
export class ActivityFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/activities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(activity: IActivityFa): Observable<EntityResponseType> {
    return this.http.post<IActivityFa>(this.resourceUrl, activity, { observe: 'response' });
  }

  update(activity: IActivityFa): Observable<EntityResponseType> {
    return this.http.put<IActivityFa>(`${this.resourceUrl}/${getActivityFaIdentifier(activity) as number}`, activity, {
      observe: 'response',
    });
  }

  partialUpdate(activity: IActivityFa): Observable<EntityResponseType> {
    return this.http.patch<IActivityFa>(`${this.resourceUrl}/${getActivityFaIdentifier(activity) as number}`, activity, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IActivityFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActivityFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActivityFaToCollectionIfMissing(
    activityCollection: IActivityFa[],
    ...activitiesToCheck: (IActivityFa | null | undefined)[]
  ): IActivityFa[] {
    const activities: IActivityFa[] = activitiesToCheck.filter(isPresent);
    if (activities.length > 0) {
      const activityCollectionIdentifiers = activityCollection.map(activityItem => getActivityFaIdentifier(activityItem)!);
      const activitiesToAdd = activities.filter(activityItem => {
        const activityIdentifier = getActivityFaIdentifier(activityItem);
        if (activityIdentifier == null || activityCollectionIdentifiers.includes(activityIdentifier)) {
          return false;
        }
        activityCollectionIdentifiers.push(activityIdentifier);
        return true;
      });
      return [...activitiesToAdd, ...activityCollection];
    }
    return activityCollection;
  }
}
