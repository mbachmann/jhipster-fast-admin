import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentText, DocumentText } from '../document-text.model';
import { DocumentTextService } from '../service/document-text.service';

@Injectable({ providedIn: 'root' })
export class DocumentTextRoutingResolveService implements Resolve<IDocumentText> {
  constructor(protected service: DocumentTextService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentText> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentText: HttpResponse<DocumentText>) => {
          if (documentText.body) {
            return of(documentText.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentText());
  }
}
