jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICatalogProduct, CatalogProduct } from '../catalog-product.model';
import { CatalogProductService } from '../service/catalog-product.service';

import { CatalogProductRoutingResolveService } from './catalog-product-routing-resolve.service';

describe('Service Tests', () => {
  describe('CatalogProduct routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CatalogProductRoutingResolveService;
    let service: CatalogProductService;
    let resultCatalogProduct: ICatalogProduct | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CatalogProductRoutingResolveService);
      service = TestBed.inject(CatalogProductService);
      resultCatalogProduct = undefined;
    });

    describe('resolve', () => {
      it('should return ICatalogProduct returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogProduct = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogProduct).toEqual({ id: 123 });
      });

      it('should return new ICatalogProduct if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogProduct = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCatalogProduct).toEqual(new CatalogProduct());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CatalogProduct })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogProduct = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogProduct).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
