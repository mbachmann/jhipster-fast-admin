import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentInvoiceWorkflowFa, DocumentInvoiceWorkflowFa } from '../document-invoice-workflow-fa.model';
import { DocumentInvoiceWorkflowFaService } from '../service/document-invoice-workflow-fa.service';

@Injectable({ providedIn: 'root' })
export class DocumentInvoiceWorkflowFaRoutingResolveService implements Resolve<IDocumentInvoiceWorkflowFa> {
  constructor(protected service: DocumentInvoiceWorkflowFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentInvoiceWorkflowFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentInvoiceWorkflow: HttpResponse<DocumentInvoiceWorkflowFa>) => {
          if (documentInvoiceWorkflow.body) {
            return of(documentInvoiceWorkflow.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentInvoiceWorkflowFa());
  }
}
