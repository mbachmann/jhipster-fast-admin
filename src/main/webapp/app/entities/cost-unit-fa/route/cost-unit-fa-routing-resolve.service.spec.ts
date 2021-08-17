jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICostUnitFa, CostUnitFa } from '../cost-unit-fa.model';
import { CostUnitFaService } from '../service/cost-unit-fa.service';

import { CostUnitFaRoutingResolveService } from './cost-unit-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('CostUnitFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CostUnitFaRoutingResolveService;
    let service: CostUnitFaService;
    let resultCostUnitFa: ICostUnitFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CostUnitFaRoutingResolveService);
      service = TestBed.inject(CostUnitFaService);
      resultCostUnitFa = undefined;
    });

    describe('resolve', () => {
      it('should return ICostUnitFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCostUnitFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCostUnitFa).toEqual({ id: 123 });
      });

      it('should return new ICostUnitFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCostUnitFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCostUnitFa).toEqual(new CostUnitFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CostUnitFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCostUnitFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCostUnitFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
