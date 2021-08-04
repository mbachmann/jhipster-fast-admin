jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPermissionMySuffix, PermissionMySuffix } from '../permission-my-suffix.model';
import { PermissionMySuffixService } from '../service/permission-my-suffix.service';

import { PermissionMySuffixRoutingResolveService } from './permission-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('PermissionMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PermissionMySuffixRoutingResolveService;
    let service: PermissionMySuffixService;
    let resultPermissionMySuffix: IPermissionMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PermissionMySuffixRoutingResolveService);
      service = TestBed.inject(PermissionMySuffixService);
      resultPermissionMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IPermissionMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermissionMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPermissionMySuffix).toEqual({ id: 123 });
      });

      it('should return new IPermissionMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermissionMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPermissionMySuffix).toEqual(new PermissionMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PermissionMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPermissionMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPermissionMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
