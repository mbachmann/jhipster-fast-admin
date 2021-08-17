jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IApplicationRole, ApplicationRole } from '../application-role.model';
import { ApplicationRoleService } from '../service/application-role.service';

import { ApplicationRoleRoutingResolveService } from './application-role-routing-resolve.service';

describe('Service Tests', () => {
  describe('ApplicationRole routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ApplicationRoleRoutingResolveService;
    let service: ApplicationRoleService;
    let resultApplicationRole: IApplicationRole | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ApplicationRoleRoutingResolveService);
      service = TestBed.inject(ApplicationRoleService);
      resultApplicationRole = undefined;
    });

    describe('resolve', () => {
      it('should return IApplicationRole returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApplicationRole = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApplicationRole).toEqual({ id: 123 });
      });

      it('should return new IApplicationRole if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApplicationRole = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultApplicationRole).toEqual(new ApplicationRole());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ApplicationRole })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultApplicationRole = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultApplicationRole).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
