import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IPermissionFa, getPermissionFaIdentifier } from '../permission-fa.model';

export type EntityResponseType = HttpResponse<IPermissionFa>;
export type EntityArrayResponseType = HttpResponse<IPermissionFa[]>;

@Injectable({ providedIn: 'root' })
export class PermissionFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permissions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/permissions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(permission: IPermissionFa): Observable<EntityResponseType> {
    return this.http.post<IPermissionFa>(this.resourceUrl, permission, { observe: 'response' });
  }

  update(permission: IPermissionFa): Observable<EntityResponseType> {
    return this.http.put<IPermissionFa>(`${this.resourceUrl}/${getPermissionFaIdentifier(permission) as number}`, permission, {
      observe: 'response',
    });
  }

  partialUpdate(permission: IPermissionFa): Observable<EntityResponseType> {
    return this.http.patch<IPermissionFa>(`${this.resourceUrl}/${getPermissionFaIdentifier(permission) as number}`, permission, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPermissionFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermissionFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermissionFa[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPermissionFaToCollectionIfMissing(
    permissionCollection: IPermissionFa[],
    ...permissionsToCheck: (IPermissionFa | null | undefined)[]
  ): IPermissionFa[] {
    const permissions: IPermissionFa[] = permissionsToCheck.filter(isPresent);
    if (permissions.length > 0) {
      const permissionCollectionIdentifiers = permissionCollection.map(permissionItem => getPermissionFaIdentifier(permissionItem)!);
      const permissionsToAdd = permissions.filter(permissionItem => {
        const permissionIdentifier = getPermissionFaIdentifier(permissionItem);
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
