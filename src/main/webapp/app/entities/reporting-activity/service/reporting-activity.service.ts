import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportingActivity, getReportingActivityIdentifier } from '../reporting-activity.model';

export type EntityResponseType = HttpResponse<IReportingActivity>;
export type EntityArrayResponseType = HttpResponse<IReportingActivity[]>;

@Injectable({ providedIn: 'root' })
export class ReportingActivityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reporting-activities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reportingActivity: IReportingActivity): Observable<EntityResponseType> {
    return this.http.post<IReportingActivity>(this.resourceUrl, reportingActivity, { observe: 'response' });
  }

  update(reportingActivity: IReportingActivity): Observable<EntityResponseType> {
    return this.http.put<IReportingActivity>(
      `${this.resourceUrl}/${getReportingActivityIdentifier(reportingActivity) as number}`,
      reportingActivity,
      { observe: 'response' }
    );
  }

  partialUpdate(reportingActivity: IReportingActivity): Observable<EntityResponseType> {
    return this.http.patch<IReportingActivity>(
      `${this.resourceUrl}/${getReportingActivityIdentifier(reportingActivity) as number}`,
      reportingActivity,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReportingActivity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportingActivity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReportingActivityToCollectionIfMissing(
    reportingActivityCollection: IReportingActivity[],
    ...reportingActivitiesToCheck: (IReportingActivity | null | undefined)[]
  ): IReportingActivity[] {
    const reportingActivities: IReportingActivity[] = reportingActivitiesToCheck.filter(isPresent);
    if (reportingActivities.length > 0) {
      const reportingActivityCollectionIdentifiers = reportingActivityCollection.map(
        reportingActivityItem => getReportingActivityIdentifier(reportingActivityItem)!
      );
      const reportingActivitiesToAdd = reportingActivities.filter(reportingActivityItem => {
        const reportingActivityIdentifier = getReportingActivityIdentifier(reportingActivityItem);
        if (reportingActivityIdentifier == null || reportingActivityCollectionIdentifiers.includes(reportingActivityIdentifier)) {
          return false;
        }
        reportingActivityCollectionIdentifiers.push(reportingActivityIdentifier);
        return true;
      });
      return [...reportingActivitiesToAdd, ...reportingActivityCollection];
    }
    return reportingActivityCollection;
  }
}
