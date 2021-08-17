jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICatalogCategoryFa, CatalogCategoryFa } from '../catalog-category-fa.model';
import { CatalogCategoryFaService } from '../service/catalog-category-fa.service';

import { CatalogCategoryFaRoutingResolveService } from './catalog-category-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('CatalogCategoryFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CatalogCategoryFaRoutingResolveService;
    let service: CatalogCategoryFaService;
    let resultCatalogCategoryFa: ICatalogCategoryFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CatalogCategoryFaRoutingResolveService);
      service = TestBed.inject(CatalogCategoryFaService);
      resultCatalogCategoryFa = undefined;
    });

    describe('resolve', () => {
      it('should return ICatalogCategoryFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogCategoryFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogCategoryFa).toEqual({ id: 123 });
      });

      it('should return new ICatalogCategoryFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogCategoryFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCatalogCategoryFa).toEqual(new CatalogCategoryFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CatalogCategoryFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogCategoryFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogCategoryFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
