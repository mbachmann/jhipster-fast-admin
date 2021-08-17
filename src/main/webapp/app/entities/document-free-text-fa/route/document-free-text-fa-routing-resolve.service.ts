import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentFreeTextFa, DocumentFreeTextFa } from '../document-free-text-fa.model';
import { DocumentFreeTextFaService } from '../service/document-free-text-fa.service';

@Injectable({ providedIn: 'root' })
export class DocumentFreeTextFaRoutingResolveService implements Resolve<IDocumentFreeTextFa> {
  constructor(protected service: DocumentFreeTextFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentFreeTextFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentFreeText: HttpResponse<DocumentFreeTextFa>) => {
          if (documentFreeText.body) {
            return of(documentFreeText.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentFreeTextFa());
  }
}
