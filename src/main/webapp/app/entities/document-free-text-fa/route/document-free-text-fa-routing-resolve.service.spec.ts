jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDocumentFreeTextFa, DocumentFreeTextFa } from '../document-free-text-fa.model';
import { DocumentFreeTextFaService } from '../service/document-free-text-fa.service';

import { DocumentFreeTextFaRoutingResolveService } from './document-free-text-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('DocumentFreeTextFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DocumentFreeTextFaRoutingResolveService;
    let service: DocumentFreeTextFaService;
    let resultDocumentFreeTextFa: IDocumentFreeTextFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DocumentFreeTextFaRoutingResolveService);
      service = TestBed.inject(DocumentFreeTextFaService);
      resultDocumentFreeTextFa = undefined;
    });

    describe('resolve', () => {
      it('should return IDocumentFreeTextFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentFreeTextFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentFreeTextFa).toEqual({ id: 123 });
      });

      it('should return new IDocumentFreeTextFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentFreeTextFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDocumentFreeTextFa).toEqual(new DocumentFreeTextFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DocumentFreeTextFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentFreeTextFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentFreeTextFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
