jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactAccount, ContactAccount } from '../contact-account.model';
import { ContactAccountService } from '../service/contact-account.service';

import { ContactAccountRoutingResolveService } from './contact-account-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactAccount routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactAccountRoutingResolveService;
    let service: ContactAccountService;
    let resultContactAccount: IContactAccount | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactAccountRoutingResolveService);
      service = TestBed.inject(ContactAccountService);
      resultContactAccount = undefined;
    });

    describe('resolve', () => {
      it('should return IContactAccount returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAccount = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAccount).toEqual({ id: 123 });
      });

      it('should return new IContactAccount if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAccount = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactAccount).toEqual(new ContactAccount());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactAccount })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAccount = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAccount).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
