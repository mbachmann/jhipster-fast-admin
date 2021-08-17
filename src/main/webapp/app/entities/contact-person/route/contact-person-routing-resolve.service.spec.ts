jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactPerson, ContactPerson } from '../contact-person.model';
import { ContactPersonService } from '../service/contact-person.service';

import { ContactPersonRoutingResolveService } from './contact-person-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactPerson routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactPersonRoutingResolveService;
    let service: ContactPersonService;
    let resultContactPerson: IContactPerson | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactPersonRoutingResolveService);
      service = TestBed.inject(ContactPersonService);
      resultContactPerson = undefined;
    });

    describe('resolve', () => {
      it('should return IContactPerson returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPerson = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactPerson).toEqual({ id: 123 });
      });

      it('should return new IContactPerson if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPerson = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactPerson).toEqual(new ContactPerson());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactPerson })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPerson = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactPerson).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
