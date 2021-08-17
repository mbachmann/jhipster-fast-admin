import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApplicationRole, getApplicationRoleIdentifier } from '../application-role.model';

export type EntityResponseType = HttpResponse<IApplicationRole>;
export type EntityArrayResponseType = HttpResponse<IApplicationRole[]>;

@Injectable({ providedIn: 'root' })
export class ApplicationRoleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/application-roles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(applicationRole: IApplicationRole): Observable<EntityResponseType> {
    return this.http.post<IApplicationRole>(this.resourceUrl, applicationRole, { observe: 'response' });
  }

  update(applicationRole: IApplicationRole): Observable<EntityResponseType> {
    return this.http.put<IApplicationRole>(
      `${this.resourceUrl}/${getApplicationRoleIdentifier(applicationRole) as number}`,
      applicationRole,
      { observe: 'response' }
    );
  }

  partialUpdate(applicationRole: IApplicationRole): Observable<EntityResponseType> {
    return this.http.patch<IApplicationRole>(
      `${this.resourceUrl}/${getApplicationRoleIdentifier(applicationRole) as number}`,
      applicationRole,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApplicationRole>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApplicationRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApplicationRoleToCollectionIfMissing(
    applicationRoleCollection: IApplicationRole[],
    ...applicationRolesToCheck: (IApplicationRole | null | undefined)[]
  ): IApplicationRole[] {
    const applicationRoles: IApplicationRole[] = applicationRolesToCheck.filter(isPresent);
    if (applicationRoles.length > 0) {
      const applicationRoleCollectionIdentifiers = applicationRoleCollection.map(
        applicationRoleItem => getApplicationRoleIdentifier(applicationRoleItem)!
      );
      const applicationRolesToAdd = applicationRoles.filter(applicationRoleItem => {
        const applicationRoleIdentifier = getApplicationRoleIdentifier(applicationRoleItem);
        if (applicationRoleIdentifier == null || applicationRoleCollectionIdentifiers.includes(applicationRoleIdentifier)) {
          return false;
        }
        applicationRoleCollectionIdentifiers.push(applicationRoleIdentifier);
        return true;
      });
      return [...applicationRolesToAdd, ...applicationRoleCollection];
    }
    return applicationRoleCollection;
  }
}
