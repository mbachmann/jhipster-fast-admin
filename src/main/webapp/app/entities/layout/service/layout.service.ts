import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILayout, getLayoutIdentifier } from '../layout.model';

export type EntityResponseType = HttpResponse<ILayout>;
export type EntityArrayResponseType = HttpResponse<ILayout[]>;

@Injectable({ providedIn: 'root' })
export class LayoutService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/layouts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(layout: ILayout): Observable<EntityResponseType> {
    return this.http.post<ILayout>(this.resourceUrl, layout, { observe: 'response' });
  }

  update(layout: ILayout): Observable<EntityResponseType> {
    return this.http.put<ILayout>(`${this.resourceUrl}/${getLayoutIdentifier(layout) as number}`, layout, { observe: 'response' });
  }

  partialUpdate(layout: ILayout): Observable<EntityResponseType> {
    return this.http.patch<ILayout>(`${this.resourceUrl}/${getLayoutIdentifier(layout) as number}`, layout, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILayout>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILayout[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLayoutToCollectionIfMissing(layoutCollection: ILayout[], ...layoutsToCheck: (ILayout | null | undefined)[]): ILayout[] {
    const layouts: ILayout[] = layoutsToCheck.filter(isPresent);
    if (layouts.length > 0) {
      const layoutCollectionIdentifiers = layoutCollection.map(layoutItem => getLayoutIdentifier(layoutItem)!);
      const layoutsToAdd = layouts.filter(layoutItem => {
        const layoutIdentifier = getLayoutIdentifier(layoutItem);
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
