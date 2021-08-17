jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICatalogUnitFa, CatalogUnitFa } from '../catalog-unit-fa.model';
import { CatalogUnitFaService } from '../service/catalog-unit-fa.service';

import { CatalogUnitFaRoutingResolveService } from './catalog-unit-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('CatalogUnitFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CatalogUnitFaRoutingResolveService;
    let service: CatalogUnitFaService;
    let resultCatalogUnitFa: ICatalogUnitFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CatalogUnitFaRoutingResolveService);
      service = TestBed.inject(CatalogUnitFaService);
      resultCatalogUnitFa = undefined;
    });

    describe('resolve', () => {
      it('should return ICatalogUnitFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogUnitFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogUnitFa).toEqual({ id: 123 });
      });

      it('should return new ICatalogUnitFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogUnitFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCatalogUnitFa).toEqual(new CatalogUnitFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CatalogUnitFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogUnitFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogUnitFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
