jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDeliveryNoteFa, DeliveryNoteFa } from '../delivery-note-fa.model';
import { DeliveryNoteFaService } from '../service/delivery-note-fa.service';

import { DeliveryNoteFaRoutingResolveService } from './delivery-note-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('DeliveryNoteFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DeliveryNoteFaRoutingResolveService;
    let service: DeliveryNoteFaService;
    let resultDeliveryNoteFa: IDeliveryNoteFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DeliveryNoteFaRoutingResolveService);
      service = TestBed.inject(DeliveryNoteFaService);
      resultDeliveryNoteFa = undefined;
    });

    describe('resolve', () => {
      it('should return IDeliveryNoteFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeliveryNoteFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDeliveryNoteFa).toEqual({ id: 123 });
      });

      it('should return new IDeliveryNoteFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeliveryNoteFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDeliveryNoteFa).toEqual(new DeliveryNoteFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DeliveryNoteFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeliveryNoteFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDeliveryNoteFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
