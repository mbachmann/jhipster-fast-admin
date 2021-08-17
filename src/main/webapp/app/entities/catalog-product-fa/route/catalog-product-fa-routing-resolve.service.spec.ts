jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICatalogProductFa, CatalogProductFa } from '../catalog-product-fa.model';
import { CatalogProductFaService } from '../service/catalog-product-fa.service';

import { CatalogProductFaRoutingResolveService } from './catalog-product-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('CatalogProductFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CatalogProductFaRoutingResolveService;
    let service: CatalogProductFaService;
    let resultCatalogProductFa: ICatalogProductFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CatalogProductFaRoutingResolveService);
      service = TestBed.inject(CatalogProductFaService);
      resultCatalogProductFa = undefined;
    });

    describe('resolve', () => {
      it('should return ICatalogProductFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogProductFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogProductFa).toEqual({ id: 123 });
      });

      it('should return new ICatalogProductFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogProductFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCatalogProductFa).toEqual(new CatalogProductFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CatalogProductFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogProductFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogProductFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
