jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITaskMySuffix, TaskMySuffix } from '../task-my-suffix.model';
import { TaskMySuffixService } from '../service/task-my-suffix.service';

import { TaskMySuffixRoutingResolveService } from './task-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('TaskMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TaskMySuffixRoutingResolveService;
    let service: TaskMySuffixService;
    let resultTaskMySuffix: ITaskMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TaskMySuffixRoutingResolveService);
      service = TestBed.inject(TaskMySuffixService);
      resultTaskMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return ITaskMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaskMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTaskMySuffix).toEqual({ id: 123 });
      });

      it('should return new ITaskMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaskMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTaskMySuffix).toEqual(new TaskMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TaskMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTaskMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTaskMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
