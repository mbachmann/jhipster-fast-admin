jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPermissionFa, PermissionFa } from '../permission-fa.model';
import { PermissionFaService } from '../service/permission-fa.service';

import { PermissionFaRoutingResolveService } from './permission-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('PermissionFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PermissionFaRoutingResolveService;
    let service: PermissionFaService;
    let resultPermissionFa: IPermissionFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PermissionFaRoutingResolveService);
      service = TestBed.inject(PermissionFaService);
      resultPermissionFa = undefined;
    });

    describe('resolve', () => {
      it('should return IPermissionFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermissionFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPermissionFa).toEqual({ id: 123 });
      });

      it('should return new IPermissionFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermissionFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPermissionFa).toEqual(new PermissionFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PermissionFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermissionFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPermissionFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
