jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFreeTextFa, FreeTextFa } from '../free-text-fa.model';
import { FreeTextFaService } from '../service/free-text-fa.service';

import { FreeTextFaRoutingResolveService } from './free-text-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('FreeTextFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FreeTextFaRoutingResolveService;
    let service: FreeTextFaService;
    let resultFreeTextFa: IFreeTextFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FreeTextFaRoutingResolveService);
      service = TestBed.inject(FreeTextFaService);
      resultFreeTextFa = undefined;
    });

    describe('resolve', () => {
      it('should return IFreeTextFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFreeTextFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFreeTextFa).toEqual({ id: 123 });
      });

      it('should return new IFreeTextFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFreeTextFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFreeTextFa).toEqual(new FreeTextFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FreeTextFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFreeTextFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFreeTextFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
