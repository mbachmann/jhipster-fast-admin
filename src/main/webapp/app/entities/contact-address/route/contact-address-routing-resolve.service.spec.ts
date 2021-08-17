jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactAddress, ContactAddress } from '../contact-address.model';
import { ContactAddressService } from '../service/contact-address.service';

import { ContactAddressRoutingResolveService } from './contact-address-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactAddress routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactAddressRoutingResolveService;
    let service: ContactAddressService;
    let resultContactAddress: IContactAddress | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactAddressRoutingResolveService);
      service = TestBed.inject(ContactAddressService);
      resultContactAddress = undefined;
    });

    describe('resolve', () => {
      it('should return IContactAddress returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAddress).toEqual({ id: 123 });
      });

      it('should return new IContactAddress if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddress = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactAddress).toEqual(new ContactAddress());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactAddress })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAddress).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
