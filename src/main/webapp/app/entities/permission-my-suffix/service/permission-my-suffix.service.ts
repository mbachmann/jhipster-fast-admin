import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IPermissionMySuffix, getPermissionMySuffixIdentifier } from '../permission-my-suffix.model';

export type EntityResponseType = HttpResponse<IPermissionMySuffix>;
export type EntityArrayResponseType = HttpResponse<IPermissionMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class PermissionMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permissions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/permissions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(permission: IPermissionMySuffix): Observable<EntityResponseType> {
    return this.http.post<IPermissionMySuffix>(this.resourceUrl, permission, { observe: 'response' });
  }

  update(permission: IPermissionMySuffix): Observable<EntityResponseType> {
    return this.http.put<IPermissionMySuffix>(`${this.resourceUrl}/${getPermissionMySuffixIdentifier(permission) as number}`, permission, {
      observe: 'response',
    });
  }

  partialUpdate(permission: IPermissionMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IPermissionMySuffix>(
      `${this.resourceUrl}/${getPermissionMySuffixIdentifier(permission) as number}`,
      permission,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPermissionMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermissionMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermissionMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPermissionMySuffixToCollectionIfMissing(
    permissionCollection: IPermissionMySuffix[],
    ...permissionsToCheck: (IPermissionMySuffix | null | undefined)[]
  ): IPermissionMySuffix[] {
    const permissions: IPermissionMySuffix[] = permissionsToCheck.filter(isPresent);
    if (permissions.length > 0) {
      const permissionCollectionIdentifiers = permissionCollection.map(permissionItem => getPermissionMySuffixIdentifier(permissionItem)!);
      const permissionsToAdd = permissions.filter(permissionItem => {
        const permissionIdentifier = getPermissionMySuffixIdentifier(permissionItem);
        if (permissionIdentifier == null || permissionCollectionIdentifiers.includes(permissionIdentifier)) {
          return false;
        }
        permissionCollectionIdentifiers.push(permissionIdentifier);
        return true;
      });
      return [...permissionsToAdd, ...permissionCollection];
    }
    return permissionCollection;
  }
}
