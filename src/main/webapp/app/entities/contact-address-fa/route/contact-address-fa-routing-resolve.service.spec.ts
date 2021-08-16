jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactAddressFa, ContactAddressFa } from '../contact-address-fa.model';
import { ContactAddressFaService } from '../service/contact-address-fa.service';

import { ContactAddressFaRoutingResolveService } from './contact-address-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactAddressFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactAddressFaRoutingResolveService;
    let service: ContactAddressFaService;
    let resultContactAddressFa: IContactAddressFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactAddressFaRoutingResolveService);
      service = TestBed.inject(ContactAddressFaService);
      resultContactAddressFa = undefined;
    });

    describe('resolve', () => {
      it('should return IContactAddressFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddressFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAddressFa).toEqual({ id: 123 });
      });

      it('should return new IContactAddressFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddressFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactAddressFa).toEqual(new ContactAddressFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactAddressFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAddressFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAddressFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
