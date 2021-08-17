jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILayout, Layout } from '../layout.model';
import { LayoutService } from '../service/layout.service';

import { LayoutRoutingResolveService } from './layout-routing-resolve.service';

describe('Service Tests', () => {
  describe('Layout routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LayoutRoutingResolveService;
    let service: LayoutService;
    let resultLayout: ILayout | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LayoutRoutingResolveService);
      service = TestBed.inject(LayoutService);
      resultLayout = undefined;
    });

    describe('resolve', () => {
      it('should return ILayout returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLayout = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLayout).toEqual({ id: 123 });
      });

      it('should return new ILayout if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLayout = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLayout).toEqual(new Layout());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Layout })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLayout = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLayout).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
