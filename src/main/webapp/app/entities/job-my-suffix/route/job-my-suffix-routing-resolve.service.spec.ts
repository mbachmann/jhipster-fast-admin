jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IJobMySuffix, JobMySuffix } from '../job-my-suffix.model';
import { JobMySuffixService } from '../service/job-my-suffix.service';

import { JobMySuffixRoutingResolveService } from './job-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('JobMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: JobMySuffixRoutingResolveService;
    let service: JobMySuffixService;
    let resultJobMySuffix: IJobMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(JobMySuffixRoutingResolveService);
      service = TestBed.inject(JobMySuffixService);
      resultJobMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IJobMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJobMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultJobMySuffix).toEqual({ id: 123 });
      });

      it('should return new IJobMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJobMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultJobMySuffix).toEqual(new JobMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as JobMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultJobMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultJobMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
