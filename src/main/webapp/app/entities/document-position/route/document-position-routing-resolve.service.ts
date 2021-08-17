import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentPosition, DocumentPosition } from '../document-position.model';
import { DocumentPositionService } from '../service/document-position.service';

@Injectable({ providedIn: 'root' })
export class DocumentPositionRoutingResolveService implements Resolve<IDocumentPosition> {
  constructor(protected service: DocumentPositionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentPosition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentPosition: HttpResponse<DocumentPosition>) => {
          if (documentPosition.body) {
            return of(documentPosition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentPosition());
  }
}
