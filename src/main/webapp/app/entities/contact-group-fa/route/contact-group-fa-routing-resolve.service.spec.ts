jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactGroupFa, ContactGroupFa } from '../contact-group-fa.model';
import { ContactGroupFaService } from '../service/contact-group-fa.service';

import { ContactGroupFaRoutingResolveService } from './contact-group-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactGroupFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactGroupFaRoutingResolveService;
    let service: ContactGroupFaService;
    let resultContactGroupFa: IContactGroupFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactGroupFaRoutingResolveService);
      service = TestBed.inject(ContactGroupFaService);
      resultContactGroupFa = undefined;
    });

    describe('resolve', () => {
      it('should return IContactGroupFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroupFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactGroupFa).toEqual({ id: 123 });
      });

      it('should return new IContactGroupFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroupFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactGroupFa).toEqual(new ContactGroupFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactGroupFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroupFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactGroupFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
