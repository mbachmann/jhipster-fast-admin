import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentLetter, DocumentLetter } from '../document-letter.model';
import { DocumentLetterService } from '../service/document-letter.service';

@Injectable({ providedIn: 'root' })
export class DocumentLetterRoutingResolveService implements Resolve<IDocumentLetter> {
  constructor(protected service: DocumentLetterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentLetter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentLetter: HttpResponse<DocumentLetter>) => {
          if (documentLetter.body) {
            return of(documentLetter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentLetter());
  }
}
