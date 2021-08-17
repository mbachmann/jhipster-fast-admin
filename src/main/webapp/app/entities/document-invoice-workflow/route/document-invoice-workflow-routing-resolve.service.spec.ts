jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDocumentInvoiceWorkflow, DocumentInvoiceWorkflow } from '../document-invoice-workflow.model';
import { DocumentInvoiceWorkflowService } from '../service/document-invoice-workflow.service';

import { DocumentInvoiceWorkflowRoutingResolveService } from './document-invoice-workflow-routing-resolve.service';

describe('Service Tests', () => {
  describe('DocumentInvoiceWorkflow routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DocumentInvoiceWorkflowRoutingResolveService;
    let service: DocumentInvoiceWorkflowService;
    let resultDocumentInvoiceWorkflow: IDocumentInvoiceWorkflow | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DocumentInvoiceWorkflowRoutingResolveService);
      service = TestBed.inject(DocumentInvoiceWorkflowService);
      resultDocumentInvoiceWorkflow = undefined;
    });

    describe('resolve', () => {
      it('should return IDocumentInvoiceWorkflow returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentInvoiceWorkflow = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentInvoiceWorkflow).toEqual({ id: 123 });
      });

      it('should return new IDocumentInvoiceWorkflow if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentInvoiceWorkflow = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDocumentInvoiceWorkflow).toEqual(new DocumentInvoiceWorkflow());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DocumentInvoiceWorkflow })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentInvoiceWorkflow = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentInvoiceWorkflow).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
