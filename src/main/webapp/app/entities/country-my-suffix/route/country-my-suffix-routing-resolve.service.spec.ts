jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountryMySuffix, CountryMySuffix } from '../country-my-suffix.model';
import { CountryMySuffixService } from '../service/country-my-suffix.service';

import { CountryMySuffixRoutingResolveService } from './country-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('CountryMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CountryMySuffixRoutingResolveService;
    let service: CountryMySuffixService;
    let resultCountryMySuffix: ICountryMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CountryMySuffixRoutingResolveService);
      service = TestBed.inject(CountryMySuffixService);
      resultCountryMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return ICountryMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCountryMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCountryMySuffix).toEqual({ id: 123 });
      });

      it('should return new ICountryMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCountryMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCountryMySuffix).toEqual(new CountryMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountryMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCountryMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCountryMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
