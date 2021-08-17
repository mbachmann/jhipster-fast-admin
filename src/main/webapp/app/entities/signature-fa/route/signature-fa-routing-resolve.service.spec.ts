jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISignatureFa, SignatureFa } from '../signature-fa.model';
import { SignatureFaService } from '../service/signature-fa.service';

import { SignatureFaRoutingResolveService } from './signature-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('SignatureFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SignatureFaRoutingResolveService;
    let service: SignatureFaService;
    let resultSignatureFa: ISignatureFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SignatureFaRoutingResolveService);
      service = TestBed.inject(SignatureFaService);
      resultSignatureFa = undefined;
    });

    describe('resolve', () => {
      it('should return ISignatureFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSignatureFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSignatureFa).toEqual({ id: 123 });
      });

      it('should return new ISignatureFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSignatureFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSignatureFa).toEqual(new SignatureFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SignatureFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSignatureFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSignatureFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
