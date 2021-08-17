jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IActivityFa, ActivityFa } from '../activity-fa.model';
import { ActivityFaService } from '../service/activity-fa.service';

import { ActivityFaRoutingResolveService } from './activity-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ActivityFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ActivityFaRoutingResolveService;
    let service: ActivityFaService;
    let resultActivityFa: IActivityFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ActivityFaRoutingResolveService);
      service = TestBed.inject(ActivityFaService);
      resultActivityFa = undefined;
    });

    describe('resolve', () => {
      it('should return IActivityFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultActivityFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultActivityFa).toEqual({ id: 123 });
      });

      it('should return new IActivityFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultActivityFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultActivityFa).toEqual(new ActivityFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ActivityFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultActivityFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultActivityFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
