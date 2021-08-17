jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILayoutFa, LayoutFa } from '../layout-fa.model';
import { LayoutFaService } from '../service/layout-fa.service';

import { LayoutFaRoutingResolveService } from './layout-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('LayoutFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LayoutFaRoutingResolveService;
    let service: LayoutFaService;
    let resultLayoutFa: ILayoutFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LayoutFaRoutingResolveService);
      service = TestBed.inject(LayoutFaService);
      resultLayoutFa = undefined;
    });

    describe('resolve', () => {
      it('should return ILayoutFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLayoutFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLayoutFa).toEqual({ id: 123 });
      });

      it('should return new ILayoutFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLayoutFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLayoutFa).toEqual(new LayoutFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LayoutFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLayoutFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLayoutFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
