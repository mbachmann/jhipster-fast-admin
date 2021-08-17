jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrderConfirmation, OrderConfirmation } from '../order-confirmation.model';
import { OrderConfirmationService } from '../service/order-confirmation.service';

import { OrderConfirmationRoutingResolveService } from './order-confirmation-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrderConfirmation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrderConfirmationRoutingResolveService;
    let service: OrderConfirmationService;
    let resultOrderConfirmation: IOrderConfirmation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrderConfirmationRoutingResolveService);
      service = TestBed.inject(OrderConfirmationService);
      resultOrderConfirmation = undefined;
    });

    describe('resolve', () => {
      it('should return IOrderConfirmation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderConfirmation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrderConfirmation).toEqual({ id: 123 });
      });

      it('should return new IOrderConfirmation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderConfirmation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrderConfirmation).toEqual(new OrderConfirmation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OrderConfirmation })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderConfirmation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrderConfirmation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
