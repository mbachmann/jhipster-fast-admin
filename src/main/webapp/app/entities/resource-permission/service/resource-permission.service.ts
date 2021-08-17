import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResourcePermission, getResourcePermissionIdentifier } from '../resource-permission.model';

export type EntityResponseType = HttpResponse<IResourcePermission>;
export type EntityArrayResponseType = HttpResponse<IResourcePermission[]>;

@Injectable({ providedIn: 'root' })
export class ResourcePermissionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resource-permissions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resourcePermission: IResourcePermission): Observable<EntityResponseType> {
    return this.http.post<IResourcePermission>(this.resourceUrl, resourcePermission, { observe: 'response' });
  }

  update(resourcePermission: IResourcePermission): Observable<EntityResponseType> {
    return this.http.put<IResourcePermission>(
      `${this.resourceUrl}/${getResourcePermissionIdentifier(resourcePermission) as number}`,
      resourcePermission,
      { observe: 'response' }
    );
  }

  partialUpdate(resourcePermission: IResourcePermission): Observable<EntityResponseType> {
    return this.http.patch<IResourcePermission>(
      `${this.resourceUrl}/${getResourcePermissionIdentifier(resourcePermission) as number}`,
      resourcePermission,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResourcePermission>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResourcePermission[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addResourcePermissionToCollectionIfMissing(
    resourcePermissionCollection: IResourcePermission[],
    ...resourcePermissionsToCheck: (IResourcePermission | null | undefined)[]
  ): IResourcePermission[] {
    const resourcePermissions: IResourcePermission[] = resourcePermissionsToCheck.filter(isPresent);
    if (resourcePermissions.length > 0) {
      const resourcePermissionCollectionIdentifiers = resourcePermissionCollection.map(
        resourcePermissionItem => getResourcePermissionIdentifier(resourcePermissionItem)!
      );
      const resourcePermissionsToAdd = resourcePermissions.filter(resourcePermissionItem => {
        const resourcePermissionIdentifier = getResourcePermissionIdentifier(resourcePermissionItem);
        if (resourcePermissionIdentifier == null || resourcePermissionCollectionIdentifiers.includes(resourcePermissionIdentifier)) {
          return false;
        }
        resourcePermissionCollectionIdentifiers.push(resourcePermissionIdentifier);
        return true;
      });
      return [...resourcePermissionsToAdd, ...resourcePermissionCollection];
    }
    return resourcePermissionCollection;
  }
}
