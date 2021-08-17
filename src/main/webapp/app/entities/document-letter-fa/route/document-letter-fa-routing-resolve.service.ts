import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentLetterFa, DocumentLetterFa } from '../document-letter-fa.model';
import { DocumentLetterFaService } from '../service/document-letter-fa.service';

@Injectable({ providedIn: 'root' })
export class DocumentLetterFaRoutingResolveService implements Resolve<IDocumentLetterFa> {
  constructor(protected service: DocumentLetterFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentLetterFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentLetter: HttpResponse<DocumentLetterFa>) => {
          if (documentLetter.body) {
            return of(documentLetter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentLetterFa());
  }
}
