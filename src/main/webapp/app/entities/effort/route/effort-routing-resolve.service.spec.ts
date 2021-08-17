jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEffort, Effort } from '../effort.model';
import { EffortService } from '../service/effort.service';

import { EffortRoutingResolveService } from './effort-routing-resolve.service';

describe('Service Tests', () => {
  describe('Effort routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EffortRoutingResolveService;
    let service: EffortService;
    let resultEffort: IEffort | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EffortRoutingResolveService);
      service = TestBed.inject(EffortService);
      resultEffort = undefined;
    });

    describe('resolve', () => {
      it('should return IEffort returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEffort = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEffort).toEqual({ id: 123 });
      });

      it('should return new IEffort if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEffort = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEffort).toEqual(new Effort());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Effort })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEffort = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEffort).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
