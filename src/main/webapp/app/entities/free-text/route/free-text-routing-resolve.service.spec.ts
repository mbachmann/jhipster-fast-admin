jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFreeText, FreeText } from '../free-text.model';
import { FreeTextService } from '../service/free-text.service';

import { FreeTextRoutingResolveService } from './free-text-routing-resolve.service';

describe('Service Tests', () => {
  describe('FreeText routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FreeTextRoutingResolveService;
    let service: FreeTextService;
    let resultFreeText: IFreeText | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FreeTextRoutingResolveService);
      service = TestBed.inject(FreeTextService);
      resultFreeText = undefined;
    });

    describe('resolve', () => {
      it('should return IFreeText returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFreeText = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFreeText).toEqual({ id: 123 });
      });

      it('should return new IFreeText if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFreeText = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFreeText).toEqual(new FreeText());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FreeText })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFreeText = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFreeText).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
