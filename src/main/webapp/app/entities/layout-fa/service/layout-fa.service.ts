import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILayoutFa, getLayoutFaIdentifier } from '../layout-fa.model';

export type EntityResponseType = HttpResponse<ILayoutFa>;
export type EntityArrayResponseType = HttpResponse<ILayoutFa[]>;

@Injectable({ providedIn: 'root' })
export class LayoutFaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/layouts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(layout: ILayoutFa): Observable<EntityResponseType> {
    return this.http.post<ILayoutFa>(this.resourceUrl, layout, { observe: 'response' });
  }

  update(layout: ILayoutFa): Observable<EntityResponseType> {
    return this.http.put<ILayoutFa>(`${this.resourceUrl}/${getLayoutFaIdentifier(layout) as number}`, layout, { observe: 'response' });
  }

  partialUpdate(layout: ILayoutFa): Observable<EntityResponseType> {
    return this.http.patch<ILayoutFa>(`${this.resourceUrl}/${getLayoutFaIdentifier(layout) as number}`, layout, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILayoutFa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILayoutFa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLayoutFaToCollectionIfMissing(layoutCollection: ILayoutFa[], ...layoutsToCheck: (ILayoutFa | null | undefined)[]): ILayoutFa[] {
    const layouts: ILayoutFa[] = layoutsToCheck.filter(isPresent);
    if (layouts.length > 0) {
      const layoutCollectionIdentifiers = layoutCollection.map(layoutItem => getLayoutFaIdentifier(layoutItem)!);
      const layoutsToAdd = layouts.filter(layoutItem => {
        const layoutIdentifier = getLayoutFaIdentifier(layoutItem);
        if (layoutIdentifier == null || layoutCollectionIdentifiers.includes(layoutIdentifier)) {
          return false;
        }
        layoutCollectionIdentifiers.push(layoutIdentifier);
        return true;
      });
      return [...layoutsToAdd, ...layoutCollection];
    }
    return layoutCollection;
  }
}
