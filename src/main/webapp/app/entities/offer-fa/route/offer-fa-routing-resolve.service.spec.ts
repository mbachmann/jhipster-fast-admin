jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOfferFa, OfferFa } from '../offer-fa.model';
import { OfferFaService } from '../service/offer-fa.service';

import { OfferFaRoutingResolveService } from './offer-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('OfferFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OfferFaRoutingResolveService;
    let service: OfferFaService;
    let resultOfferFa: IOfferFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OfferFaRoutingResolveService);
      service = TestBed.inject(OfferFaService);
      resultOfferFa = undefined;
    });

    describe('resolve', () => {
      it('should return IOfferFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOfferFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOfferFa).toEqual({ id: 123 });
      });

      it('should return new IOfferFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOfferFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOfferFa).toEqual(new OfferFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OfferFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOfferFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOfferFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
