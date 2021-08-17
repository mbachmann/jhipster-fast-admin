jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExchangeRateFa, ExchangeRateFa } from '../exchange-rate-fa.model';
import { ExchangeRateFaService } from '../service/exchange-rate-fa.service';

import { ExchangeRateFaRoutingResolveService } from './exchange-rate-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ExchangeRateFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExchangeRateFaRoutingResolveService;
    let service: ExchangeRateFaService;
    let resultExchangeRateFa: IExchangeRateFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExchangeRateFaRoutingResolveService);
      service = TestBed.inject(ExchangeRateFaService);
      resultExchangeRateFa = undefined;
    });

    describe('resolve', () => {
      it('should return IExchangeRateFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExchangeRateFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExchangeRateFa).toEqual({ id: 123 });
      });

      it('should return new IExchangeRateFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExchangeRateFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExchangeRateFa).toEqual(new ExchangeRateFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ExchangeRateFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExchangeRateFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExchangeRateFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
