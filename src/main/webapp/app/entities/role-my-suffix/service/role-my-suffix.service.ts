import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IRoleMySuffix, getRoleMySuffixIdentifier } from '../role-my-suffix.model';

export type EntityResponseType = HttpResponse<IRoleMySuffix>;
export type EntityArrayResponseType = HttpResponse<IRoleMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class RoleMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/roles');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/roles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(role: IRoleMySuffix): Observable<EntityResponseType> {
    return this.http.post<IRoleMySuffix>(this.resourceUrl, role, { observe: 'response' });
  }

  update(role: IRoleMySuffix): Observable<EntityResponseType> {
    return this.http.put<IRoleMySuffix>(`${this.resourceUrl}/${getRoleMySuffixIdentifier(role) as number}`, role, { observe: 'response' });
  }

  partialUpdate(role: IRoleMySuffix): Observable<EntityResponseType> {
    return this.http.patch<IRoleMySuffix>(`${this.resourceUrl}/${getRoleMySuffixIdentifier(role) as number}`, role, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoleMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoleMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoleMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addRoleMySuffixToCollectionIfMissing(
    roleCollection: IRoleMySuffix[],
    ...rolesToCheck: (IRoleMySuffix | null | undefined)[]
  ): IRoleMySuffix[] {
    const roles: IRoleMySuffix[] = rolesToCheck.filter(isPresent);
    if (roles.length > 0) {
      const roleCollectionIdentifiers = roleCollection.map(roleItem => getRoleMySuffixIdentifier(roleItem)!);
      const rolesToAdd = roles.filter(roleItem => {
        const roleIdentifier = getRoleMySuffixIdentifier(roleItem);
        if (roleIdentifier == null || roleCollectionIdentifiers.includes(roleIdentifier)) {
          return false;
        }
        roleCollectionIdentifiers.push(roleIdentifier);
        return true;
      });
      return [...rolesToAdd, ...roleCollection];
    }
    return roleCollection;
  }
}
