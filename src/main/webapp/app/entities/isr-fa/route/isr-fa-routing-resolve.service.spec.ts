jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIsrFa, IsrFa } from '../isr-fa.model';
import { IsrFaService } from '../service/isr-fa.service';

import { IsrFaRoutingResolveService } from './isr-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('IsrFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IsrFaRoutingResolveService;
    let service: IsrFaService;
    let resultIsrFa: IIsrFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IsrFaRoutingResolveService);
      service = TestBed.inject(IsrFaService);
      resultIsrFa = undefined;
    });

    describe('resolve', () => {
      it('should return IIsrFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIsrFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIsrFa).toEqual({ id: 123 });
      });

      it('should return new IIsrFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIsrFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIsrFa).toEqual(new IsrFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as IsrFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIsrFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultIsrFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
