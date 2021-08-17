jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICustomField, CustomField } from '../custom-field.model';
import { CustomFieldService } from '../service/custom-field.service';

import { CustomFieldRoutingResolveService } from './custom-field-routing-resolve.service';

describe('Service Tests', () => {
  describe('CustomField routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CustomFieldRoutingResolveService;
    let service: CustomFieldService;
    let resultCustomField: ICustomField | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CustomFieldRoutingResolveService);
      service = TestBed.inject(CustomFieldService);
      resultCustomField = undefined;
    });

    describe('resolve', () => {
      it('should return ICustomField returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCustomField = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCustomField).toEqual({ id: 123 });
      });

      it('should return new ICustomField if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCustomField = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCustomField).toEqual(new CustomField());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CustomField })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCustomField = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCustomField).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
