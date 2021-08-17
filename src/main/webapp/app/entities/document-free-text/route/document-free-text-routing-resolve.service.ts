import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentFreeText, DocumentFreeText } from '../document-free-text.model';
import { DocumentFreeTextService } from '../service/document-free-text.service';

@Injectable({ providedIn: 'root' })
export class DocumentFreeTextRoutingResolveService implements Resolve<IDocumentFreeText> {
  constructor(protected service: DocumentFreeTextService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentFreeText> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentFreeText: HttpResponse<DocumentFreeText>) => {
          if (documentFreeText.body) {
            return of(documentFreeText.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentFreeText());
  }
}
