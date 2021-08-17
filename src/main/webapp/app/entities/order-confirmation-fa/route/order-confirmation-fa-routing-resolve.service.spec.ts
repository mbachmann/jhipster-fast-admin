jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrderConfirmationFa, OrderConfirmationFa } from '../order-confirmation-fa.model';
import { OrderConfirmationFaService } from '../service/order-confirmation-fa.service';

import { OrderConfirmationFaRoutingResolveService } from './order-confirmation-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrderConfirmationFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrderConfirmationFaRoutingResolveService;
    let service: OrderConfirmationFaService;
    let resultOrderConfirmationFa: IOrderConfirmationFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrderConfirmationFaRoutingResolveService);
      service = TestBed.inject(OrderConfirmationFaService);
      resultOrderConfirmationFa = undefined;
    });

    describe('resolve', () => {
      it('should return IOrderConfirmationFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderConfirmationFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrderConfirmationFa).toEqual({ id: 123 });
      });

      it('should return new IOrderConfirmationFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderConfirmationFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrderConfirmationFa).toEqual(new OrderConfirmationFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrderConfirmationFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderConfirmationFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrderConfirmationFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
