jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICatalogUnit, CatalogUnit } from '../catalog-unit.model';
import { CatalogUnitService } from '../service/catalog-unit.service';

import { CatalogUnitRoutingResolveService } from './catalog-unit-routing-resolve.service';

describe('Service Tests', () => {
  describe('CatalogUnit routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CatalogUnitRoutingResolveService;
    let service: CatalogUnitService;
    let resultCatalogUnit: ICatalogUnit | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CatalogUnitRoutingResolveService);
      service = TestBed.inject(CatalogUnitService);
      resultCatalogUnit = undefined;
    });

    describe('resolve', () => {
      it('should return ICatalogUnit returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogUnit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogUnit).toEqual({ id: 123 });
      });

      it('should return new ICatalogUnit if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogUnit = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCatalogUnit).toEqual(new CatalogUnit());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CatalogUnit })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCatalogUnit = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCatalogUnit).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
