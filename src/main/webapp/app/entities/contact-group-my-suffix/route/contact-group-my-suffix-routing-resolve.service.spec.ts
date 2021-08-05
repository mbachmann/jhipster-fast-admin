jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactGroupMySuffix, ContactGroupMySuffix } from '../contact-group-my-suffix.model';
import { ContactGroupMySuffixService } from '../service/contact-group-my-suffix.service';

import { ContactGroupMySuffixRoutingResolveService } from './contact-group-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactGroupMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactGroupMySuffixRoutingResolveService;
    let service: ContactGroupMySuffixService;
    let resultContactGroupMySuffix: IContactGroupMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactGroupMySuffixRoutingResolveService);
      service = TestBed.inject(ContactGroupMySuffixService);
      resultContactGroupMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IContactGroupMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroupMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactGroupMySuffix).toEqual({ id: 123 });
      });

      it('should return new IContactGroupMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroupMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactGroupMySuffix).toEqual(new ContactGroupMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactGroupMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroupMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactGroupMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
