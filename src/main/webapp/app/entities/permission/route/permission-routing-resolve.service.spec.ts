jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPermission, Permission } from '../permission.model';
import { PermissionService } from '../service/permission.service';

import { PermissionRoutingResolveService } from './permission-routing-resolve.service';

describe('Service Tests', () => {
  describe('Permission routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PermissionRoutingResolveService;
    let service: PermissionService;
    let resultPermission: IPermission | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PermissionRoutingResolveService);
      service = TestBed.inject(PermissionService);
      resultPermission = undefined;
    });

    describe('resolve', () => {
      it('should return IPermission returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermission = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPermission).toEqual({ id: 123 });
      });

      it('should return new IPermission if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermission = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPermission).toEqual(new Permission());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Permission })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermission = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPermission).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
