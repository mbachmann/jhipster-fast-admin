jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVatFa, VatFa } from '../vat-fa.model';
import { VatFaService } from '../service/vat-fa.service';

import { VatFaRoutingResolveService } from './vat-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('VatFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VatFaRoutingResolveService;
    let service: VatFaService;
    let resultVatFa: IVatFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VatFaRoutingResolveService);
      service = TestBed.inject(VatFaService);
      resultVatFa = undefined;
    });

    describe('resolve', () => {
      it('should return IVatFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVatFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVatFa).toEqual({ id: 123 });
      });

      it('should return new IVatFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVatFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVatFa).toEqual(new VatFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VatFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVatFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVatFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
