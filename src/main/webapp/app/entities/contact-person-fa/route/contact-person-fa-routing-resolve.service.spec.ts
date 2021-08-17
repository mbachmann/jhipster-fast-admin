jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactPersonFa, ContactPersonFa } from '../contact-person-fa.model';
import { ContactPersonFaService } from '../service/contact-person-fa.service';

import { ContactPersonFaRoutingResolveService } from './contact-person-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactPersonFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactPersonFaRoutingResolveService;
    let service: ContactPersonFaService;
    let resultContactPersonFa: IContactPersonFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactPersonFaRoutingResolveService);
      service = TestBed.inject(ContactPersonFaService);
      resultContactPersonFa = undefined;
    });

    describe('resolve', () => {
      it('should return IContactPersonFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPersonFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactPersonFa).toEqual({ id: 123 });
      });

      it('should return new IContactPersonFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPersonFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactPersonFa).toEqual(new ContactPersonFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactPersonFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPersonFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactPersonFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
