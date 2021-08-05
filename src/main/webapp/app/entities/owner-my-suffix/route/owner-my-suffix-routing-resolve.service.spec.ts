jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOwnerMySuffix, OwnerMySuffix } from '../owner-my-suffix.model';
import { OwnerMySuffixService } from '../service/owner-my-suffix.service';

import { OwnerMySuffixRoutingResolveService } from './owner-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('OwnerMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OwnerMySuffixRoutingResolveService;
    let service: OwnerMySuffixService;
    let resultOwnerMySuffix: IOwnerMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OwnerMySuffixRoutingResolveService);
      service = TestBed.inject(OwnerMySuffixService);
      resultOwnerMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IOwnerMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOwnerMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOwnerMySuffix).toEqual({ id: 123 });
      });

      it('should return new IOwnerMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOwnerMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOwnerMySuffix).toEqual(new OwnerMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OwnerMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOwnerMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOwnerMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
