jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDocumentLetterFa, DocumentLetterFa } from '../document-letter-fa.model';
import { DocumentLetterFaService } from '../service/document-letter-fa.service';

import { DocumentLetterFaRoutingResolveService } from './document-letter-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('DocumentLetterFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DocumentLetterFaRoutingResolveService;
    let service: DocumentLetterFaService;
    let resultDocumentLetterFa: IDocumentLetterFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DocumentLetterFaRoutingResolveService);
      service = TestBed.inject(DocumentLetterFaService);
      resultDocumentLetterFa = undefined;
    });

    describe('resolve', () => {
      it('should return IDocumentLetterFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentLetterFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentLetterFa).toEqual({ id: 123 });
      });

      it('should return new IDocumentLetterFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentLetterFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDocumentLetterFa).toEqual(new DocumentLetterFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DocumentLetterFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDocumentLetterFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDocumentLetterFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
