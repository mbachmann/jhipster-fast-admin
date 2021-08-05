import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IRoleFa, getRoleFaIdentifier } from '../role-fa.model';

export type EntityResponseType = HttpResponse<IRoleFa>;
export type EntityArrayResponseType = HttpResponse<IRoleFa[]>;

@Injectable({ providedIn: 'root' })
export class RoleFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/roles');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/roles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(role: IRoleFa): Observable<EntityResponseType> {
    return this.http.post<IRoleFa>(this.resourceUrl, role, { observe: 'response' });
  }

  update(role: IRoleFa): Observable<EntityResponseType> {
    return this.http.put<IRoleFa>(`${this.resourceUrl}/${getRoleFaIdentifier(role) as number}`, role, { observe: 'response' });
  }

  partialUpdate(role: IRoleFa): Observable<EntityResponseType> {
    return this.http.patch<IRoleFa>(`${this.resourceUrl}/${getRoleFaIdentifier(role) as number}`, role, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRoleFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoleFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRoleFa[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addRoleFaToCollectionIfMissing(roleCollection: IRoleFa[], ...rolesToCheck: (IRoleFa | null | undefined)[]): IRoleFa[] {
    const roles: IRoleFa[] = rolesToCheck.filter(isPresent);
    if (roles.length > 0) {
      const roleCollectionIdentifiers = roleCollection.map(roleItem => getRoleFaIdentifier(roleItem)!);
      const rolesToAdd = roles.filter(roleItem => {
        const roleIdentifier = getRoleFaIdentifier(roleItem);
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
