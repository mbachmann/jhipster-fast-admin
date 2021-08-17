jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReportingActivity, ReportingActivity } from '../reporting-activity.model';
import { ReportingActivityService } from '../service/reporting-activity.service';

import { ReportingActivityRoutingResolveService } from './reporting-activity-routing-resolve.service';

describe('Service Tests', () => {
  describe('ReportingActivity routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ReportingActivityRoutingResolveService;
    let service: ReportingActivityService;
    let resultReportingActivity: IReportingActivity | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ReportingActivityRoutingResolveService);
      service = TestBed.inject(ReportingActivityService);
      resultReportingActivity = undefined;
    });

    describe('resolve', () => {
      it('should return IReportingActivity returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReportingActivity = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReportingActivity).toEqual({ id: 123 });
      });

      it('should return new IReportingActivity if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReportingActivity = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultReportingActivity).toEqual(new ReportingActivity());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ReportingActivity })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReportingActivity = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReportingActivity).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
