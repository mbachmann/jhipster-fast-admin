jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOwnerFa, OwnerFa } from '../owner-fa.model';
import { OwnerFaService } from '../service/owner-fa.service';

import { OwnerFaRoutingResolveService } from './owner-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('OwnerFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OwnerFaRoutingResolveService;
    let service: OwnerFaService;
    let resultOwnerFa: IOwnerFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OwnerFaRoutingResolveService);
      service = TestBed.inject(OwnerFaService);
      resultOwnerFa = undefined;
    });

    describe('resolve', () => {
      it('should return IOwnerFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOwnerFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOwnerFa).toEqual({ id: 123 });
      });

      it('should return new IOwnerFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOwnerFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOwnerFa).toEqual(new OwnerFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OwnerFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOwnerFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOwnerFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
