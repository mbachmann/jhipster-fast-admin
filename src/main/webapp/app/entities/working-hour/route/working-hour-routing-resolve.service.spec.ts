jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWorkingHour, WorkingHour } from '../working-hour.model';
import { WorkingHourService } from '../service/working-hour.service';

import { WorkingHourRoutingResolveService } from './working-hour-routing-resolve.service';

describe('Service Tests', () => {
  describe('WorkingHour routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: WorkingHourRoutingResolveService;
    let service: WorkingHourService;
    let resultWorkingHour: IWorkingHour | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(WorkingHourRoutingResolveService);
      service = TestBed.inject(WorkingHourService);
      resultWorkingHour = undefined;
    });

    describe('resolve', () => {
      it('should return IWorkingHour returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkingHour = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorkingHour).toEqual({ id: 123 });
      });

      it('should return new IWorkingHour if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkingHour = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultWorkingHour).toEqual(new WorkingHour());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WorkingHour })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkingHour = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorkingHour).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
