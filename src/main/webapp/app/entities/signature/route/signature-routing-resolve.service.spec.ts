jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISignature, Signature } from '../signature.model';
import { SignatureService } from '../service/signature.service';

import { SignatureRoutingResolveService } from './signature-routing-resolve.service';

describe('Service Tests', () => {
  describe('Signature routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SignatureRoutingResolveService;
    let service: SignatureService;
    let resultSignature: ISignature | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SignatureRoutingResolveService);
      service = TestBed.inject(SignatureService);
      resultSignature = undefined;
    });

    describe('resolve', () => {
      it('should return ISignature returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSignature = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSignature).toEqual({ id: 123 });
      });

      it('should return new ISignature if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSignature = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSignature).toEqual(new Signature());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Signature })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSignature = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSignature).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
