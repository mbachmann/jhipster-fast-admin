jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactAccountFa, ContactAccountFa } from '../contact-account-fa.model';
import { ContactAccountFaService } from '../service/contact-account-fa.service';

import { ContactAccountFaRoutingResolveService } from './contact-account-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactAccountFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactAccountFaRoutingResolveService;
    let service: ContactAccountFaService;
    let resultContactAccountFa: IContactAccountFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactAccountFaRoutingResolveService);
      service = TestBed.inject(ContactAccountFaService);
      resultContactAccountFa = undefined;
    });

    describe('resolve', () => {
      it('should return IContactAccountFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAccountFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAccountFa).toEqual({ id: 123 });
      });

      it('should return new IContactAccountFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAccountFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactAccountFa).toEqual(new ContactAccountFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactAccountFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactAccountFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactAccountFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
