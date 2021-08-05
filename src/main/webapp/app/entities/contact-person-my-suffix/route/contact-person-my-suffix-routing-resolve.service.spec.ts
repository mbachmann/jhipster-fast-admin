jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactPersonMySuffix, ContactPersonMySuffix } from '../contact-person-my-suffix.model';
import { ContactPersonMySuffixService } from '../service/contact-person-my-suffix.service';

import { ContactPersonMySuffixRoutingResolveService } from './contact-person-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactPersonMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactPersonMySuffixRoutingResolveService;
    let service: ContactPersonMySuffixService;
    let resultContactPersonMySuffix: IContactPersonMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactPersonMySuffixRoutingResolveService);
      service = TestBed.inject(ContactPersonMySuffixService);
      resultContactPersonMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IContactPersonMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPersonMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactPersonMySuffix).toEqual({ id: 123 });
      });

      it('should return new IContactPersonMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPersonMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactPersonMySuffix).toEqual(new ContactPersonMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactPersonMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactPersonMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactPersonMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
