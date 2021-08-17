jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInvoiceFa, InvoiceFa } from '../invoice-fa.model';
import { InvoiceFaService } from '../service/invoice-fa.service';

import { InvoiceFaRoutingResolveService } from './invoice-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('InvoiceFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InvoiceFaRoutingResolveService;
    let service: InvoiceFaService;
    let resultInvoiceFa: IInvoiceFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InvoiceFaRoutingResolveService);
      service = TestBed.inject(InvoiceFaService);
      resultInvoiceFa = undefined;
    });

    describe('resolve', () => {
      it('should return IInvoiceFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoiceFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInvoiceFa).toEqual({ id: 123 });
      });

      it('should return new IInvoiceFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoiceFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInvoiceFa).toEqual(new InvoiceFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InvoiceFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoiceFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInvoiceFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
