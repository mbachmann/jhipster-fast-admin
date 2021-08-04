jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRegionMySuffix, RegionMySuffix } from '../region-my-suffix.model';
import { RegionMySuffixService } from '../service/region-my-suffix.service';

import { RegionMySuffixRoutingResolveService } from './region-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('RegionMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RegionMySuffixRoutingResolveService;
    let service: RegionMySuffixService;
    let resultRegionMySuffix: IRegionMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RegionMySuffixRoutingResolveService);
      service = TestBed.inject(RegionMySuffixService);
      resultRegionMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IRegionMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRegionMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRegionMySuffix).toEqual({ id: 123 });
      });

      it('should return new IRegionMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRegionMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRegionMySuffix).toEqual(new RegionMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RegionMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRegionMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRegionMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
