import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentPositionFa, DocumentPositionFa } from '../document-position-fa.model';
import { DocumentPositionFaService } from '../service/document-position-fa.service';

@Injectable({ providedIn: 'root' })
export class DocumentPositionFaRoutingResolveService implements Resolve<IDocumentPositionFa> {
  constructor(protected service: DocumentPositionFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentPositionFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentPosition: HttpResponse<DocumentPositionFa>) => {
          if (documentPosition.body) {
            return of(documentPosition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentPositionFa());
  }
}
