jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDocumentPositionFa, DocumentPositionFa } from '../document-position-fa.model';
import { DocumentPositionFaService } from '../service/document-position-fa.service';

import { DocumentPositionFaRoutingResolveService } from './document-position-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('DocumentPositionFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DocumentPositionFaRoutingResolveService;
    let service: DocumentPositionFaService;
    let resultDocumentPositionFa: IDocumentPositionFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DocumentPositionFaRoutingResolveService);
      service = TestBed.inject(DocumentPositionFaService);
      resultDocumentPositionFa = undefined;
    });

    describe('resolve', () => {
      it('should return IDocumentPositionFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentPositionFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentPositionFa).toEqual({ id: 123 });
      });

      it('should return new IDocumentPositionFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentPositionFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDocumentPositionFa).toEqual(new DocumentPositionFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DocumentPositionFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentPositionFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentPositionFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
