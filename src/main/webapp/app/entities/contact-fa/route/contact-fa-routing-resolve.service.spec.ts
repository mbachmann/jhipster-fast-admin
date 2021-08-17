jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactFa, ContactFa } from '../contact-fa.model';
import { ContactFaService } from '../service/contact-fa.service';

import { ContactFaRoutingResolveService } from './contact-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactFaRoutingResolveService;
    let service: ContactFaService;
    let resultContactFa: IContactFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactFaRoutingResolveService);
      service = TestBed.inject(ContactFaService);
      resultContactFa = undefined;
    });

    describe('resolve', () => {
      it('should return IContactFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactFa).toEqual({ id: 123 });
      });

      it('should return new IContactFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactFa).toEqual(new ContactFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
