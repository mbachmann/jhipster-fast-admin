jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactAddressMySuffix, ContactAddressMySuffix } from '../contact-address-my-suffix.model';
import { ContactAddressMySuffixService } from '../service/contact-address-my-suffix.service';

import { ContactAddressMySuffixRoutingResolveService } from './contact-address-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactAddressMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactAddressMySuffixRoutingResolveService;
    let service: ContactAddressMySuffixService;
    let resultContactAddressMySuffix: IContactAddressMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactAddressMySuffixRoutingResolveService);
      service = TestBed.inject(ContactAddressMySuffixService);
      resultContactAddressMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IContactAddressMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddressMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAddressMySuffix).toEqual({ id: 123 });
      });

      it('should return new IContactAddressMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddressMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactAddressMySuffix).toEqual(new ContactAddressMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactAddressMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddressMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAddressMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
