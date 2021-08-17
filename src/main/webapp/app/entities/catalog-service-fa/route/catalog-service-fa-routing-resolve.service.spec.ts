jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICatalogServiceFa, CatalogServiceFa } from '../catalog-service-fa.model';
import { CatalogServiceFaService } from '../service/catalog-service-fa.service';

import { CatalogServiceFaRoutingResolveService } from './catalog-service-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('CatalogServiceFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CatalogServiceFaRoutingResolveService;
    let service: CatalogServiceFaService;
    let resultCatalogServiceFa: ICatalogServiceFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CatalogServiceFaRoutingResolveService);
      service = TestBed.inject(CatalogServiceFaService);
      resultCatalogServiceFa = undefined;
    });

    describe('resolve', () => {
      it('should return ICatalogServiceFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogServiceFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogServiceFa).toEqual({ id: 123 });
      });

      it('should return new ICatalogServiceFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogServiceFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCatalogServiceFa).toEqual(new CatalogServiceFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CatalogServiceFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogServiceFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogServiceFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
