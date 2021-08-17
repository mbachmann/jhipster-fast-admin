jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IValueAddedTax, ValueAddedTax } from '../value-added-tax.model';
import { ValueAddedTaxService } from '../service/value-added-tax.service';

import { ValueAddedTaxRoutingResolveService } from './value-added-tax-routing-resolve.service';

describe('Service Tests', () => {
  describe('ValueAddedTax routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ValueAddedTaxRoutingResolveService;
    let service: ValueAddedTaxService;
    let resultValueAddedTax: IValueAddedTax | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ValueAddedTaxRoutingResolveService);
      service = TestBed.inject(ValueAddedTaxService);
      resultValueAddedTax = undefined;
    });

    describe('resolve', () => {
      it('should return IValueAddedTax returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValueAddedTax = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultValueAddedTax).toEqual({ id: 123 });
      });

      it('should return new IValueAddedTax if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValueAddedTax = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultValueAddedTax).toEqual(new ValueAddedTax());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ValueAddedTax })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultValueAddedTax = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultValueAddedTax).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
