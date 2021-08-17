jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEffortFa, EffortFa } from '../effort-fa.model';
import { EffortFaService } from '../service/effort-fa.service';

import { EffortFaRoutingResolveService } from './effort-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('EffortFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EffortFaRoutingResolveService;
    let service: EffortFaService;
    let resultEffortFa: IEffortFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EffortFaRoutingResolveService);
      service = TestBed.inject(EffortFaService);
      resultEffortFa = undefined;
    });

    describe('resolve', () => {
      it('should return IEffortFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEffortFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEffortFa).toEqual({ id: 123 });
      });

      it('should return new IEffortFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEffortFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEffortFa).toEqual(new EffortFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EffortFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEffortFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEffortFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
