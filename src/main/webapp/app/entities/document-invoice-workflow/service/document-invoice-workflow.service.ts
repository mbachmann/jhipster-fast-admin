import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentInvoiceWorkflow, getDocumentInvoiceWorkflowIdentifier } from '../document-invoice-workflow.model';

export type EntityResponseType = HttpResponse<IDocumentInvoiceWorkflow>;
export type EntityArrayResponseType = HttpResponse<IDocumentInvoiceWorkflow[]>;

@Injectable({ providedIn: 'root' })
export class DocumentInvoiceWorkflowService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-invoice-workflows');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentInvoiceWorkflow: IDocumentInvoiceWorkflow): Observable<EntityResponseType> {
    return this.http.post<IDocumentInvoiceWorkflow>(this.resourceUrl, documentInvoiceWorkflow, { observe: 'response' });
  }

  update(documentInvoiceWorkflow: IDocumentInvoiceWorkflow): Observable<EntityResponseType> {
    return this.http.put<IDocumentInvoiceWorkflow>(
      `${this.resourceUrl}/${getDocumentInvoiceWorkflowIdentifier(documentInvoiceWorkflow) as number}`,
      documentInvoiceWorkflow,
      { observe: 'response' }
    );
  }

  partialUpdate(documentInvoiceWorkflow: IDocumentInvoiceWorkflow): Observable<EntityResponseType> {
    return this.http.patch<IDocumentInvoiceWorkflow>(
      `${this.resourceUrl}/${getDocumentInvoiceWorkflowIdentifier(documentInvoiceWorkflow) as number}`,
      documentInvoiceWorkflow,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentInvoiceWorkflow>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentInvoiceWorkflow[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentInvoiceWorkflowToCollectionIfMissing(
    documentInvoiceWorkflowCollection: IDocumentInvoiceWorkflow[],
    ...documentInvoiceWorkflowsToCheck: (IDocumentInvoiceWorkflow | null | undefined)[]
  ): IDocumentInvoiceWorkflow[] {
    const documentInvoiceWorkflows: IDocumentInvoiceWorkflow[] = documentInvoiceWorkflowsToCheck.filter(isPresent);
    if (documentInvoiceWorkflows.length > 0) {
      const documentInvoiceWorkflowCollectionIdentifiers = documentInvoiceWorkflowCollection.map(
        documentInvoiceWorkflowItem => getDocumentInvoiceWorkflowIdentifier(documentInvoiceWorkflowItem)!
      );
      const documentInvoiceWorkflowsToAdd = documentInvoiceWorkflows.filter(documentInvoiceWorkflowItem => {
        const documentInvoiceWorkflowIdentifier = getDocumentInvoiceWorkflowIdentifier(documentInvoiceWorkflowItem);
        if (
          documentInvoiceWorkflowIdentifier == null ||
          documentInvoiceWorkflowCollectionIdentifiers.includes(documentInvoiceWorkflowIdentifier)
        ) {
          return false;
        }
        documentInvoiceWorkflowCollectionIdentifiers.push(documentInvoiceWorkflowIdentifier);
        return true;
      });
      return [...documentInvoiceWorkflowsToAdd, ...documentInvoiceWorkflowCollection];
    }
    return documentInvoiceWorkflowCollection;
  }
}
