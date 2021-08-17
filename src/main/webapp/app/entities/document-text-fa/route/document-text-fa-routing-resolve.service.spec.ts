jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDocumentTextFa, DocumentTextFa } from '../document-text-fa.model';
import { DocumentTextFaService } from '../service/document-text-fa.service';

import { DocumentTextFaRoutingResolveService } from './document-text-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('DocumentTextFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DocumentTextFaRoutingResolveService;
    let service: DocumentTextFaService;
    let resultDocumentTextFa: IDocumentTextFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DocumentTextFaRoutingResolveService);
      service = TestBed.inject(DocumentTextFaService);
      resultDocumentTextFa = undefined;
    });

    describe('resolve', () => {
      it('should return IDocumentTextFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentTextFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentTextFa).toEqual({ id: 123 });
      });

      it('should return new IDocumentTextFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentTextFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDocumentTextFa).toEqual(new DocumentTextFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DocumentTextFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentTextFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentTextFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
