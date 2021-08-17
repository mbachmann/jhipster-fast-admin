jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICostUnit, CostUnit } from '../cost-unit.model';
import { CostUnitService } from '../service/cost-unit.service';

import { CostUnitRoutingResolveService } from './cost-unit-routing-resolve.service';

describe('Service Tests', () => {
  describe('CostUnit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CostUnitRoutingResolveService;
    let service: CostUnitService;
    let resultCostUnit: ICostUnit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CostUnitRoutingResolveService);
      service = TestBed.inject(CostUnitService);
      resultCostUnit = undefined;
    });

    describe('resolve', () => {
      it('should return ICostUnit returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCostUnit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCostUnit).toEqual({ id: 123 });
      });

      it('should return new ICostUnit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCostUnit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCostUnit).toEqual(new CostUnit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CostUnit })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCostUnit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCostUnit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
