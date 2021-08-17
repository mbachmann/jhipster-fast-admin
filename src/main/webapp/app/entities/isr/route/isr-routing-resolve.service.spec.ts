jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIsr, Isr } from '../isr.model';
import { IsrService } from '../service/isr.service';

import { IsrRoutingResolveService } from './isr-routing-resolve.service';

describe('Service Tests', () => {
  describe('Isr routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IsrRoutingResolveService;
    let service: IsrService;
    let resultIsr: IIsr | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IsrRoutingResolveService);
      service = TestBed.inject(IsrService);
      resultIsr = undefined;
    });

    describe('resolve', () => {
      it('should return IIsr returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIsr = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIsr).toEqual({ id: 123 });
      });

      it('should return new IIsr if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIsr = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIsr).toEqual(new Isr());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Isr })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIsr = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIsr).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
